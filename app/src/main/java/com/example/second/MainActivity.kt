package com.example.second

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import kotlin.getValue

class MainActivity : AppCompatActivity() {

    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val exitButton: Button = findViewById(R.id.exit_btn)
        val newGameButton: Button = findViewById(R.id.newGameButton)
        val intentPlay =  Intent(this, PlayActivity::class.java)
        val intentSettings =  Intent(this, SettingsActivity::class.java)


        exitButton.setOnClickListener {
            finishAffinity()
        }


        val buttonSettings: Button = findViewById(R.id.setting_btn)

        buttonSettings.setOnClickListener {
            startActivity(intentSettings)
        }

        val buttonPlay: Button = findViewById(R.id.start_game_btn)

        buttonPlay.setOnClickListener {
            startActivity(intentPlay)
        }

        newGameButton.setOnClickListener {
            gameViewModel.resetGame()
            startActivity(intentPlay)
        }
    }
}