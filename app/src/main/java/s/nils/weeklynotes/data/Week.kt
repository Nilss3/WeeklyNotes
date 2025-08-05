package s.nils.weeklynotes.data

import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

data class Week(
    val year: Int,
    val weekNumber: Int,
    val notes: List<Note> = emptyList()
) {
    val startDate: LocalDate = LocalDate.of(year, 1, 1)
        .with(WeekFields.ISO.weekBasedYear(), year.toLong())
        .with(WeekFields.ISO.weekOfWeekBasedYear(), weekNumber.toLong())
        .with(WeekFields.ISO.dayOfWeek(), 1L)
    
    val endDate: LocalDate = startDate.plusDays(6)
    
    val dateRange: String
        get() = buildString {
            append("Mo\n${startDate.dayOfMonth}\n${startDate.month.name.take(3)}")
            append("    Tu\n${startDate.plusDays(1).dayOfMonth}\n${startDate.plusDays(1).month.name.take(3)}")
            append("    We\n${startDate.plusDays(2).dayOfMonth}\n${startDate.plusDays(2).month.name.take(3)}")
            append("    Thu\n${startDate.plusDays(3).dayOfMonth}\n${startDate.plusDays(3).month.name.take(3)}")
            append("    Fr\n${startDate.plusDays(4).dayOfMonth}\n${startDate.plusDays(4).month.name.take(3)}")
            append("    Sa\n${startDate.plusDays(5).dayOfMonth}\n${startDate.plusDays(5).month.name.take(3)}")
            append("    Su\n${endDate.dayOfMonth}\n${endDate.month.name.take(3)}")
        }
    
    val title: String
        get() = "$year week $weekNumber"
} 