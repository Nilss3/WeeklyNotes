package s.nils.weeklynotes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun CustomColorScreen(
    currentTextColor: Color,
    currentBackgroundColor: Color,
    onTextColorChanged: (Color) -> Unit,
    onBackgroundColorChanged: (Color) -> Unit,
    onSave: () -> Unit,
    onBackPressed: () -> Unit
) {
    var showTextColorPicker by remember { mutableStateOf(false) }
    var showBackgroundColorPicker by remember { mutableStateOf(false) }
    var editingTextColor by remember { mutableStateOf(currentTextColor) }
    var editingBackgroundColor by remember { mutableStateOf(currentBackgroundColor) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header with back button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = "Custom Colors",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Text and frame color picker
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Text and Frame Color",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Color preview
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(editingTextColor, RoundedCornerShape(8.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Color picker button
                Button(
                    onClick = { showTextColorPicker = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Pick Color")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Background color picker
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Background Color",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Color preview
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(editingBackgroundColor, RoundedCornerShape(8.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Color picker button
                Button(
                    onClick = { showBackgroundColorPicker = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Pick Color")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Preview section
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Preview",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Preview box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(editingBackgroundColor, RoundedCornerShape(8.dp))
                    .border(2.dp, editingTextColor, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Sample Text",
                        style = TextStyle(
                            color = editingTextColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "This is how your app will look",
                        style = TextStyle(
                            color = editingTextColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Save button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    onTextColorChanged(editingTextColor)
                    onBackgroundColorChanged(editingBackgroundColor)
                    onSave()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("Save Colors")
            }
        }
    }
    
    // Text color picker dialog
    if (showTextColorPicker) {
        AdvancedColorPickerDialog(
            initialColor = editingTextColor,
            onColorSelected = { color ->
                editingTextColor = color
                showTextColorPicker = false
            },
            onDismiss = { showTextColorPicker = false }
        )
    }
    
    // Background color picker dialog
    if (showBackgroundColorPicker) {
        AdvancedColorPickerDialog(
            initialColor = editingBackgroundColor,
            onColorSelected = { color ->
                editingBackgroundColor = color
                showBackgroundColorPicker = false
            },
            onDismiss = { showBackgroundColorPicker = false }
        )
    }
}

@Composable
private fun AdvancedColorPickerDialog(
    initialColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    var red by remember { mutableStateOf(initialColor.red) }
    var green by remember { mutableStateOf(initialColor.green) }
    var blue by remember { mutableStateOf(initialColor.blue) }
    
    val currentColor = Color(red, green, blue)
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Pick Color",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Color preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(currentColor, RoundedCornerShape(8.dp))
                        .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Red slider
                Text(
                    text = "Red",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = red,
                    onValueChange = { red = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Red,
                        activeTrackColor = Color.Red,
                        inactiveTrackColor = Color.LightGray
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Green slider
                Text(
                    text = "Green",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = green,
                    onValueChange = { green = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Green,
                        activeTrackColor = Color.Green,
                        inactiveTrackColor = Color.LightGray
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Blue slider
                Text(
                    text = "Blue",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = blue,
                    onValueChange = { blue = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Blue,
                        activeTrackColor = Color.Blue,
                        inactiveTrackColor = Color.LightGray
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Quick color presets
                Text(
                    text = "Quick Colors",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(8),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(80.dp)
                ) {
                    val presetColors = listOf(
                        Color.Black, Color.White, Color.Red, Color.Green, Color.Blue,
                        Color.Yellow, Color.Cyan, Color.Magenta, Color.Gray, Color.DarkGray,
                        Color.LightGray, Color(0xFF8B4513), Color(0xFF006400), Color(0xFF4B0082),
                        Color(0xFF800000), Color(0xFF808000), Color(0xFF008080), Color(0xFF000080),
                        Color(0xFFFF69B4), Color(0xFF00CED1), Color(0xFFFFD700), Color(0xFF32CD32),
                        Color(0xFFFF6347), Color(0xFF9370DB), Color(0xFF20B2AA), Color(0xFFFFA500)
                    )
                    
                    items(presetColors.size) { index ->
                        val color = presetColors[index]
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(color, RoundedCornerShape(16.dp))
                                .border(
                                    width = if (color == currentColor) 3.dp else 1.dp,
                                    color = if (color == currentColor) Color.Blue else Color.Gray,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { 
                                    red = color.red
                                    green = color.green
                                    blue = color.blue
                                }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = { onColorSelected(currentColor) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Select")
                    }
                }
            }
        }
    }
}


