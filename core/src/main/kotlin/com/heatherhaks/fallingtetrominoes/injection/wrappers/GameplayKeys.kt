package com.heatherhaks.fallingtetrominoes.injection.wrappers

import com.badlogic.gdx.Input
import com.heatherhaks.fallingtetrominoes.input.KeyData

class GameplayKeys {
    val upKey = KeyData("UP", Input.Keys.W)
    val downKey = KeyData("DOWN", Input.Keys.S)
    val leftKey = KeyData("LEFT", Input.Keys.A)
    val rightKey = KeyData("RIGHT", Input.Keys.D)
    val clockwiseKey = KeyData("CLOCKWISE", Input.Keys.E)
    val counterclockwiseKey = KeyData("COUNTERCLOCKWISE", Input.Keys.Q)
    val hardDropKey = KeyData("HARD_DROP", Input.Keys.SPACE)
    val holdKey = KeyData("HOLD", Input.Keys.SHIFT_LEFT)

    val keyArray = arrayOf(upKey, downKey, leftKey, rightKey, clockwiseKey, counterclockwiseKey, hardDropKey, holdKey)
}