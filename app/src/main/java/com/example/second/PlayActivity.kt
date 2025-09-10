package com.example.second

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PlayActivity : AppCompatActivity() {

    private val game: GameViewModel by viewModels()
    private var selectedMove: Move? = null

    private lateinit var scoreTextView: TextView
    private lateinit var botMoveText: TextView


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_play)

        scoreTextView = findViewById(R.id.scoreText)
        botMoveText = findViewById(R.id.botMoveText)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val rockButton: ImageView = findViewById(R.id.imageRock)
        val paperButton: ImageView = findViewById(R.id.imagePaper)
        val scissorsButton: ImageView = findViewById(R.id.imageScissors)
        val buttonBack: Button = findViewById(R.id.backButton)
        val makeMove: Button = findViewById(R.id.makeMove)
        val botMoveText: TextView = findViewById(R.id.botMoveText)

        buttonBack.setOnClickListener {
            finish()
        }

        fun highlightSelection(move: Move) {
            rockButton.alpha = if (move == Move.ROCK) 1.0f else 0.5f
            paperButton.alpha = if (move == Move.PAPER) 1.0f else 0.5f
            scissorsButton.alpha = if (move == Move.SCISSORS) 1.0f else 0.5f
        }

        rockButton.setOnClickListener {
            selectedMove = Move.ROCK
            highlightSelection(Move.ROCK)
        }

        paperButton.setOnClickListener {
            selectedMove = Move.PAPER
            highlightSelection(Move.PAPER)
        }

        scissorsButton.setOnClickListener {
            selectedMove = Move.SCISSORS
            highlightSelection(Move.SCISSORS)

        }

        game.scoreText.observe(this) { text ->
            scoreTextView.text = text
        }

        game.botMove.observe(this) { bot ->
            botMoveText.text = bot
        }

        game.winner.observe(this) { winner ->
            winner?.let {
                Toast.makeText(this, "$it победил!", Toast.LENGTH_LONG).show()
                game.resetGame()
            }
        }

        makeMove.setOnClickListener {
            selectedMove?.let { move ->
                game.play(move)
            } ?: Toast.makeText(this, "Выберите ход!", Toast.LENGTH_SHORT).show()
        }
    }
}