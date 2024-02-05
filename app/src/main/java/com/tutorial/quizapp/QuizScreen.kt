package com.tutorial.quizapp

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.derivedStateOf
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
    Box(modifier = Modifier
        .fillMaxSize()) {

        // apply background theme
        Image(
            painter = painterResource(id = R.drawable.entry_theme),
            contentDescription = "Entry Screen App Theme",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

        var questionIndex by remember { mutableIntStateOf(0) }
        // maintain a derived variable that stores the question index for answer reviews
        var questionIndexReview by remember { mutableStateOf(-1) }

        var displayQuestion by remember { mutableStateOf(questions[0]) }
        var displayQuestionReview by remember { mutableStateOf(questions[0]) }


        var score by remember { mutableStateOf(0) }
        // maintain a state for the selected answers
        // default selected options is 0
        var selectedOption by remember { mutableStateOf<List<Int>>(mutableListOf(0,0,0,0)) }

        // a variable that stores whethe screen is in quiz mode or answer display mode
        val quizMode by remember { derivedStateOf {
            questionIndex < answers.size
        }}

        // add second stackable surface for the options
        val endIndex by remember {
            derivedStateOf { if (quizMode) questionIndex else questionIndexReview }
        }

        // display below composable as long as question index within range
        if (questionIndex < answers.size || questionIndexReview < answers.size){
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
                        val dispText = if (quizMode) displayQuestion else displayQuestionReview
                        Text(text = "$dispText",
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
                if (quizMode){
                    displayQuestion = questions[questionIndex]
                }else{
                    displayQuestionReview = questions[questionIndexReview]
                }


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.TopCenter
                ) {
                    // number of stackable cards is equal to the
                    // number of remaining questions (total-current)
                    // as questionIndex changes, the stacked effect is created

                    // in compose the element displayed last overlaps the current
                    // we want the first question to be displayed on top
                    // so render bottom up
                    for (answerCardIndex in answers.indices.reversed()){
                        this@Column.AnimatedVisibility(visible = answerCardIndex >= endIndex){
                            // the elevation is computed based on i
                            val yOffset = answerCardIndex + 1
                            val elevation = (answers.size-yOffset)*50.dp

                            if (quizMode){
                                StackableCard(
                                    qIndex = answerCardIndex,
                                    activeqIndex = questionIndex,
                                    yOffset = yOffset.toFloat(),
                                    onClick = { qIndex ->
                                        questionIndex = qIndex
                                        // update the review index so that
                                        // it starts from 0 after quiz over
                                        questionIndexReview = questionIndex - answers.size
                                    },
                                    onClickAnswer = { answer->
                                        // store the option
                                        val currentList = selectedOption.toMutableList()
                                        currentList[questionIndex] = answer
                                        selectedOption = currentList

                                        // also update score
                                        score+= if (selectedOption[endIndex] == answers[endIndex]) 1 else 0
                                    },
                                    elevate = elevation
                                )

                            }else{
                                StackableCardAnswers(
                                    qIndex = answerCardIndex,
                                    activeqIndex = questionIndexReview,
                                    yOffset = yOffset.toFloat(),
                                    onClick = { qIndex ->
                                        questionIndexReview = qIndex
                                    },
                                    elevate = elevation,
                                    selectedOptions = selectedOption,
                                    score = score,
                                )
                            }
                        }
                    }
                }
            }
        }
        else{
//            // navigate to the start screen
//            score = 0
//            // calculate the score based on the user selected options for all questions
//            for (k in answers.indices){
//                score+= if (selectedOption[k] == answers[k]) 1 else 0
//            }
            navigateToSecondScreen(score)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StackableCard(
    qIndex: Int,
    activeqIndex: Int,
    yOffset: Float,
    elevate: Dp,
    onClick: (Int) -> Unit,
    onClickAnswer: (Int) -> Unit
) {
    val cardHeight = 500.dp  // 500.dp
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

        var buttonText by remember {
            mutableStateOf( "Next" )
        }
        var buttonSize by remember {
            mutableStateOf(140.dp)
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
                                currBackList[prevIndex] = Color(0xFF42A5F5) //(Color(0xFF66BB6A))
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

                        // update the button text and width here
                        // variable to store whether the user is in the final question
                        // edit button text based on whether final question or not
                        buttonText = if (qIndex == questions.size - 1) "Review Answers" else "Next"
                        buttonSize = if (qIndex == questions.size - 1) 280.dp else 140.dp
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
                onClick(activeqIndex + 1)
            },modifier = Modifier
                .size(width = buttonSize, height = 80.dp)
                .padding(16.dp)
                .animateContentSize(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE57373),
                    contentColor = Color.White
                )
            ) {
                // test text to reach to button click
                // use "Next $selectedOption" instead
                Text("$buttonText",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                )
            }
//            Text("${selectedCardBackgroundColor[0].toString()}")
//            Text("${selectedCardTextColor.joinToString(" , ")}")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StackableCardAnswers(
    qIndex: Int,
    activeqIndex: Int,
    selectedOptions: List<Int>,
    yOffset: Float,
    elevate: Dp,
    score: Int,
    onClick: (Int) -> Unit
){
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

        var selectedCardBackgroundColor by remember {
            mutableStateOf(listOf(Color.LightGray, Color.LightGray, Color.LightGray))
        }

        var selectedCardTextColor by remember {
            mutableStateOf(listOf(Color.Black, Color.Black, Color.Black))
        }

        // set the background colours of the right and wrong answers based on previous selection
        val correctIndex = answers[qIndex] - 1
        val selectedIndex = selectedOptions[qIndex] - 1
        val currBackList = selectedCardBackgroundColor.toMutableList()
        val currTxtList = selectedCardTextColor.toMutableList()

        currBackList[correctIndex] = Color(0xFF81C784)
        currTxtList[correctIndex]  = Color.White

        if (correctIndex != selectedIndex){
            currBackList[selectedIndex] = Color(0xFFE57373)
            currTxtList[selectedIndex]  = Color.White
        }

        selectedCardBackgroundColor = currBackList
        selectedCardTextColor = currTxtList


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

            // prompt showing user whether answer is correct or wrong
            Card(
                onClick = { /*TODO*/ },
                colors = CardDefaults.cardColors(
                    containerColor = Color.White, //Card background color
                    contentColor = Color.Black  //Card content color,e.g.text
                ))
            {
                if (correctIndex == selectedIndex){
                    Text("Correct Answer !",
                            style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF81C784),
                            fontSize = 24.sp,
                            fontFamily = FontFamily.SansSerif
                        ))
                }else{
                    Text("Wrong Answer",
                            style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE57373),
                            fontSize = 24.sp,
                            fontFamily = FontFamily.SansSerif
                        ))
                }
            }

            // put button in column scope
            // button for going to next screen
            ElevatedButton(onClick = {
                // update the question index
                onClick(activeqIndex + 1)
            },modifier = Modifier
                .size(width = 140.dp, height = 80.dp)
                .padding(16.dp)
                .animateContentSize(),
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
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                )
            }


            // display the score in last screen
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White, //Card background color
                    contentColor = Color.Black  //Card content color,e.g.text
                )
            )
            {
                if (activeqIndex == answers.size - 1){
                    Text("Total Score: $score",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontFamily = FontFamily.SansSerif
                        ))
                }
            }

//            Text("${selectedIndex} ${correctIndex}")
//            Text("${selectedCardBackgroundColor.joinToString(" , ")}")
        }
    }

}
