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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        // maintain a state for the selected answers
        // default selected options is 0
        var selectedOption by remember { mutableStateOf<List<Int>>(mutableListOf(0,0,0,0)) }

        // display below composable as long as question index within range
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "$displayQuestion",
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        )
                    }
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
                            yOffset = yOffset.toFloat(),
                            onClick = { qIndex ->
                                questionIndex = qIndex
                            },
                            onClickAnswer = { answer->
                                // store the option
                                val currentList = selectedOption.toMutableList()
                                currentList[questionIndex] = answer
                                selectedOption = currentList
                            },
                            elevate = elevation
                        )
                    }
                    // test the value of the options selected
                    // Text("${selectedOption.joinToString(", ")}")
                }
            }
        }
        else{
            // navigate to the start screen
            score = 0
            // calculate the score based on the user selected options for all questions
            for (k in answers.indices){
                score+= if (selectedOption[k] == answers[k]) 1 else 0
            }
            navigateToSecondScreen(score)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StackableCard(
    qIndex: Int,
    yOffset: Float,
    elevate: Dp,
    onClick: (Int) -> Unit,
    onClickAnswer: (Int) -> Unit
) {
    val cardHeight = 500.dp
    val cardWidth = 350.dp
    val cardSpacing = 16.dp


    Surface(
        modifier = Modifier
            .offset(y = yOffset * cardSpacing)
            .width(cardWidth)
            .height(cardHeight)
            .padding(16.dp),
        shadowElevation = elevate,
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {

        var selectedOption by remember {
            mutableStateOf(0)
        }


        var selectedCardBackgroundColor by remember {
            mutableStateOf<List<Color>>(mutableListOf(Color.LightGray, Color.LightGray, Color.LightGray))
        }

        var selectedCardTextColor by remember {
            mutableStateOf<List<Color>>(mutableListOf(Color.Black, Color.Black, Color.Black))
        }

        // add a column layout for the options and the next button
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){

            // set up a column of 3 multiple choices
            for (j in 0..2){
                // select the display array
                val displayChoiceList = when(j){
                    0-> choicesA
                    1-> choicesB
                    2-> choicesC
                    else -> choicesA
                }
                val option = when(j){
                    0-> "A."
                    1-> "B."
                    2-> "C."
                    else -> "none"
                }
                // Each individual option that the user can click on
                Card(
                    onClick = {
                        // change colour of previous selection and current selection

                        fun changeColors(prevIndex: Int, selected: Boolean){
                            val currBackList = selectedCardBackgroundColor.toMutableList()
                            val currTxtList = selectedCardTextColor.toMutableList()
                            if (!selected){
                                currBackList[prevIndex] = Color.LightGray
                                currTxtList[prevIndex]  = Color.Black
                            }else{
                                currBackList[prevIndex] = (Color(0xFF66BB6A))
                                currTxtList[prevIndex]  = Color.White
                            }
                            selectedCardBackgroundColor = currBackList
                            selectedCardTextColor = currTxtList
                        }
                        // previous
                        val previousSelectionIndex = if (selectedOption == 0) 0 else selectedOption-1
                        changeColors(previousSelectionIndex, false)

                        selectedOption = j+1

                        // current
                        changeColors(selectedOption-1, true)
                    },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .size(width = 275.dp, height = 70.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxSize()
                        .padding(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = selectedCardBackgroundColor[j], //Card background color
                        contentColor = selectedCardTextColor[j]  //Card content color,e.g.text
                    )
                ){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text("$option ${displayChoiceList[qIndex]}",
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontStyle = FontStyle.Italic
                            ))
                    }
                }
            }


            // put button in column scope
            // button for going to next screen
            ElevatedButton(onClick = {
                // update the user selection option
                onClickAnswer(selectedOption)
                // update the question index
                onClick(qIndex + 1)
            },modifier = Modifier
                .size(width = 140.dp, height = 80.dp)
                .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE57373),
                    contentColor = Color.White
                )
            ) {
                // test text to reach to button click
                // use "Next $selectedOption" instead
                Text("Next",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                )
            }
//            Text("${selectedCardBackgroundColor[0].toString()}")
//            Text("${selectedCardTextColor.joinToString(" , ")}")
        }
    }


}

