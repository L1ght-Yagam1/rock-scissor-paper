package com.example.second

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
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


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_play)

        scoreTextView = findViewById(R.id.scoreText)

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
        val playerMoveImage: ImageView = findViewById(R.id.playerMoveImage)
        val botMoveImage: ImageView = findViewById(R.id.botMoveImage)
        val winnerTextView: TextView = findViewById(R.id.winnerTextView)

        val animFromLeft = AnimationUtils.loadAnimation(this, R.anim.move_from_left)
        val animFromRight = AnimationUtils.loadAnimation(this, R.anim.move_from_right)

        fun resetRound() {
            // Сбрасываем прозрачность
            playerMoveImage.alpha = 1.0f
            botMoveImage.alpha = 1.0f

            // Сброс победителя раунда
            winnerTextView.text = ""
        }


        fun highlightWinner(playerMove: Move, botMove: Move) {



            val playerWins = playerMove.defeats(botMove)
            val botWins = botMove.defeats(playerMove)

            if (playerWins) {
                // Бот проиграл — делаем его картинку полупрозрачной
                botMoveImage.animate().alpha(0.3f).setDuration(500).start()
                playerMoveImage.alpha = 1.0f
            } else if (botWins) {
                // Игрок проиграл
                playerMoveImage.animate().alpha(0.3f).setDuration(500).start()
                botMoveImage.alpha = 1.0f
            } else {
                // Ничья
                playerMoveImage.alpha = 1.0f
                botMoveImage.alpha = 1.0f
            }
        }



        fun getMoveDrawable(move: Move): Int {
            return when(move) {
                Move.ROCK -> R.drawable.rock
                Move.PAPER -> R.drawable.paper
                Move.SCISSORS -> R.drawable.scissors
            }
        }


        fun onPlayerMoveSelected(playerMove: Move) {
            resetRound()

            playerMoveImage.setImageResource(getMoveDrawable(playerMove))
            playerMoveImage.visibility = View.VISIBLE
            playerMoveImage.startAnimation(animFromLeft)

            game.play(playerMove)

            // Подсветка победителя
            val botMove = game.botMoveObject.value!!
            Handler(Looper.getMainLooper()).postDelayed({
                highlightWinner(playerMove, botMove) }, 300)
        }



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

        game.roundWinner.observe(this) { text ->
            winnerTextView.text = text
        }

        game.botMoveObject.observe(this) { botMove ->
            botMove?.let {
                botMoveImage.setImageResource(getMoveDrawable(it))
                botMoveImage.visibility = View.VISIBLE
                botMoveImage.startAnimation(animFromRight)
            }
        }

        game.winner.observe(this) { winner ->
            winner?.let {
                Toast.makeText(this, "$it победил!", Toast.LENGTH_LONG).show()
                game.resetGame()
            }
        }

        makeMove.setOnClickListener {
            selectedMove?.let { move ->
                onPlayerMoveSelected(move)
            } ?: Toast.makeText(this, "Выберите ход!", Toast.LENGTH_SHORT).show()
        }

    }
}