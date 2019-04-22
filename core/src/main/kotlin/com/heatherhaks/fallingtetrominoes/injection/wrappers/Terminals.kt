package com.heatherhaks.fallingtetrominoes.injection.wrappers

import com.badlogic.gdx.graphics.Color
import com.halfdeadgames.kterminal.KTerminalData
import ktx.inject.Context

class Terminals(context: Context) {
    private val terminalSize = context.inject<TerminalSize>()

    val main = KTerminalData(terminalSize.width, terminalSize.height, Color.WHITE.toFloatBits(), Color.CLEAR.toFloatBits())
    val playTop = KTerminalData(terminalSize.width, terminalSize.height, Color.WHITE.toFloatBits(), Color.CLEAR.toFloatBits())
    val playBottom = KTerminalData(terminalSize.width, terminalSize.height, Color.WHITE.toFloatBits(), Color.CLEAR.toFloatBits())
    val halfRes = KTerminalData(terminalSize.width * 2, terminalSize.height * 2, Color.WHITE.toFloatBits(), Color.CLEAR.toFloatBits())
}