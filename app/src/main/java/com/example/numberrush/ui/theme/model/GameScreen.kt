package com.example.numberrush.ui.theme.model

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.numberrush.R
import kotlinx.coroutines.delay


@Composable
fun Option(text: String, option: String, gameViewModel: AppViewModel, modifier: Modifier = Modifier ){
    Row(
        horizontalArrangement = Arrangement.Absolute.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(end=8.dp)
    ) {
        RadioButton(
            selected = (gameViewModel.userAns==option),
            onClick = {
                gameViewModel.setUserAnswer(option)
            }
        )
        Text(
            text = text
        )
    }
}


@Composable
fun Options(options: List<String>, gameViewModel: AppViewModel, modifier: Modifier=Modifier){
    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = modifier
            .padding(top = 2.dp)
            .wrapContentSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column {
                Option("A. ${options[0]}", options[0], gameViewModel)
                Option("C. ${options[2]}", options[2], gameViewModel)
            }
            Column {
                Option("B. ${options[1]}", options[1], gameViewModel)
                Option("D. ${options[3]}", options[3], gameViewModel)
            }
        }
    }
}


@Composable
fun ScoreAndTimer(gameUiState: AppUiState, timeLeft: Int, modifier: Modifier = Modifier){
    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = modifier
            .padding(bottom = 2.dp)
            .wrapContentSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text= stringResource(id=R.string.score).plus(gameUiState.score)
                )
            Text(
                text= stringResource(id=R.string.time_left).plus(timeLeft)
            )
        }
    }
}


@Composable
fun GameButtons(gameViewModel: AppViewModel, modifier: Modifier = Modifier){
    Button(
        onClick = { gameViewModel.checkUserAnswer() }, modifier.padding(top=8.dp, bottom=8.dp)
    ){
        Text(text="Submit")
    }
    Button(
        onClick = { gameViewModel.skipQuestion() },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
    ){
        Text(text=stringResource(R.string.skip))
    }
}


@Composable
fun TheGameApp(
    modifier: Modifier = Modifier,gameUiState: AppUiState, gameViewModel: AppViewModel = viewModel()
){
    if (!gameViewModel.gameStarted){
        AlertDialog(
            title = {Text(stringResource(R.string.app_name))},
            text = { Text(stringResource(R.string.intro)) },
            onDismissRequest = {  },
            confirmButton = {
                Button(
                    onClick = { gameViewModel.gameStarted = true }
                ){
                    Text(stringResource(R.string.start_game))
                }
            }
        )
    }else {
        LaunchedEffect(key1 = gameViewModel.timeLeft) {
            if (gameViewModel.timeLeft > 0) {
                delay(1000L)
                gameViewModel.timeLeft--
            } else {
                gameViewModel.gameOver()
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            ScoreAndTimer(gameUiState, gameViewModel.timeLeft, modifier)
            Card(
                elevation = CardDefaults.cardElevation(15.dp),
                modifier = modifier.wrapContentSize()//.padding(start=8.dp, end=8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .background(Color(0x3F00FF00))
                ){
                    Text(
                        text = gameUiState.currentQuestion.question,
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp,
                        modifier = modifier.padding(32.dp)
                    )
                }
            }
            Options(gameUiState.currentQuestion.options, gameViewModel)
            GameButtons(gameViewModel)
            if (gameUiState.gameOver) {
                GameOverDialog(
                    gameUiState.score,
                    gameViewModel,
                    playAgain = {
                        gameViewModel.resetGame()
                    }
                )
            }
        }
    }
}


@Composable
private fun GameOverDialog(
    score: Int, gameViewModel: AppViewModel, playAgain: () -> Unit, modifier: Modifier = Modifier
){
    val active = LocalContext.current as Activity
    AlertDialog(
        onDismissRequest = { },
        title = { Text(stringResource(R.string.game_over)) },
        text = {
            Row {
                Text(
                    stringResource(R.string.your_score).plus(score),
                    modifier.padding(end = 16.dp)
                )
                Text(stringResource(R.string.high_score).plus(gameViewModel.highScore))
            }
        },
        modifier = modifier,
        dismissButton = {
            Button(
                onClick={ active.finish() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ){
                Text(stringResource(R.string.exit))
            }
        },
        confirmButton = {
            Button(
                onClick= playAgain
            ){
                Text(stringResource(R.string.play_again))
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(modifier: Modifier = Modifier, changed: Boolean, onClick: ()->Unit){
    CenterAlignedTopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                    )
                    IconButton(
                        onClick = onClick,
                    ) {
                        Image(
                            painter = painterResource(if (changed) R.drawable.sun else R.drawable.moon),
                            modifier = modifier.size(25.dp),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    )
}