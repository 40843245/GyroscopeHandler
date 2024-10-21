package com.jay30.gyroscopehandler

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.jay30.gyroscopehandler.ui.theme.GyroscopeHandlerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GyroscopeHandlerTheme {
                val TAG = "tag_MainActivity"
                Log.d(TAG,"onCreate method in MainActivity class was called.")
                Main()
            }
        }
    }
}

@Composable
fun Main(){
    val TAG = "tag_Main"
    Log.d(TAG,"Main function was called.")
    MainScreen()
}