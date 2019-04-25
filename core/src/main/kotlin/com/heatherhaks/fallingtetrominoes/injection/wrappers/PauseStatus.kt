package com.heatherhaks.fallingtetrominoes.injection.wrappers

class PauseStatus {
    var isPaused : Boolean = true
    var isNotPaused: Boolean
        get() = !isPaused
        set(value) {
            isPaused = !value
        }
}