package s.nils.weeklynotes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlinx.coroutines.delay
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import s.nils.weeklynotes.data.Note
import s.nils.weeklynotes.ui.theme.ColorScheme
import androidx.compose.ui.tooling.preview.Preview
import s.nils.weeklynotes.ui.theme.WeeklyNotesTheme
import java.time.LocalDate
import java.time.ZoneOffset
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyHeader(
    week: s.nils.weeklynotes.data.Week,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
    onExportNotes: () -> Unit,
    onImportNotes: () -> Unit,
    onToggleClosedNotes: () -> Unit,
    hideClosedNotes: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    onColorSchemeClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hamburger menu button (left)
            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .clickable { showMenu = true }
                    .border(2.dp, MaterialTheme.colorScheme.onSurface, shape = MaterialTheme.shapes.small)
                    .clip(MaterialTheme.shapes.small),
                color = MaterialTheme.colorScheme.surface
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            // Week navigation (right)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Previous week button
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onPreviousWeek() }
                        .border(2.dp, MaterialTheme.colorScheme.onSurface, shape = MaterialTheme.shapes.small)
                        .clip(MaterialTheme.shapes.small),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous week",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                // Week title - clickable to open date picker
                Text(
                    text = week.title,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clickable { showDatePicker = true }
                        .padding(8.dp)
                )
                
                // Next week button
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onNextWeek() }
                        .border(2.dp, MaterialTheme.colorScheme.onSurface, shape = MaterialTheme.shapes.small)
                        .clip(MaterialTheme.shapes.small),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next week",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
        
        // Native Android dropdown menu
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.onSurface, shape = MaterialTheme.shapes.small)
        ) {
            DropdownMenuItem(
                text = { 
                    Text(
                        if (hideClosedNotes) "Unhide closed tasks" else "Hide closed tasks",
                        color = MaterialTheme.colorScheme.onSurface
                    ) 
                },
                onClick = {
                    onToggleClosedNotes()
                    showMenu = false
                }
            )
            
            // Divider
            androidx.compose.material3.Divider(color = MaterialTheme.colorScheme.onSurfaceVariant, thickness = 1.dp)
            
            DropdownMenuItem(
                text = { Text("Go to current week", color = MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    onDateSelected(LocalDate.now())
                    showMenu = false
                }
            )
            
            // Divider
            androidx.compose.material3.Divider(color = MaterialTheme.colorScheme.onSurfaceVariant, thickness = 1.dp)
            
            DropdownMenuItem(
                text = { Text("Color scheme", color = MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    onColorSchemeClick()
                    showMenu = false
                }
            )
            
            // Divider
            androidx.compose.material3.Divider(color = MaterialTheme.colorScheme.onSurfaceVariant, thickness = 1.dp)
            
            DropdownMenuItem(
                text = { Text("Import notes...", color = MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    onImportNotes()
                    showMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text("Export notes...", color = MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    onExportNotes()
                    showMenu = false
                }
            )
        }
        
        // Date picker dialog
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()
            
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val selectedDate = java.time.Instant.ofEpochMilli(millis)
                                    .atZone(ZoneOffset.UTC)
                                    .toLocalDate()
                                onDateSelected(selectedDate)
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onContentChange: (String) -> Unit,
    onStatusClick: () -> Unit,
    onStatusChange: (s.nils.weeklynotes.data.NoteStatus) -> Unit,
    onMoveToNextWeek: () -> Unit,
    onMoveToTop: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onMoveToBottom: () -> Unit,
    onDelete: () -> Unit,
    isNewNote: Boolean,
    isFirst: Boolean,
    isLast: Boolean
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(note.content)) }
    val focusRequester = remember { FocusRequester() }
    var showContextMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(note.content) {
        textFieldValue = TextFieldValue(note.content)
    }

    // Auto-focus for new notes
    LaunchedEffect(isNewNote) {
        if (isNewNote) {
            focusRequester.requestFocus()
        }
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.onSurface, shape = MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.small)
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Status button
        Surface(
            modifier = Modifier
                .size(32.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onStatusClick() },
                        onLongPress = { showContextMenu = true }
                    )
                }
                .border(2.dp, MaterialTheme.colorScheme.onSurface, shape = MaterialTheme.shapes.small)
                .clip(MaterialTheme.shapes.small),
            color = MaterialTheme.colorScheme.surface
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = note.getStatusSymbol(),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        
        // Contextual menu
        DropdownMenu(
            expanded = showContextMenu,
            onDismissRequest = { showContextMenu = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.onSurface, shape = MaterialTheme.shapes.small)
        ) {
            // Mark as submenu
            var showMarkAsSubmenu by remember { mutableStateOf(false) }
            
            DropdownMenuItem(
                text = { 
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Mark as", color = MaterialTheme.colorScheme.onSurface)
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Submenu",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                },
                onClick = {
                    showMarkAsSubmenu = true
                }
            )
            
            // Mark as submenu
            DropdownMenu(
                expanded = showMarkAsSubmenu,
                onDismissRequest = { showMarkAsSubmenu = false },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface, shape = MaterialTheme.shapes.small)
            ) {
                DropdownMenuItem(
                    text = { Text("(-) Note/info", color = MaterialTheme.colorScheme.onSurface) },
                    onClick = {
                        onStatusChange(s.nils.weeklynotes.data.NoteStatus.INFO)
                        showMarkAsSubmenu = false
                        showContextMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("() Open task: To do", color = MaterialTheme.colorScheme.onSurface) },
                    onClick = {
                        onStatusChange(s.nils.weeklynotes.data.NoteStatus.BLANK)
                        showMarkAsSubmenu = false
                        showContextMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("(V) Closed task: Done", color = MaterialTheme.colorScheme.onSurface) },
                    onClick = {
                        onStatusChange(s.nils.weeklynotes.data.NoteStatus.DONE)
                        showMarkAsSubmenu = false
                        showContextMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("(X) Closed task: Cancelled", color = MaterialTheme.colorScheme.onSurface) },
                    onClick = {
                        onStatusChange(s.nils.weeklynotes.data.NoteStatus.CANCELLED)
                        showMarkAsSubmenu = false
                        showContextMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("(>) Closed task: Moved", color = MaterialTheme.colorScheme.onSurface) },
                    onClick = {
                        onStatusChange(s.nils.weeklynotes.data.NoteStatus.MOVED)
                        showMarkAsSubmenu = false
                        showContextMenu = false
                    }
                )
            }
            
            // Divider
            androidx.compose.material3.Divider(color = MaterialTheme.colorScheme.onSurfaceVariant, thickness = 1.dp)
            
            // Move to next week option
            DropdownMenuItem(
                text = { Text("Move to next week", color = MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    onMoveToNextWeek()
                    showContextMenu = false
                }
            )
            
            // Divider
            androidx.compose.material3.Divider(color = MaterialTheme.colorScheme.onSurfaceVariant, thickness = 1.dp)
            
            // Move options
            DropdownMenuItem(
                text = { Text("Move to top", color = if (isFirst) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    if (!isFirst) {
                        onMoveToTop()
                    }
                    showContextMenu = false
                },
                enabled = !isFirst
            )
            DropdownMenuItem(
                text = { Text("Move up", color = if (isFirst) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    if (!isFirst) {
                        onMoveUp()
                    }
                    showContextMenu = false
                },
                enabled = !isFirst
            )
            DropdownMenuItem(
                text = { Text("Move down", color = if (isLast) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    if (!isLast) {
                        onMoveDown()
                    }
                    showContextMenu = false
                },
                enabled = !isLast
            )
            DropdownMenuItem(
                text = { Text("Move to bottom", color = if (isLast) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    if (!isLast) {
                        onMoveToBottom()
                    }
                    showContextMenu = false
                },
                enabled = !isLast
            )
            
            // Divider
            androidx.compose.material3.Divider(color = MaterialTheme.colorScheme.onSurfaceVariant, thickness = 1.dp)
            
            // Delete option
            DropdownMenuItem(
                text = { Text("Delete", color = MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    showDeleteDialog = true
                    showContextMenu = false
                }
            )
        }
        
        // Delete confirmation dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Note") },
                text = { Text("Are you sure you want to delete this note?") },
                confirmButton = {
                    TextButton(onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Note content text field
        BasicTextField(
            value = textFieldValue.text,
            onValueChange = { newText ->
                textFieldValue = TextFieldValue(newText)
                onContentChange(newText)
            },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            singleLine = false,
            maxLines = Int.MAX_VALUE // Allow unlimited lines
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyNotesScreen(
    viewModel: WeeklyNotesViewModel,
    onExportNotes: () -> Unit = { viewModel.exportNotes() },
    onImportNotes: () -> Unit = { viewModel.importNotes() }
) {
    var showColorSchemeScreen by remember { mutableStateOf(false) }
    var showCustomColorScreen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    
    fun openCalendarApp(date: LocalDate) {
        try {
            // Convert date to milliseconds for calendar intent
            val timeInMillis = date.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
            
            // Create calendar intent with specific time
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("content://com.android.calendar/time/$timeInMillis")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            
            // Try to launch the calendar app
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                // Fallback: try alternative format
                val fallbackIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("content://com.android.calendar/time/")
                    putExtra("beginTime", timeInMillis)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(fallbackIntent)
            }
        } catch (e: Exception) {
            // If calendar app is not available, do nothing
        }
    }
    
    // Scroll to bottom when new notes are added
    LaunchedEffect(uiState.notes.size) {
        if (uiState.notes.isNotEmpty()) {
            // Add a longer delay to ensure the item is rendered before scrolling
            kotlinx.coroutines.delay(300)
            listState.animateScrollToItem(uiState.notes.size - 1)
        }
    }

    if (showCustomColorScreen) {
        CustomColorScreen(
            currentTextColor = uiState.customTextColor,
            currentBackgroundColor = uiState.customBackgroundColor,
            onTextColorChanged = { color ->
                viewModel.updateCustomTextColor(color)
            },
            onBackgroundColorChanged = { color ->
                viewModel.updateCustomBackgroundColor(color)
            },
            onSave = {
                showCustomColorScreen = false
            },
            onBackPressed = { showCustomColorScreen = false }
        )
    } else if (showColorSchemeScreen) {
        ColorSchemeScreen(
            currentColorScheme = uiState.colorScheme,
            onColorSchemeSelected = { colorScheme ->
                if (colorScheme == ColorScheme.CUSTOM) {
                    viewModel.updateColorScheme(colorScheme)
                    showCustomColorScreen = true
                    showColorSchemeScreen = false
                } else {
                    viewModel.updateColorScheme(colorScheme)
                }
            },
            onBackPressed = { showColorSchemeScreen = false }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp) // Add space for status bar
            ) {
                // Header with week navigation
                WeeklyHeader(
                    week = uiState.currentWeek,
                    onPreviousWeek = { viewModel.navigateToPreviousWeek() },
                    onNextWeek = { viewModel.navigateToNextWeek() },
                    onExportNotes = onExportNotes,
                    onImportNotes = onImportNotes,
                    onToggleClosedNotes = { viewModel.toggleClosedNotesVisibility() },
                    hideClosedNotes = uiState.hideClosedNotes,
                    onDateSelected = { selectedDate -> viewModel.navigateToDate(selectedDate) },
                    onColorSchemeClick = { showColorSchemeScreen = true }
                )
            
            // Date range
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Monday
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { openCalendarApp(uiState.currentWeek.startDate) }
                        .padding(4.dp)
                ) {
                    Text(
                        text = "Mo",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.dayOfMonth.toString(),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.month.name.take(3),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                
                // Tuesday
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { openCalendarApp(uiState.currentWeek.startDate.plusDays(1)) }
                        .padding(4.dp)
                ) {
                    Text(
                        text = "Tu",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(1).dayOfMonth.toString(),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(1).month.name.take(3),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                
                // Wednesday
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { openCalendarApp(uiState.currentWeek.startDate.plusDays(2)) }
                        .padding(4.dp)
                ) {
                    Text(
                        text = "We",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(2).dayOfMonth.toString(),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(2).month.name.take(3),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                
                // Thursday
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { openCalendarApp(uiState.currentWeek.startDate.plusDays(3)) }
                        .padding(4.dp)
                ) {
                    Text(
                        text = "Thu",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(3).dayOfMonth.toString(),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(3).month.name.take(3),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                
                // Friday
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { openCalendarApp(uiState.currentWeek.startDate.plusDays(4)) }
                        .padding(4.dp)
                ) {
                    Text(
                        text = "Fr",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(4).dayOfMonth.toString(),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(4).month.name.take(3),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                
                // Saturday
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { openCalendarApp(uiState.currentWeek.startDate.plusDays(5)) }
                        .padding(4.dp)
                ) {
                    Text(
                        text = "Sa",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(5).dayOfMonth.toString(),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(5).month.name.take(3),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                
                // Sunday
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { openCalendarApp(uiState.currentWeek.endDate) }
                        .padding(4.dp)
                ) {
                    Text(
                        text = "Su",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = uiState.currentWeek.endDate.dayOfMonth.toString(),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = uiState.currentWeek.endDate.month.name.take(3),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            }
            
            // Notes area - now takes remaining space and scrolls properly
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f) // Take remaining space
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(top = 4.dp, bottom = 40.dp)
            ) {
                val filteredNotes = if (uiState.hideClosedNotes) {
                    uiState.notes.filter { note ->
                        note.status != s.nils.weeklynotes.data.NoteStatus.DONE &&
                        note.status != s.nils.weeklynotes.data.NoteStatus.CANCELLED &&
                        note.status != s.nils.weeklynotes.data.NoteStatus.MOVED
                    }
                } else {
                    uiState.notes
                }
                
                items(filteredNotes) { note ->
                    val noteIndex = filteredNotes.indexOf(note)
                    NoteItem(
                        note = note,
                        onContentChange = { content -> viewModel.updateNoteContent(note.id, content) },
                        onStatusClick = { viewModel.cycleNoteStatus(note.id) },
                        onStatusChange = { status -> viewModel.changeNoteStatus(note.id, status) },
                        onMoveToNextWeek = { viewModel.moveNoteToNextWeek(note.id) },
                        onMoveToTop = { viewModel.moveNoteToTop(note.id) },
                        onMoveUp = { viewModel.moveNoteUp(note.id) },
                        onMoveDown = { viewModel.moveNoteDown(note.id) },
                        onMoveToBottom = { viewModel.moveNoteToBottom(note.id) },
                        onDelete = { viewModel.deleteNote(note.id) },
                        isNewNote = note == uiState.notes.lastOrNull() && note.content.isEmpty(),
                        isFirst = noteIndex == 0,
                        isLast = noteIndex == filteredNotes.size - 1
                    )
                }
            }
        }
        
        // Floating action button
        FloatingActionButton(
            onClick = { viewModel.addNote() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 64.dp, end = 16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add note",
                modifier = Modifier.size(24.dp)
            )
        }
        
        // Import warning dialog
        if (uiState.showImportWarning) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissImportWarning() },
                title = { Text("Import Notes") },
                text = { Text("This will delete all existing notes and replace them with the imported data. Are you sure you want to continue?") },
                confirmButton = {
                    TextButton(onClick = { viewModel.confirmImport() }) {
                        Text("Import")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissImportWarning() }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyNotesScreenPreview() {
    WeeklyNotesTheme {
        // Note: This preview won't work properly without a real ViewModel
        // In a real app, this would be provided by the MainActivity
        Text("WeeklyNotesScreen Preview - ViewModel required")
    }
}