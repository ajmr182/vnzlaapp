package com.ajdev.vnzlaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.ajdev.vnzlaapp.ui.screens.exchangescreen.ExchangeScreen
import com.ajdev.vnzlaapp.ui.screens.exchangescreen.ExchangeViewModel
import com.ajdev.vnzlaapp.ui.theme.VnzlaApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VnzlaApp {
                val viewModel: ExchangeViewModel = hiltViewModel()
                ExchangeScreen(viewModel)
            }
        }
    }
}