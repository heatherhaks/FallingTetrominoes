package com.heatherhaks.fallingtetrominoes.tetrominoes

class TetrominoBag {
    private val freshList = listOf(TetrominoTypes.I,
            TetrominoTypes.J,
            TetrominoTypes.L,
            TetrominoTypes.O,
            TetrominoTypes.S,
            TetrominoTypes.T,
            TetrominoTypes.Z)

    val bag = freshList.shuffled().toMutableList()
    init {
        bag += freshList.shuffled()
    }

    fun getTetrominoType() : TetrominoTypes {
        if(bag.size < 8) bag += freshList.shuffled()
        return bag.removeAt(0)
    }

    fun reset() {
        bag.clear()
        bag += freshList.shuffled()
        bag += freshList.shuffled()
    }

}