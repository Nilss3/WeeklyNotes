package s.nils.weeklynotes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import s.nils.weeklynotes.ui.WeeklyNotesScreen
import s.nils.weeklynotes.ui.WeeklyNotesViewModel
import s.nils.weeklynotes.ui.theme.WeeklyNotesTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: WeeklyNotesViewModel
    
    private val exportFilePicker = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                viewModel.exportToLocation(uri)
            }
        }
    }
    
    private val importFilePicker = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        android.util.Log.d("WeeklyNotes", "Import file picker result: ${result.resultCode}")
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                android.util.Log.d("WeeklyNotes", "Import file picker URI: $uri")
                viewModel.importFromFile(uri)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Create ViewModel once
        viewModel = WeeklyNotesViewModel(application)
        
        setContent {
            WeeklyNotesTheme {
                WeeklyNotesScreen(
                    viewModel = viewModel,
                    onExportNotes = { launchExportFilePicker() },
                    onImportNotes = { launchImportFilePicker() }
                )
            }
        }
    }
    
    private fun launchExportFilePicker() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
            putExtra(Intent.EXTRA_TITLE, "weekly_notes_export.json")
        }
        exportFilePicker.launch(intent)
    }
    
    private fun launchImportFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
        }
        importFilePicker.launch(intent)
    }
}