package com.heatherhaks.fallingtetrominoes.tetrominoes

enum class TetrominoRotations(val layout: Array<Array<Pair<Int, Int>>>){
    I(arrayOf(
            arrayOf(Pair(-1, 0), Pair(0, 0), Pair(1, 0), Pair(2, 0)),
            arrayOf(Pair(1, -1), Pair(1, 0), Pair(1, 1), Pair(1, 2)),
            arrayOf(Pair(-1, 1), Pair(0, 1), Pair(1, 1), Pair(2, 1)),
            arrayOf(Pair(0, -1), Pair(0, 0), Pair(0, 1), Pair(0, 2)))),
    J(arrayOf(
            arrayOf(Pair(-1, -1), Pair(-1, 0), Pair(0, 0), Pair(1, 0)),
            arrayOf(Pair(0, -1), Pair(0, 0), Pair(0, 1), Pair(1, -1)),
            arrayOf(Pair(-1, 0), Pair(0, 0), Pair(1, 0), Pair(1, 1)),
            arrayOf(Pair(-1, 1), Pair(0, -1), Pair(0, 0), Pair(0, 1)))),
    L(arrayOf(
            arrayOf(Pair(-1, 0), Pair(0, 0), Pair(1, -1), Pair(1, 0)),
            arrayOf(Pair(0, -1), Pair(0, 0), Pair(0, 1), Pair(1, 1)),
            arrayOf(Pair(-1, 0), Pair(-1, 1), Pair(0, 0), Pair(1, 0)),
            arrayOf(Pair(-1, -1), Pair(0, -1), Pair(0, 0), Pair(0, 1)))),
    O(arrayOf(
            arrayOf(Pair(0, -1), Pair(1, -1), Pair(0, 0), Pair(1, 0)),
            arrayOf(Pair(0, -1), Pair(1, -1), Pair(0, 0), Pair(1, 0)),
            arrayOf(Pair(0, -1), Pair(1, -1), Pair(0, 0), Pair(1, 0)),
            arrayOf(Pair(0, -1), Pair(1, -1), Pair(0, 0), Pair(1, 0)))),
    S(arrayOf(
            arrayOf(Pair(-1, 0), Pair(0, -1), Pair(0, 0), Pair(1, -1)),
            arrayOf(Pair(0, -1), Pair(0, 0), Pair(1, 0), Pair(1, 1)),
            arrayOf(Pair(-1, 1), Pair(0, 0), Pair(0, 1), Pair(1, 0)),
            arrayOf(Pair(-1, -1), Pair(-1, 0), Pair(0, 0), Pair(0, 1)))),
    T(arrayOf(
            arrayOf(Pair(-1, 0), Pair(0, -1), Pair(0, 0), Pair(1, 0)),
            arrayOf(Pair(0, -1), Pair(0, 0), Pair(0, 1), Pair(1, 0)),
            arrayOf(Pair(-1, 0), Pair(0, 0), Pair(1, 0), Pair(0, 1)),
            arrayOf(Pair(-1, 0), Pair(0, -1), Pair(0, 0), Pair(0, 1)))),
    Z(arrayOf(
            arrayOf(Pair(-1, -1), Pair(0, -1), Pair(0, 0), Pair(1, 0)),
            arrayOf(Pair(0, 0), Pair(0, 1), Pair(1, -1), Pair(1, 0)),
            arrayOf(Pair(-1, 0), Pair(0, 0), Pair(0, 1), Pair(1, 1)),
            arrayOf(Pair(-1, 0), Pair(-1, 1), Pair(0, 0), Pair(0, -1)))),
    NONE(arrayOf(
            arrayOf(Pair(0, 0), Pair(0, 0), Pair(0, 0), Pair(0, 0)),
            arrayOf(Pair(0, 0), Pair(0, 0), Pair(0, 0), Pair(0, 0)),
            arrayOf(Pair(0, 0), Pair(0, 0), Pair(0, 0), Pair(0, 0)),
            arrayOf(Pair(0, 0), Pair(0, 0), Pair(0, 0), Pair(0, 0))))
}