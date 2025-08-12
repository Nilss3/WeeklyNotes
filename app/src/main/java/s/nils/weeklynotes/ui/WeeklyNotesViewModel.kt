package s.nils.weeklynotes.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import s.nils.weeklynotes.data.Note
import s.nils.weeklynotes.data.NoteStatus
import s.nils.weeklynotes.data.NotesStorage
import s.nils.weeklynotes.data.Week
import s.nils.weeklynotes.ui.theme.ColorScheme
import java.io.File
import android.net.Uri
import android.content.Context
import java.time.LocalDate
import java.time.temporal.WeekFields

data class WeeklyNotesUiState(
    val currentWeek: Week = NotesStorage.getCurrentWeek(),
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val showImportWarning: Boolean = false,
    val pendingImportUri: Uri? = null,
    val hideClosedNotes: Boolean = false,
    val colorScheme: ColorScheme = ColorScheme.LIGHT,
    val customTextColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Black,
    val customBackgroundColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.White
)

class WeeklyNotesViewModel(application: Application) : AndroidViewModel(application) {
    private val storage = NotesStorage(application)
    private val _uiState = MutableStateFlow(WeeklyNotesUiState())
    val uiState: StateFlow<WeeklyNotesUiState> = _uiState.asStateFlow()

    init {
        loadCurrentWeek()
        loadColorScheme()
        loadCustomColors()
    }

    private fun loadCurrentWeek() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val week = _uiState.value.currentWeek
            val loadedWeek = storage.loadWeek(week.year, week.weekNumber)
            
