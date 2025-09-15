package com.example.second

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.core.content.edit
import androidx.lifecycle.MediatorLiveData

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val game =  Game()

    private val prefs = application.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)

    // Переменные для изменения данных
    private val _playerScore = MutableLiveData(prefs.getInt("playerScore", 0))
    private val _botScore = MutableLiveData(prefs.getInt("botScore", 0))
    private val _maxScore = MutableLiveData(prefs.getInt("maxScore", 3))
    private val _botMove = MutableLiveData<Move?>()
    private val _winner =  MutableLiveData<String?>()

    private val _botMoveObject = MutableLiveData<Move?>()

    val botMoveObject: LiveData<Move?> = _botMoveObject

    // Переменные для отображения данных
    val maxScore: LiveData<Int> = _maxScore

    val maxScoreText: LiveData<String> = MediatorLiveData<String>().apply {
        fun update() {
            value = "Макс очков: ${_maxScore.value ?: 0}"
        }
        addSource(_maxScore) { update() }
        update()
    }
    val botMove: LiveData<String> = MediatorLiveData<String>().apply {
        fun update() {
            value = "Бот выбрал ${_botMove.value ?: "ничего"}"
        }
        addSource(_botMove) { update() }
    }
    val winner: LiveData<String?> = _winner
    private val _roundWinner =  MutableLiveData<String>()
    val roundWinner: LiveData<String> = MediatorLiveData<String>().apply {
        fun update() {
            value = "Победитель раунда: ${_roundWinner.value ?: ""}"
        }
        addSource(_roundWinner) { update() }
        update()
    }

    val scoreText: LiveData<String> = MediatorLiveData<String>().apply {
        fun update() {
            value = "Player: ${_playerScore.value ?: 0}  Bot: ${_botScore.value ?: 0}"
        }
        addSource(_playerScore) { update() }
        addSource(_botScore) { update() }
    }

    init {
        // загрузить сохранённые очки (если есть) и применить в game
        // Данные есть = берём, нету = значения по умолчанию (p1)
        val p = prefs.getInt("playerScore", 0)
        val b = prefs.getInt("botScore", 0)
        val m = prefs.getInt("maxScore", 3)

        game.setScores(p, b, m)

        update()
    }

    fun update() {
        _playerScore.value = game.playerScore
        _botScore.value = game.botScore
        _maxScore.value = game.maxScore
        _winner.value = game.winner
        _botMove.value = game.botMove
        _botMoveObject.value = game.botMove
        _roundWinner.value = game.roundWinner
    }


    fun play(playerMove: Move) {
        game.play(playerMove)
        update()
        saveState()
    }

    fun resetGame() {
        game.reset()
        update()
        saveState()
    }

    fun setMaxScore(maxValue: Int) {
        game.maxScore = maxValue
        update()
        saveState()
    }
    private fun saveState() {
        prefs.edit {
            putInt("playerScore", _playerScore.value ?: 0)
            putInt("botScore", _botScore.value ?: 0)
            putInt("maxScore", _maxScore.value ?: 3)
        }
    }
}
