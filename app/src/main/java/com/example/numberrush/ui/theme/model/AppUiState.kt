package com.example.numberrush.ui.theme.model

import com.example.numberrush.ui.theme.data.Questions

data class AppUiState(
    val currentQuestion: Questions,
    val score: Int = 0,
    val isWrong: Boolean = false,
    val gameOver: Boolean = false
)
