package com.tutorial.quizapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScoreScreen(
    score: Int,
    navigateToQuizScreen:()->Unit,
    navigateToEntryScreen: (Int)->Unit){

    var showScore by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.entry_theme),
            contentDescription = "Score Screen",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

        // column layout with three buttons
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){


            ElevatedButton(onClick = {
                // navigate to quiz game
                navigateToQuizScreen()
            },modifier = Modifier.size(width = 280.dp, height = 100.dp).padding(16.dp)) {
                Text("Play New Game",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF44336),
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif
                    ))
            }
            ElevatedButton(onClick = {
                // display the last game's score
                // use score passed down
                showScore = !showScore
            },modifier = Modifier.size(width = 280.dp, height = 100.dp).padding(16.dp)) {
                AnimatedVisibility(visible = !showScore){
                    Text("Last Game Score",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF44336),
                            fontSize = 24.sp,
                            fontFamily = FontFamily.SansSerif
                        ))
                }
                AnimatedVisibility(visible = showScore) {
                    Text("Score: $score pts",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF44336),
                            fontSize = 24.sp,
                            fontFamily = FontFamily.SansSerif
                        ))
                }
            }
            ElevatedButton(onClick = {
                // go back to the entry screen
                navigateToEntryScreen(score)
            },modifier = Modifier.size(width = 280.dp, height = 100.dp).padding(16.dp)) {
                Text("Back",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF44336),
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif
                    ))
            }
        }
    }
}