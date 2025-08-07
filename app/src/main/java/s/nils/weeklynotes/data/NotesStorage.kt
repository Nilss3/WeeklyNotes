package s.nils.weeklynotes.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

data class WeekData(
    val year: Int,
    val weekNumber: Int,
    val notes: List<NoteData>
)

data class NoteData(
    val id: String,
    val content: String,
    val status: String,
    val date: String,
    val order: Int
)

class NotesStorage(private val context: Context) {
    private val gson = Gson()
    private val storageDir = File(context.getExternalFilesDir(null), "weekly notes")
    
    init {
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
    }
    
    suspend fun saveWeek(week: Week): Unit = withContext(Dispatchers.IO) {
        val weekData = WeekData(
            year = week.year,
            weekNumber = week.weekNumber,
            notes = week.notes.map { note ->
                NoteData(
                    id = note.id,
                    content = note.content,
                    status = note.status.name,
                    date = note.date.toString(),
                    order = note.order
                )
            }
        )
        
        val json = gson.toJson(weekData)
        val file = File(storageDir, "week_${week.year}_${week.weekNumber}.json")
        file.writeText(json)
    }
    
    suspend fun loadWeek(year: Int, weekNumber: Int): Week? = withContext(Dispatchers.IO) {
        val file = File(storageDir, "week_${year}_${weekNumber}.json")
        if (!file.exists()) return@withContext null
        
        try {
            val json = file.readText()
            val weekData = gson.fromJson<WeekData>(json, object : TypeToken<WeekData>() {}.type)
            
            Week(
                year = weekData.year,
                weekNumber = weekData.weekNumber,
                notes = weekData.notes.map { noteData ->
                    Note(
                        id = noteData.id,
                        content = noteData.content,
                        status = NoteStatus.valueOf(noteData.status),
                        date = LocalDate.parse(noteData.date),
                        order = noteData.order
                    )
                }.sortedBy { it.order }
            )
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun getAllWeeks(): List<Week> = withContext(Dispatchers.IO) {
        val weeks = mutableListOf<Week>()
        storageDir.listFiles()?.forEach { file ->
            if (file.name.startsWith("week_") && file.name.endsWith(".json")) {
                try {
                    val json = file.readText()
                    val weekData = gson.fromJson<WeekData>(json, object : TypeToken<WeekData>() {}.type)
                    
                    val week = Week(
                        year = weekData.year,
                        weekNumber = weekData.weekNumber,
                        notes = weekData.notes.map { noteData ->
                            Note(
                                id = noteData.id,
                                content = noteData.content,
                                status = NoteStatus.valueOf(noteData.status),
                                date = LocalDate.parse(noteData.date),
                                order = noteData.order
                            )
                        }.sortedBy { it.order }
                    )
                    weeks.add(week)
                } catch (e: Exception) {
                    // Skip corrupted files
                }
            }
        }
        weeks
    }
    
    fun getExportFile(): File {
        val allWeeks = runBlocking { getAllWeeks() }
        val exportData = allWeeks.map { week ->
            WeekData(
                year = week.year,
                weekNumber = week.weekNumber,
                notes = week.notes.map { note ->
                    NoteData(
                        id = note.id,
                        content = note.content,
                        status = note.status.name,
                        date = note.date.toString(),
                        order = note.order
                    )
                }
            )
        }
        
        val json = gson.toJson(exportData)
        val exportFile = File(storageDir, "weekly_notes_export.json")
        exportFile.writeText(json)
        return exportFile
    }
    
    suspend fun importFromFile(file: File): Boolean = withContext(Dispatchers.IO) {
        try {
            val json = file.readText()
            val weeksData = gson.fromJson<List<WeekData>>(json, object : TypeToken<List<WeekData>>() {}.type)
            
            weeksData.forEach { weekData ->
                val week = Week(
                    year = weekData.year,
                    weekNumber = weekData.weekNumber,
                    notes = weekData.notes.map { noteData ->
                        Note(
                            id = noteData.id,
                            content = noteData.content,
                            status = NoteStatus.valueOf(noteData.status),
                            date = LocalDate.parse(noteData.date),
                            order = noteData.order
                        )
                    }.sortedBy { it.order }
                )
                saveWeek(week)
            }
            true
        } catch (e: Exception) {
            false
        }
    }
    
    companion object {
        fun getCurrentWeek(): Week {
            val now = LocalDate.now()
            // Use ISO week fields to ensure consistent week numbering
            val weekFields = WeekFields.ISO
            val year = now.get(weekFields.weekBasedYear())
            val weekNumber = now.get(weekFields.weekOfWeekBasedYear())
            return Week(year = year, weekNumber = weekNumber)
        }
    }
} 