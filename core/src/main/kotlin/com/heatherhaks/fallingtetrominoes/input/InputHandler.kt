package com.heatherhaks.fallingtetrominoes.input

import ktx.app.KtxInputAdapter

class InputHandler : KtxInputAdapter {
    val keyMap = mutableMapOf<String, KeyData>()

    var keyTyped = 0.toChar()
    var keycode = -1
    var isKeyTyped = false
    var isAnyKeyPressed = false

    fun register(keyData: KeyData) {
        keyMap[keyData.name] = keyData
    }

    fun registerAll(vararg keys: KeyData) {
        for(item in keys) {
            register(item)
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        keyMap.forEach{
            if(keycode == it.value.keycode) it.value.state = KeyState.JUST_PRESSED
        }
        this.keycode = keycode
        isAnyKeyPressed = true

        return true
    }

    override fun keyTyped(character: Char): Boolean {
        keyTyped = character
        isKeyTyped = true

        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        keyMap.forEach{
            if(keycode == it.value.keycode) it.value.state = KeyState.JUST_RELEASED
        }

        return true
    }

    fun tick(delta: Float) {
        keyMap.forEach{
            when(it.value.state) {
                KeyState.JUST_PRESSED -> {
                    it.value.count = 0f
                    it.value.state = KeyState.HELD
                }
                KeyState.HELD -> it.value.count += delta
                KeyState.JUST_RELEASED, KeyState.IDLE -> it.value.state = KeyState.IDLE
            }
        }

        keyTyped = 0.toChar()
        keycode = -1
        isKeyTyped = false
        isAnyKeyPressed = false
    }

    fun reset() {
        keyMap.forEach{
            it.value.state = KeyState.IDLE
            it.value.count = 0f
        }
    }
}