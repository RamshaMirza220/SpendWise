package com.ramsha.spendwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ramsha.spendwise.ui.SpendWiseNavHost
import com.ramsha.spendwise.ui.theme.SpendWiseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpendWiseTheme {
                SpendWiseNavHost()
            }
        }
    }
}
