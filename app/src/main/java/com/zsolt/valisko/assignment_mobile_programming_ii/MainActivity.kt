package com.zsolt.valisko.assignment_mobile_programming_ii

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.zsolt.valisko.assignment_mobile_programming_ii.ui.theme.AssignmentmobileprogrammingiiTheme

class MainActivity : ComponentActivity() {
    // Task 4: ViewModel class instance
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Task 3: Attach the lifecycle observer
        lifecycle.addObserver(MyLifecycleObserver())
        
        enableEdgeToEdge()
        setContent {
            AssignmentmobileprogrammingiiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AssignmentScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AssignmentScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    // Task 4: Implementation using an Observer (Data Binding)
    // Local state to bridge LiveData with Compose UI
    var liveDisplayText by remember { mutableStateOf("") }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = Observer<String> { newText ->
            liveDisplayText = newText
        }
        viewModel.text.observe(lifecycleOwner, observer)
        onDispose {
            viewModel.text.removeObserver(observer)
        }
    }

    // Task 2: Get Activity context to call runOnUiThread
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Task 1, 4 & 5: TextView showing text from LiveData
        Text(text = liveDisplayText)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Task 1, 4 & 5: EditText (TextField)
        TextField(
            value = liveDisplayText,
            onValueChange = { 
                // Task 4: typed text immediately appears in TextView via LiveData
                viewModel.onTextChanged(it)
            },
            label = { Text("Enter text") }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Task 1, 2, 5: Button with label "Send"
        Button(onClick = {
            // Task 2: Start a background thread (4 seconds wait)
            Thread {
                try {
                    // Task 2 Parameter: 4 seconds
                    Thread.sleep(4000)
                    // Task 2 Requirement: UI updates must use runOnUiThread
                    (context as? Activity)?.runOnUiThread {
                        viewModel.setFinalText()
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }.start()
        }) {
            Text("Send")
        }
    }
}
