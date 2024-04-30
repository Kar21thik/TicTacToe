package com.example.myapplication

import java.text.FieldPosition
import kotlin.random.Random

data class GameModel (
    var gameId: String ="-1",
    var filledPosition: MutableList<String> = mutableListOf("","","","","","","","","",),
    var winner :String ="",
    var gamestatus:GameStatus =GameStatus.CREATED,
    var currentPlayer: String =( arrayOf("X","O"))[Random.nextInt(2)]

)
enum class GameStatus {
    CREATED,
    JOINED,
    INPROGRESS,
    FINISHED
}