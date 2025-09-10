package com.example.second

enum class Move() {
    ROCK, PAPER(), SCISSORS;

    fun defeats(other: Move): Boolean {
        return (this == ROCK && other == SCISSORS) ||
                (this == SCISSORS && other == PAPER) ||
                (this == PAPER && other == ROCK)
    }
}
