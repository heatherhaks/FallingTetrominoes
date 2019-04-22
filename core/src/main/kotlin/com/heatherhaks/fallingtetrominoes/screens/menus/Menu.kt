package com.heatherhaks.fallingtetrominoes.screens.menus

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.halfdeadgames.kterminal.KTerminalData
import com.halfdeadgames.kterminal.KTerminalRenderer
import com.heatherhaks.fallingtetrominoes.FallingTetrominoes
import com.heatherhaks.fallingtetrominoes.injection.wrappers.MenuKeys
import com.heatherhaks.fallingtetrominoes.injection.wrappers.Renderers
import com.heatherhaks.fallingtetrominoes.injection.wrappers.TerminalSize
import com.heatherhaks.fallingtetrominoes.injection.wrappers.Terminals
import com.heatherhaks.fallingtetrominoes.input.InputHandler
import ktx.app.KtxScreen
import ktx.graphics.use
import ktx.inject.Context

abstract class Menu(context: Context) : KtxScreen {
    val game: FallingTetrominoes = context.inject()
    val batch: SpriteBatch = context.inject()
    val terminal: KTerminalData = context.inject<Terminals>().main
    val renderer: KTerminalRenderer = context.inject<Renderers>().main
    val terminalSize: TerminalSize = context.inject()
    var cursorY: Int = 0
    val inputHandler: InputHandler = context.inject()
    val menuKeys: MenuKeys = context.inject()
    var menuCount: Int = 0

    override fun show() {
        cursorY = 0
    }

    override fun render(delta: Float) {
        terminal.resetCursor()
        terminal.clearAll()

        processInput()
        inputHandler.tick(delta)

        displayMenuText()

        batch.use {
            renderer.render(0f, 0f, terminal)
        }
    }

    abstract fun getMenuText() : Array<String>

    open fun displayMenuText() {
        val text = getMenuText()
        menuCount = text.size
        var textMaxLength = 0
        text.forEach {
            if(it.length > textMaxLength) textMaxLength = it.length
        }
        val startX = (terminalSize.width / 2) - (textMaxLength / 2)
        val startY = (terminalSize.height / 2) - (text.size / 2)

        text.forEachIndexed{ i, it ->
            terminal[startX, startY + i].write(it)
        }

        terminal[startX - 1, startY + cursorY].write('>')
    }

    abstract fun menuActions()

    open fun processInput() {
        if(inputHandler.isAnyKeyPressed) {
            if(menuKeys.upKey.isJustPressed()) cursorY--
            if(menuKeys.downKey.isJustPressed()) cursorY++
            if(cursorY < 0) cursorY = 0
            if(cursorY >= menuCount) cursorY = menuCount - 1
            if(menuKeys.select.isJustPressed()) menuActions()
        }
    }
}