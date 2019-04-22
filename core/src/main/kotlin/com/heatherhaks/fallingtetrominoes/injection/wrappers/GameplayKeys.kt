package com.heatherhaks.fallingtetrominoes.injection.wrappers

import com.badlogic.gdx.Input
import com.heatherhaks.fallingtetrominoes.input.KeyData

class GameplayKeys {
    val leftKey = KeyData("LEFT", Input.Keys.A)
    val rightKey = KeyData("RIGHT", Input.Keys.D)
    val clockwiseKey = KeyData("ROTATE CW", Input.Keys.E)
    val counterclockwiseKey = KeyData("ROTATE CCW", Input.Keys.Q)
    val softDropKey = KeyData("SOFT DROP", Input.Keys.S)
    val hardDropKey = KeyData("HARD DROP", Input.Keys.SPACE)
    val holdKey = KeyData("HOLD", Input.Keys.SHIFT_LEFT)

    val keyArray = arrayOf(leftKey, rightKey, clockwiseKey, counterclockwiseKey, softDropKey, hardDropKey, holdKey)
}