package com.example.geosender

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.geosender.ui.NavGraph
import com.example.geosender.ui.theme.GeoSenderTheme
import com.example.geosender.ui.theme.Typography

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeoSenderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    NavGraph()
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
@Composable
fun LocationAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF6200EE),
            primaryContainer = Color(0xFF00BB77),
            onPrimary = Color.White,
            onPrimaryContainer = Color.White,
            secondary = Color(0xFF03DAC6),
            background = Color(0xFFFFFFFF),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF000000)
        ),
        typography = Typography(
            displayLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 57.sp,
                lineHeight = 64.sp,
                letterSpacing = (-0.25).sp
            ),
            // Add other text styles as needed
        ),
        content = content
    )
}