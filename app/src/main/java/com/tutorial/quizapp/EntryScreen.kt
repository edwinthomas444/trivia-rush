package com.tutorial.quizapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EntryScreen(score: Int, name1: String, name2: String, navigateToScoreScreen:(Int)->Unit){
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.entry_theme),
            contentDescription = "Entry Screen App Theme",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            // add a logo of UOttawa
            Image(
                painter = painterResource(id = R.drawable.uottawa_logo_stylized),
                contentScale = ContentScale.Crop,
                contentDescription = "UOttawa Logo",
                modifier = Modifier.size(250.dp)
                    .clip(RoundedCornerShape(16.dp))
//                    .border(2.dp, Color.Gray, CircleShape)
            )
            // add spacing element between logo and text
            Spacer(modifier = Modifier.height(16.dp))
            // add text
            Text(
                text = "Welcome to $name1's & $name2's Game",
                modifier = Modifier.width(200.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontFamily = FontFamily.SansSerif
                ),
                textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(16.dp))
            // add a button to go to next screen
            ElevatedButton(
                onClick = {
                    // navigate to score screen
                    navigateToScoreScreen(score)
                }
                ) {
                Text(text = "OK",
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