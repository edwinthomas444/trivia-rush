package com.tutorial.quizapp

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import kotlin.random.Random


@Composable
fun QuizScreen(navigateToSecondScreen:(Int)->Unit){
    Box(modifier = Modifier.fillMaxSize()) {

        // apply background theme
        Image(
            painter = painterResource(id = R.drawable.entry_theme),
            contentDescription = "Entry Screen App Theme",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )



        var questionIndex by remember { mutableIntStateOf(0) }
        var displayQuestion by remember { mutableStateOf(questions[0]) }
        var score by remember { mutableStateOf(0) }

        // display below composable as long as questionj index within range
        if (questionIndex < answers.size){
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)) {

                // add first surface for the question card
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .size(width = 350.dp, height = 150.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxSize()
                        .padding(14.dp)
                ) {
                    Text(
                        text = "$displayQuestion",
                        modifier = Modifier
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                    )
                }
                // update question on question card
                displayQuestion = questions[questionIndex]
                // add second stackable surface for the options
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.TopCenter
                ) {
                    // number of stackable cards is equal to the
                    // number of remaining questions (total-current)
                    for (zIndex in (0..<answers.size-questionIndex)){
                        // the elevation is computed based on i
                        val yOffset = answers.size-questionIndex-zIndex-1
                        val elevation = zIndex*50.dp
                        StackableCard(
                            qIndex = questionIndex,
                            text = questions[zIndex],
                            yOffset = yOffset.toFloat(),
                            onClick = { qIndex ->
                                questionIndex = qIndex
                            },
                            elevate = elevation
                        )
                    }
                }
            }
        }
        else{
            // navigate to the start screen
            score = Random.nextInt(1, 5)
            navigateToSecondScreen(score)
        }
    }
}

@Composable
fun StackableCard(
    qIndex: Int,
    text: String,
    yOffset: Float,
    elevate: Dp,
    onClick: (Int) -> Unit
) {
    val cardHeight = 500.dp
    val cardWidth = 350.dp
    val cardSpacing = 16.dp


    Surface(
        modifier = Modifier
            .clickable(onClick = { onClick(qIndex + 1) })
            .offset(y = yOffset * cardSpacing)
            .width(cardWidth)
            .height(cardHeight)
            .padding(16.dp),
        shadowElevation = elevate,
        shape = RoundedCornerShape(20.dp),
        color = Color.LightGray
    ) {
        Column{
            // set up a column of 3 multiple choices
            for (j in 0..2){
                // select the display array
                val displayChoiceList = when(j){
                    0-> choicesA
                    1-> choicesB
                    2-> choicesC
                    else -> choicesA
                }

                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .size(width = 250.dp, height = 70.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxSize()
                        .padding(6.dp)
                ){
                    Text("${displayChoiceList[qIndex]}")
                }
            }
        }

    }


}

