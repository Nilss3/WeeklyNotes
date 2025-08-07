package s.nils.weeklynotes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import s.nils.weeklynotes.data.Note
import androidx.compose.ui.tooling.preview.Preview
import s.nils.weeklynotes.ui.theme.WeeklyNotesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyNotesScreen(
    viewModel: WeeklyNotesViewModel,
    onExportNotes: () -> Unit = { viewModel.exportNotes() },
    onImportNotes: () -> Unit = { viewModel.importNotes() }
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    
    // Scroll to bottom when new notes are added
    LaunchedEffect(uiState.notes.size) {
        if (uiState.notes.isNotEmpty()) {
            // Add a longer delay to ensure the item is rendered before scrolling
            kotlinx.coroutines.delay(300)
            listState.animateScrollToItem(uiState.notes.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
                hideClosedNotes = uiState.hideClosedNotes
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Mo",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.dayOfMonth.toString(),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.month.name.take(3),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                
                // Tuesday
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tu",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(1).dayOfMonth.toString(),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(1).month.name.take(3),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                
                // Wednesday
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "We",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(2).dayOfMonth.toString(),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(2).month.name.take(3),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                
                // Thursday
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Thu",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(3).dayOfMonth.toString(),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(3).month.name.take(3),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                
                // Friday
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Fr",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(4).dayOfMonth.toString(),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(4).month.name.take(3),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                
                // Saturday
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sa",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(5).dayOfMonth.toString(),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = uiState.currentWeek.startDate.plusDays(5).month.name.take(3),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                
                // Sunday
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Su",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = uiState.currentWeek.endDate.dayOfMonth.toString(),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = uiState.currentWeek.endDate.month.name.take(3),
                        style = TextStyle(
                            color = Color.Black,
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
                    NoteItem(
                        note = note,
                        onContentChange = { content -> viewModel.updateNoteContent(note.id, content) },
                        onStatusClick = { viewModel.cycleNoteStatus(note.id) },
                        isNewNote = note == uiState.notes.lastOrNull() && note.content.isEmpty()
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
            containerColor = Color.Black,
            contentColor = Color.White
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

@Composable
fun WeeklyHeader(
    week: s.nils.weeklynotes.data.Week,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
    onExportNotes: () -> Unit,
    onImportNotes: () -> Unit,
    onToggleClosedNotes: () -> Unit,
    hideClosedNotes: Boolean
) {
    var showMenu by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
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
                    .border(2.dp, Color.Black, shape = MaterialTheme.shapes.small)
                    .clip(MaterialTheme.shapes.small),
                color = Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.Black,
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
                        .border(2.dp, Color.Black, shape = MaterialTheme.shapes.small)
                        .clip(MaterialTheme.shapes.small),
                    color = Color.White
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous week",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                // Week title
                Text(
                    text = week.title,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                
                // Next week button
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onNextWeek() }
                        .border(2.dp, Color.Black, shape = MaterialTheme.shapes.small)
                        .clip(MaterialTheme.shapes.small),
                    color = Color.White
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next week",
                            tint = Color.Black,
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
            modifier = Modifier.background(Color.White)
        ) {
            DropdownMenuItem(
                text = { 
                    Text(
                        if (hideClosedNotes) "Unhide 'closed' notes" else "Hide 'closed' notes",
                        color = Color.Black
                    ) 
                },
                onClick = {
                    onToggleClosedNotes()
                    showMenu = false
                }
            )
            
            // Divider
            androidx.compose.material3.Divider(color = Color.Gray, thickness = 1.dp)
            
            DropdownMenuItem(
                text = { Text("Import notes...", color = Color.Black) },
                onClick = {
                    onImportNotes()
                    showMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text("Export notes...", color = Color.Black) },
                onClick = {
                    onExportNotes()
                    showMenu = false
                }
            )
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onContentChange: (String) -> Unit,
    onStatusClick: () -> Unit,
    isNewNote: Boolean
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(note.content)) }
    val focusRequester = remember { FocusRequester() }
    
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
            .border(2.dp, Color.Black, shape = MaterialTheme.shapes.small)
            .background(Color.White, shape = MaterialTheme.shapes.small)
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Status button
        Surface(
            modifier = Modifier
                .size(32.dp)
                .clickable { onStatusClick() }
                .border(2.dp, Color.Black, shape = MaterialTheme.shapes.small)
                .clip(MaterialTheme.shapes.small),
            color = Color.White
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = note.getStatusSymbol(),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
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
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            singleLine = false,
            maxLines = Int.MAX_VALUE // Allow unlimited lines
        )
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