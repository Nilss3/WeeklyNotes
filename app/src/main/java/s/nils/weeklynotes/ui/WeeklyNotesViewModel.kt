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

data class WeeklyNotesUiState(
    val currentWeek: Week = NotesStorage.getCurrentWeek(),
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false
)

class WeeklyNotesViewModel(application: Application) : AndroidViewModel(application) {
    private val storage = NotesStorage(application)
    private val _uiState = MutableStateFlow(WeeklyNotesUiState())
    val uiState: StateFlow<WeeklyNotesUiState> = _uiState.asStateFlow()

    init {
        loadCurrentWeek()
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
            storage.getExportFile()
        }
    }

    fun importNotes(filePath: String) {
        viewModelScope.launch {
            val file = java.io.File(filePath)
            if (storage.importFromFile(file)) {
                loadCurrentWeek()
            }
        }
    }
} 