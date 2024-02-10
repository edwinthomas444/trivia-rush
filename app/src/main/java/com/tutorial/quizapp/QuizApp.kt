package com.tutorial.quizapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController




@Composable
fun QuizApp(name1: String, name2: String, navController: NavHostController){

    NavHost(navController = navController, startDestination = "firstscreen"){
        composable(route = "firstscreen"){
            val score = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("score") ?: 0
            EntryScreen(score, name1 = name1, name2 = name2){
                // pass the value it got it from second screen or if first time, default to 0
                navController.currentBackStackEntry?.savedStateHandle?.set("score",it)
                navController.navigate("secondscreen")
            }
        }

        composable(route = "secondscreen"){
            //get score from third screen, default score to 0
            val score = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("score") ?: 0
            ScoreScreen(score, {
                // navigate to quiz screen implementation
                // no need to pass the score in this case to the stack and the quiz screen generates it
                navController.navigate("thirdscreen")
            },{
                // navigate to entry screen implementation, and set the current screens score to score
                navController.currentBackStackEntry?.savedStateHandle?.set("score",it)
                navController.navigate("firstscreen")
            })
        }

        composable(route = "thirdscreen"){
            // save the score from the third screen to pass to the second screen
            // for each new game get random 4 indices
            val indicesList = getRandomIndices(k=4,n=20)

            QuizScreen({
                // it is the score that is passed from the 2nd screen when it call
                // this function
                navController.currentBackStackEntry?.savedStateHandle?.set("score",it)
                //navigate to score screen
                navController.navigate("secondscreen")
            }, randomIndices = indicesList)
        }
    }

}

