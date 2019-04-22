package com.heatherhaks.fallingtetrominoes.injection.wrappers

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.halfdeadgames.kterminal.KTerminalRenderer
import ktx.inject.Context

class Renderers(context: Context) {
    private val batch = context.inject<SpriteBatch>()

    val main = KTerminalRenderer(batch, "main.png", 16, 17)
    val tetromino = KTerminalRenderer(batch, "tetrominoFont16x16.png", 4, 4)
    val halfRes = KTerminalRenderer(batch, "tetrominoFont8x8.png", 4, 4)


    fun dispose() {
        main.dispose()
        tetromino.dispose()
        halfRes.dispose()
    }
}