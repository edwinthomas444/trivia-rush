package com.tutorial.quizapp

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.tutorial.quizapp.ui.theme.QuizAppTheme
import androidx.compose.ui.platform.LocalContext
import android.util.Log

class MainActivity : ComponentActivity() {

    // client that binds to the service
    private lateinit var aService: AudioService
    // variable to store if the service is bound
    private var aBound : Boolean = false

    // anonymous class definition and assignment to an instance of that class
    // the anonymous class overrides Service Connection
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, serviceBinder: IBinder?) {
            // binder is passed to serviceBinder by calling onBind() of Service
            // 'as' casts it
            val binder = serviceBinder as AudioService.AudioBinder
            // instance of the Service class itself
            // as getService returns this@AudioService
            aService = binder.getService()
            aBound = true
            Log.d(TAG, "Service connected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            // this is called when android system loses connection to the service
            // not invoked when unbind() is called from the client side
            aBound = false
            Log.d(TAG, "Service disconnected")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // start the media player and set to looping
                    // val medContext = LocalContext.current
                    val name1: String = "Edwin Thomas"
                    val name2: String = "Saket Dawgotra"
                    val navController = rememberNavController()

                    QuizApp(name1, name2, navController)
                }
            }
        }
    }

    // currently only implements the case where the app is completely not visible
    // and then comes back, not the partially visible onPause-> onResume() case.
    override fun onStart(){
        super.onStart()
        // Intent takes the current context (which is this application) and
        // the class object of service
        // .also applies processing over the intent object as a pipeline after it
        // the code creates an intent object and uses it to bind to a service with
        // a service connection. Note: bind_auto_create creates the service if its not already created
        Intent(this, AudioService::class.java).also {
            intent -> bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        Log.d(TAG, "onStart() of service")


    }
    // on app exit
    // we could also implement unbind with the connection in the onDestroy()
    // but onStop() comes before onDestroy() and terminates the bind
    override fun onStop() {
        super.onStop()
        // stop service
        unbindService(connection)
        aBound = false
    }

    override fun onPause(){
        super.onPause()
        if (aBound){
            aService?.pausePlayback()
        }
    }

    override fun onResume() {
        super.onResume()
        if (aBound){
            aService?.startPlayback()
        }
    }

    // Tag for debugging purposes
    companion object {
        private const val TAG = "MainActivity"
    }

}