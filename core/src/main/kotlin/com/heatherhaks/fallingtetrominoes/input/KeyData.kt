package com.heatherhaks.fallingtetrominoes.input

import com.badlogic.gdx.Input
import com.heatherhaks.fallingtetrominoes.input.KeyState.*

@Suppress("MemberVisibilityCanBePrivate", "unused")
class KeyData(var name: String, var keycode: Int) {
    var state = IDLE
    var count = 0f

    fun isIdle() = state == IDLE
    fun isNotIdle() = !isIdle()

    fun isJustPressed() = state == JUST_PRESSED
    fun isNotJustPressed() = !isJustPressed()

    fun isJustReleased() = state == JUST_RELEASED
    fun isNotJustReleased() = !isJustReleased()

    fun isHeld() = state == HELD
    fun isNotHeld() = !isHeld()

    fun isActive() = isNotIdle() && isNotJustReleased()
    fun isNotActive() = isIdle() || isJustReleased()

    override fun toString() : String = "{name=$name, key=${Input.Keys.toString(keycode)}/$keycode, count=$count, state=$state}"
}