package com.heatherhaks.fallingtetrominoes.injection.wrappers

import com.badlogic.gdx.Input
import com.heatherhaks.fallingtetrominoes.input.KeyData

class MenuKeys {
    val upKey = KeyData("MENU UP", Input.Keys.W)
    val downKey = KeyData("MENU DOWN", Input.Keys.S)
    val leftKey = KeyData("MENU LEFT", Input.Keys.A)
    val rightKey = KeyData("MENU RIGHT", Input.Keys.D)
    val select = KeyData("MENU SELECT", Input.Keys.SPACE)
    val escape = KeyData("MENU ESCAPE", Input.Keys.ESCAPE)

    val keyArray = arrayOf(upKey, downKey, leftKey, rightKey, select, escape)
}