            _uiState.update { 
                it.copy(
                    currentWeek = loadedWeek ?: week,
                    notes = loadedWeek?.notes ?: emptyList(),
                    isLoading = false
                )
            }
        }
    }

    fun navigateToPreviousWeek() {
        viewModelScope.launch {
            val currentWeek = _uiState.value.currentWeek
            val newWeekNumber = if (currentWeek.weekNumber > 1) currentWeek.weekNumber - 1 else 52
            val newYear = if (currentWeek.weekNumber > 1) currentWeek.year else currentWeek.year - 1
            
            val newWeek = Week(year = newYear, weekNumber = newWeekNumber)
            val loadedWeek = storage.loadWeek(newYear, newWeekNumber)
            
            _uiState.update { 
                it.copy(
                    currentWeek = loadedWeek ?: newWeek,
                    notes = loadedWeek?.notes ?: emptyList()
                )
            }
        }
    }

    fun navigateToNextWeek() {
        viewModelScope.launch {
            val currentWeek = _uiState.value.currentWeek
            val newWeekNumber = if (currentWeek.weekNumber < 52) currentWeek.weekNumber + 1 else 1
            val newYear = if (currentWeek.weekNumber < 52) currentWeek.year else currentWeek.year + 1
            
            val newWeek = Week(year = newYear, weekNumber = newWeekNumber)
            val loadedWeek = storage.loadWeek(newYear, newWeekNumber)
            
            _uiState.update { 
                it.copy(
                    currentWeek = loadedWeek ?: newWeek,
                    notes = loadedWeek?.notes ?: emptyList()
                )
            }
        }
    }

    fun navigateToDate(selectedDate: LocalDate) {
        viewModelScope.launch {
            // Calculate the week number for the selected date
            val weekFields = WeekFields.ISO
            val year = selectedDate.get(weekFields.weekBasedYear())
            val weekNumber = selectedDate.get(weekFields.weekOfWeekBasedYear())
            
            val newWeek = Week(year = year, weekNumber = weekNumber)
            val loadedWeek = storage.loadWeek(year, weekNumber)
            
            _uiState.update { 
                it.copy(
                    currentWeek = loadedWeek ?: newWeek,
                    notes = loadedWeek?.notes ?: emptyList()
                )
            }
        }
    }

    fun addNote() {
        val currentNotes = _uiState.value.notes
        val newNote = Note(
            content = "",
            status = NoteStatus.INFO,
            order = currentNotes.size
        )
        
        val updatedNotes = currentNotes + newNote
        _uiState.update { it.copy(notes = updatedNotes) }
        saveCurrentWeek()
    }

    fun updateNoteContent(noteId: String, content: String) {
        val currentNotes = _uiState.value.notes
        val updatedNotes = currentNotes.map { note ->
            if (note.id == noteId) {
                note.copy(content = content)
            } else {
                note
            }
        }.filter { it.content.isNotBlank() } // Remove empty notes
        
        _uiState.update { it.copy(notes = updatedNotes) }
        saveCurrentWeek()
    }

    fun cycleNoteStatus(noteId: String) {
        val currentNotes = _uiState.value.notes
        val updatedNotes = currentNotes.map { note ->
            if (note.id == noteId) {
                note.copy(status = note.nextStatus())
            } else {
                note
            }
        }
        
        _uiState.update { it.copy(notes = updatedNotes) }
        saveCurrentWeek()
    }

    private fun saveCurrentWeek() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val weekToSave = currentState.currentWeek.copy(notes = currentState.notes)
            storage.saveWeek(weekToSave)
        }
    }

    fun exportNotes() {
        viewModelScope.launch {
            try {
                val exportFile = storage.getExportFile()
                // The file is now saved in the accessible location
                // User can access it through file manager
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    fun exportToLocation(uri: Uri) {
        viewModelScope.launch {
            try {
                val exportData = storage.getAllWeeksForExport()
                android.util.Log.d("WeeklyNotes", "Exporting ${exportData.size} weeks")
                exportData.forEach { weekData ->
                    android.util.Log.d("WeeklyNotes", "Exporting week ${weekData.year}-${weekData.weekNumber} with ${weekData.notes.size} notes")
                }
                
                val json = storage.gson.toJson(exportData)
                android.util.Log.d("WeeklyNotes", "Export JSON size: ${json.length} characters")
                
                getApplication<Application>().contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(json.toByteArray())
                }
                android.util.Log.d("WeeklyNotes", "Export completed successfully")
            } catch (e: Exception) {
                android.util.Log.e("WeeklyNotes", "Export error: ${e.message}", e)
            }
        }
    }

    fun importNotes() {
        // This will be handled by the file picker in MainActivity
    }
    
    fun importFromFile(uri: Uri) {
        android.util.Log.d("WeeklyNotes", "importFromFile called with URI: $uri")
        _uiState.update { 
            it.copy(
                showImportWarning = true,
                pendingImportUri = uri
            )
        }
    }
    
    fun confirmImport() {
        android.util.Log.d("WeeklyNotes", "confirmImport called")
        viewModelScope.launch {
            val uri = _uiState.value.pendingImportUri
            android.util.Log.d("WeeklyNotes", "confirmImport URI: $uri")
            if (uri != null) {
                try {
                    val inputStream = getApplication<Application>().contentResolver.openInputStream(uri)
                    val json = inputStream?.bufferedReader().use { it?.readText() } ?: return@launch
                    
                    android.util.Log.d("WeeklyNotes", "Importing JSON: ${json.take(100)}...")
                    
                    if (storage.importFromJson(json)) {
                        android.util.Log.d("WeeklyNotes", "Import successful, loading current week")
                        loadCurrentWeek()
                    } else {
                        android.util.Log.e("WeeklyNotes", "Import failed")
                    }
                } catch (e: Exception) {
                    android.util.Log.e("WeeklyNotes", "Import error: ${e.message}", e)
                }
            }
            _uiState.update { 
                it.copy(
                    showImportWarning = false,
                    pendingImportUri = null
                )
            }
        }
    }
    
    fun dismissImportWarning() {
        _uiState.update { 
            it.copy(
                showImportWarning = false,
                pendingImportUri = null
            )
        }
    }
    
    fun toggleClosedNotesVisibility() {
        _uiState.update { 
            it.copy(
                hideClosedNotes = !it.hideClosedNotes
            )
        }
    }
    
    fun changeNoteStatus(noteId: String, newStatus: NoteStatus) {
        val currentNotes = _uiState.value.notes
        val updatedNotes = currentNotes.map { note ->
            if (note.id == noteId) {
                note.copy(status = newStatus)
            } else {
                note
            }
        }
        
        _uiState.update { it.copy(notes = updatedNotes) }
        saveCurrentWeek()
    }
    
    fun moveNoteToNextWeek(noteId: String) {
        viewModelScope.launch {
            val currentNotes = _uiState.value.notes
            val noteToMove = currentNotes.find { it.id == noteId }
            
            if (noteToMove != null) {
                // Remove note from current week
                val updatedNotes = currentNotes.filter { it.id != noteId }
                _uiState.update { it.copy(notes = updatedNotes) }
                saveCurrentWeek()
                
                // Calculate next week
                val currentWeek = _uiState.value.currentWeek
                val nextWeekNumber = if (currentWeek.weekNumber < 52) currentWeek.weekNumber + 1 else 1
                val nextYear = if (currentWeek.weekNumber < 52) currentWeek.year else currentWeek.year + 1
                
                // Load next week and add the note
                val nextWeek = Week(year = nextYear, weekNumber = nextWeekNumber)
                val existingNextWeek = storage.loadWeek(nextYear, nextWeekNumber)
                
                val nextWeekNotes = existingNextWeek?.notes?.toMutableList() ?: mutableListOf()
                val movedNote = noteToMove.copy(
                    date = nextWeek.startDate,
                    order = nextWeekNotes.size
                )
                nextWeekNotes.add(movedNote)
                
                val updatedNextWeek = nextWeek.copy(notes = nextWeekNotes)
                storage.saveWeek(updatedNextWeek)
            }
        }
    }
    
    fun deleteNote(noteId: String) {
        val currentNotes = _uiState.value.notes
        val updatedNotes = currentNotes.filter { it.id != noteId }
        
        _uiState.update { it.copy(notes = updatedNotes) }
        saveCurrentWeek()
    }
    
    fun moveNoteToTop(noteId: String) {
        val currentNotes = _uiState.value.notes
        val noteIndex = currentNotes.indexOfFirst { it.id == noteId }
        
        if (noteIndex > 0) {
            val note = currentNotes[noteIndex]
            val updatedNotes = currentNotes.toMutableList()
            updatedNotes.removeAt(noteIndex)
            updatedNotes.add(0, note.copy(order = 0))
            
            // Update order for all notes
            val reorderedNotes = updatedNotes.mapIndexed { index, note ->
                note.copy(order = index)
            }
            
            _uiState.update { it.copy(notes = reorderedNotes) }
            saveCurrentWeek()
        }
    }
    
    fun moveNoteUp(noteId: String) {
        val currentNotes = _uiState.value.notes
        val noteIndex = currentNotes.indexOfFirst { it.id == noteId }
        
        if (noteIndex > 0) {
            val updatedNotes = currentNotes.toMutableList()
            val note = updatedNotes[noteIndex]
            val previousNote = updatedNotes[noteIndex - 1]
            
            updatedNotes[noteIndex] = previousNote.copy(order = note.order)
            updatedNotes[noteIndex - 1] = note.copy(order = previousNote.order)
            
            _uiState.update { it.copy(notes = updatedNotes) }
            saveCurrentWeek()
        }
    }
    
    fun moveNoteDown(noteId: String) {
        val currentNotes = _uiState.value.notes
        val noteIndex = currentNotes.indexOfFirst { it.id == noteId }
        
        if (noteIndex >= 0 && noteIndex < currentNotes.size - 1) {
            val updatedNotes = currentNotes.toMutableList()
            val note = updatedNotes[noteIndex]
            val nextNote = updatedNotes[noteIndex + 1]
            
            updatedNotes[noteIndex] = nextNote.copy(order = note.order)
            updatedNotes[noteIndex + 1] = note.copy(order = nextNote.order)
            
            _uiState.update { it.copy(notes = updatedNotes) }
            saveCurrentWeek()
        }
    }
    
    fun moveNoteToBottom(noteId: String) {
        val currentNotes = _uiState.value.notes
        val noteIndex = currentNotes.indexOfFirst { it.id == noteId }
        
        if (noteIndex >= 0 && noteIndex < currentNotes.size - 1) {
            val note = currentNotes[noteIndex]
            val updatedNotes = currentNotes.toMutableList()
            updatedNotes.removeAt(noteIndex)
            updatedNotes.add(note.copy(order = currentNotes.size - 1))
            
            // Update order for all notes
            val reorderedNotes = updatedNotes.mapIndexed { index, note ->
                note.copy(order = index)
            }
            
            _uiState.update { it.copy(notes = reorderedNotes) }
            saveCurrentWeek()
        }
    }
    
    // Color scheme management
    fun updateColorScheme(colorScheme: ColorScheme) {
        _uiState.update { it.copy(colorScheme = colorScheme) }
        viewModelScope.launch {
            storage.saveColorScheme(colorScheme)
        }
    }
    
    private fun loadColorScheme() {
        viewModelScope.launch {
            val savedColorScheme = storage.loadColorScheme()
            _uiState.update { it.copy(colorScheme = savedColorScheme) }
        }
    }
    
    private fun loadCustomColors() {
        viewModelScope.launch {
            val (textColor, backgroundColor) = storage.loadCustomColors()
            _uiState.update { it.copy(customTextColor = textColor, customBackgroundColor = backgroundColor) }
        }
    }
    
    fun updateCustomTextColor(color: androidx.compose.ui.graphics.Color) {
        _uiState.update { it.copy(customTextColor = color) }
        viewModelScope.launch {
            storage.saveCustomColors(uiState.value.customTextColor, uiState.value.customBackgroundColor)
        }
    }
    
    fun updateCustomBackgroundColor(color: androidx.compose.ui.graphics.Color) {
        _uiState.update { it.copy(customBackgroundColor = color) }
        viewModelScope.launch {
            storage.saveCustomColors(uiState.value.customTextColor, uiState.value.customBackgroundColor)
        }
    }
} 