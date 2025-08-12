package s.nils.weeklynotes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import s.nils.weeklynotes.ui.theme.ColorScheme

@Composable
fun ColorSchemeScreen(
    currentColorScheme: ColorScheme,
    onColorSchemeSelected: (ColorScheme) -> Unit,
    onBackPressed: () -> Unit
) {
    // Use consistent colors for the selection screen regardless of current theme
    // This makes it clearer that we're showing the stored preference, not the current theme
    val backgroundColor = Color.White
    val textColor = Color.Black
    val borderColor = Color.Black
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
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
                    tint = textColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = "Color Scheme",
                style = TextStyle(
                    color = textColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Color scheme options
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ColorSchemeOption(
                title = "Always use black on white (light/e-ink)",
                isSelected = currentColorScheme == ColorScheme.LIGHT,
                backgroundColor = backgroundColor,
                textColor = textColor,
                borderColor = borderColor,
                onClick = { onColorSchemeSelected(ColorScheme.LIGHT) }
            )
            
            ColorSchemeOption(
                title = "Always use white on black (dark/oled)",
                isSelected = currentColorScheme == ColorScheme.DARK,
                backgroundColor = backgroundColor,
                textColor = textColor,
                borderColor = borderColor,
                onClick = { onColorSchemeSelected(ColorScheme.DARK) }
            )
            
            ColorSchemeOption(
                title = "Change automatically between light and dark",
                isSelected = currentColorScheme == ColorScheme.SYSTEM,
                backgroundColor = backgroundColor,
                textColor = textColor,
                borderColor = borderColor,
                onClick = { onColorSchemeSelected(ColorScheme.SYSTEM) }
            )
            
            ColorSchemeOption(
                title = "Custom (pick...)",
                isSelected = currentColorScheme == ColorScheme.CUSTOM,
                backgroundColor = backgroundColor,
                textColor = textColor,
                borderColor = borderColor,
                onClick = { onColorSchemeSelected(ColorScheme.CUSTOM) }
            )
        }
    }
}

@Composable
private fun ColorSchemeOption(
    title: String,
    isSelected: Boolean,
    backgroundColor: Color,
    textColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = textColor,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Spacer(modifier = Modifier.width(20.dp))
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = title,
            style = TextStyle(
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}
