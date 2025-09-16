package com.example.second

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class PlayActivity : AppCompatActivity() {

    private val game: GameViewModel by viewModels()
    private var selectedMove: Move? = null

    private lateinit var scoreTextView: TextView
    private lateinit var winnerTextView: TextView
    private lateinit var playerMoveImage: ImageView
    private lateinit var botMoveImage: ImageView
    private lateinit var botMoveText: TextView

    private lateinit var moveButtons: Map<ImageView, Move>

    private lateinit var animFromLeft: Animation
    private lateinit var animFromRight: Animation



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_play)

        setupUI()
        setupObservers()
        setupAnimations()
    }

    private fun setupUI() {
        scoreTextView = findViewById(R.id.scoreText)
        winnerTextView = findViewById(R.id.winnerTextView)
        botMoveText = findViewById(R.id.botMoveText)
        playerMoveImage = findViewById(R.id.playerMoveImage)
        botMoveImage = findViewById(R.id.botMoveImage)

        val rockButton: ImageView = findViewById(R.id.imageRock)
        val paperButton: ImageView = findViewById(R.id.imagePaper)
        val scissorsButton: ImageView = findViewById(R.id.imageScissors)
        val makeMove: Button = findViewById(R.id.makeMove)
        val backButton: Button = findViewById(R.id.backButton)

        moveButtons = mapOf(
            rockButton to Move.ROCK,
            paperButton to Move.PAPER,
            scissorsButton to Move.SCISSORS
        )

        moveButtons.forEach { (button, move) ->
            button.setOnClickListener {
                selectedMove = move
                highlightSelection(move)
            }
        }

        makeMove.setOnClickListener { onMakeMove() }
        backButton.setOnClickListener { finish() }
    }

    private fun setupObservers() {
        game.scoreText.observe(this) { scoreTextView.text = it }
        game.roundWinner.observe(this) { winnerTextView.text = it }
        game.winner.observe(this) { winner -> winner?.let { showWinnerDialog(it) } }
    }

    private fun setupAnimations() {
        animFromLeft = AnimationUtils.loadAnimation(this, R.anim.move_from_left)
        animFromRight = AnimationUtils.loadAnimation(this, R.anim.move_from_right)
    }

    fun getMoveDrawable(move: Move): Int {
        return when(move) {
            Move.ROCK -> R.drawable.rock
            Move.PAPER -> R.drawable.paper
            Move.SCISSORS -> R.drawable.scissors
        }
    }

    private fun onMakeMove() {
        selectedMove?.let { move ->
            // игрок сделал ход
            game.play(move)

            // анимация для картинок
            playerMoveImage.setImageResource(getMoveDrawable(move))
            playerMoveImage.visibility = View.VISIBLE
            playerMoveImage.startAnimation(animFromLeft)

            val botMove = game.botMoveObject.value!!
            botMoveImage.setImageResource(getMoveDrawable(botMove))
            botMoveImage.visibility = View.VISIBLE
            botMoveImage.startAnimation(animFromRight)

            botMoveText.text = getString(R.string.bot_move, botMove.name)
        } ?: Toast.makeText(this, R.string.choose_move, Toast.LENGTH_SHORT).show()
    }

    private fun highlightSelection(move: Move) {
        moveButtons.forEach { (button, m) ->
            button.alpha = if (m == move) 1.0f else 0.5f
        }
    }

    private fun showWinnerDialog(winner: String) {
        val title = if (winner == "Bot") getString(R.string.defeat) else getString(R.string.victory)
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage("$winner победил!")
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
                game.resetGame()
            }
            .setCancelable(false)
            .show()
    }

}