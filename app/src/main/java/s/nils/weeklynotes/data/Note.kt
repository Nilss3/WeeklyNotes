package s.nils.weeklynotes.data

import java.time.LocalDate

enum class NoteStatus {
    BLANK, DONE, CANCELLED, MOVED, INFO
}

data class Note(
    val id: String = java.util.UUID.randomUUID().toString(),
    val content: String,
    val status: NoteStatus = NoteStatus.BLANK,
    val date: LocalDate = LocalDate.now(),
    val order: Int = 0
) {
    fun nextStatus(): NoteStatus {
        return when (status) {
            NoteStatus.BLANK -> NoteStatus.DONE
            NoteStatus.DONE -> NoteStatus.CANCELLED
            NoteStatus.CANCELLED -> NoteStatus.MOVED
            NoteStatus.MOVED -> NoteStatus.INFO
            NoteStatus.INFO -> NoteStatus.BLANK
        }
    }
    
    fun getStatusSymbol(): String {
        return when (status) {
            NoteStatus.BLANK -> ""
            NoteStatus.DONE -> "V"
            NoteStatus.CANCELLED -> "X"
            NoteStatus.MOVED -> ">"
            NoteStatus.INFO -> "-"
        }
    }
} 