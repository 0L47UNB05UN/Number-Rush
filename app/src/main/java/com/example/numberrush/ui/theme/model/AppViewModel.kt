package com.example.numberrush.ui.theme.model


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableIntStateOf
import com.example.numberrush.ui.theme.data.Questions
import com.example.numberrush.ui.theme.data.SCORE_INCRE
import com.example.numberrush.ui.theme.data.START_TIME
import com.example.numberrush.ui.theme.data.TIME_DECRE
import com.example.numberrush.ui.theme.data.TIME_INCRE
import com.example.numberrush.ui.theme.data.questions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update




class AppViewModel: ViewModel(){
    private val _uiState = MutableStateFlow(AppUiState(getNewQuestion()))
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()
    var userAns by mutableStateOf("")
    var timeLeft by mutableIntStateOf(START_TIME)
    var gameStarted by mutableStateOf(false)
    var highScore by mutableIntStateOf(0)
    var darkMode by mutableStateOf(false)


    init{
        resetGame()
    }


    fun gameOver() {
        _uiState.update{ currentState ->
            currentState.copy(
                gameOver = true
            )
        }
        highScore = _uiState.value.score
    }

    private fun getNewQuestion(): Questions{
        return questions.random()
    }


    fun resetGame(){
        userAns = ""
        _uiState.value = AppUiState(getNewQuestion())
        timeLeft = START_TIME
    }


    fun isUserCorrect(): Boolean{
         return _uiState.value.currentQuestion.answer == userAns
    }


    fun skipQuestion(){
        _uiState.update { currentState ->
            currentState.copy(
                currentQuestion = getNewQuestion()
            )
        }
        userAns = ""
    }


    fun setUserAnswer(answer: String){
        userAns = answer
    }


    fun checkUserAnswer(){
        if (userAns.isNotEmpty()) {
            if (isUserCorrect()) {
                timeLeft += TIME_INCRE
                _uiState.update { currentState ->
                    currentState.copy(
                        score = _uiState.value.score.plus(SCORE_INCRE),
                        currentQuestion = getNewQuestion()
                    )
                }
            } else {
                timeLeft -= TIME_DECRE
                _uiState.update { currentState ->
                    currentState.copy(
                        isWrong = true,
                        currentQuestion = getNewQuestion()
                    )
                }
            }
            userAns = ""
        }
    }
}