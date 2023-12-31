package com.example.dancognitionapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.dancognitionapp.assessment.AssessmentActivity
import com.example.dancognitionapp.landing.LandingDestination
import com.example.dancognitionapp.landing.LandingPageScreen
import com.example.dancognitionapp.participants.ParticipantsActivity
import com.example.dancognitionapp.utils.theme.DanCognitionAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DanCognitionAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LandingPageScreen() { destination ->
                        when(destination) {
                            LandingDestination.StartTrial ->
                                startActivity(AssessmentActivity.newIntent(this, false))
                            LandingDestination.Practice ->
                                startActivity(AssessmentActivity.newIntent(this, true))
                            LandingDestination.AddParticipants -> {
                                startActivity(Intent(this, ParticipantsActivity::class.java))
                            }
                        }
                    }
                }
            }
        }
    }
}
