package com.example.second

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private val game: GameViewModel by viewModels()

    private lateinit var buttonBack: Button
    private lateinit var textMaxValue: TextView
    private lateinit var maxValueBar: SeekBar

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        setupUI()
        setupObservers()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupUI() {
        buttonBack = findViewById(R.id.backButton)
        textMaxValue = findViewById(R.id.maxValueText)
        maxValueBar = findViewById(R.id.maxValueBar)

        maxValueBar.max = 10
        maxValueBar.min = 1

        maxValueBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) game.setMaxScore(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun setupObservers() {
        game.maxScore.observe(this) { score ->
            maxValueBar.progress = score
        }

        game.maxScoreText.observe(this) { text ->
            textMaxValue.text = text
        }
    }
}