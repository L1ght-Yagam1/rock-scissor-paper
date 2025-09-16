package com.example.second

class Game {
    var playerScore = 0
    var botScore = 0
    var maxScore = 3
    var winner: String? = null

    var roundWinner  = ""

     var botMove: Move = Move.PAPER

    fun play(playerMove: Move) {
        this.botMove = Move.entries.random()
        if (playerMove.defeats(this.botMove)) {
            roundWinner = "Player"
            playerScore++
        } else if (this.botMove.defeats(playerMove)) {
            botScore++
            roundWinner = "Bot"
        }

        else
            roundWinner = "Ничья"

        if (playerScore >= maxScore) winner = "Player"
        if (botScore >= maxScore) winner = "Bot"
    }



    fun reset() {
        playerScore = 0
        botScore = 0
        winner = null
    }

    fun setScores(player: Int, bot: Int, max1: Int, roundWinner: String) {
        playerScore = player
        botScore = bot
        maxScore = max1
        this.roundWinner = roundWinner
    }
}
