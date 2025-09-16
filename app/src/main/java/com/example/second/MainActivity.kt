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
        setupUI()
    }

    private fun setupUI() {
        findViewById<Button>(R.id.exit_btn).setOnClickListener { finishAffinity() }
        findViewById<Button>(R.id.setting_btn).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        findViewById<Button>(R.id.start_game_btn).setOnClickListener {
            startActivity(Intent(this, PlayActivity::class.java))
        }
        findViewById<Button>(R.id.newGameButton).setOnClickListener {
            gameViewModel.resetGame()
            startActivity(Intent(this, PlayActivity::class.java))
        }
    }


}