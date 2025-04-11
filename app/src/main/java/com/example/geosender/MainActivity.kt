package com.example.geosender

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.geosender.ui.theme.GeoSenderTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeoSenderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Hello()
                }
            }
        }
    }
}

@Composable
fun Hello(){
    Text(text = "Hello",
        modifier = Modifier.fillMaxSize(),
        textAlign = TextAlign.Center)
}