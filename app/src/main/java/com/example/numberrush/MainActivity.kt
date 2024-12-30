package com.example.numberrush

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.numberrush.ui.theme.NumberRushTheme
import com.example.numberrush.ui.theme.model.AppViewModel
import com.example.numberrush.ui.theme.model.GameTopBar
import com.example.numberrush.ui.theme.model.TheGameApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val gameViewModel: AppViewModel = viewModel()
            val gameUiState by gameViewModel.uiState.collectAsState()
            NumberRushTheme(darkTheme=gameViewModel.darkMode){
                Scaffold(
                    topBar = {
                        GameTopBar(changed=gameViewModel.darkMode, onClick={gameViewModel.darkMode=!gameViewModel.darkMode})
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .padding(innerPadding)
                            .wrapContentSize()
                            .fillMaxSize()
                    ) {
                        TheGameApp(Modifier, gameUiState, gameViewModel)
                    }
                }
            }
        }
    }
}

