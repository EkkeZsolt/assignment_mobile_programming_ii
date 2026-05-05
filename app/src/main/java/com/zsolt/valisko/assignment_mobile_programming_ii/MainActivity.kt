package com.zsolt.valisko.assignment_mobile_programming_ii

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zsolt.valisko.assignment_mobile_programming_ii.ui.theme.AssignmentmobileprogrammingiiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Task 3: Attach the lifecycle observer (overriding onResume and onPause)
        lifecycle.addObserver(MyLifecycleObserver())
        
        enableEdgeToEdge()
        setContent {
            AssignmentmobileprogrammingiiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AssignmentScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AssignmentScreen(modifier: Modifier = Modifier) {
    // Task 5: State management using remember and mutableStateOf
    var displayText by remember { mutableStateOf("") }
    var inputText by remember { mutableStateOf("") }
    
    // Handler used to update UI from background thread (Task 2)
    val handler = remember { Handler(Looper.getMainLooper()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Task 1 & 5: TextView (Text) on top
        Text(text = displayText)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Task 1 & 5: EditText (TextField) below it
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter text") }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Task 1 & 5: Button at the bottom with label "Send"
        Button(onClick = {
            // Task 1: Copy EditText content to TextView
            displayText = inputText
            
            // Task 2: Start a background thread
            Thread {
                try {
                    // Task 2 Parameter: Wait for 4 seconds
                    Thread.sleep(4000)
                    
                    // Task 2: Use a handler to set the TextView's text from background task
                    handler.post {
                        displayText = "Process ended."
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
