package com.heatherhaks.fallingtetrominoes.screens

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.heatherhaks.fallingtetrominoes.FallingTetrominoes
import com.heatherhaks.fallingtetrominoes.injection.wrappers.Renderers
import com.heatherhaks.fallingtetrominoes.injection.wrappers.TerminalSize
import com.heatherhaks.fallingtetrominoes.injection.wrappers.Terminals
import com.heatherhaks.fallingtetrominoes.input.InputHandler
import com.heatherhaks.fallingtetrominoes.input.KeyData
import com.heatherhaks.fallingtetrominoes.input.KeyState
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.graphics.use
import ktx.inject.Context

class MenuScreen(context: Context) : KtxScreen {
    val batch = context.inject<SpriteBatch>()
    val game = context.inject<FallingTetrominoes>()
    var terminalSize = context.inject<TerminalSize>()
    val terminal = context.inject<Terminals>().main
    val renderer = context.inject<Renderers>().main

    val inputHandler = context.inject<InputHandler>()

    val upKey = KeyData("UP", Input.Keys.W)
    val downKey = KeyData("DOWN", Input.Keys.S)
    val leftKey = KeyData("LEFT", Input.Keys.A)
    val rightKey = KeyData("RIGHT", Input.Keys.D)
    val clockwiseKey = KeyData("CLOCKWISE", Input.Keys.E)
    val counterclockwiseKey = KeyData("COUNTERCLOCKWISE", Input.Keys.Q)
    val hardDropKey = KeyData("HARD_DROP", Input.Keys.SPACE)
    val holdKey = KeyData("HOLD", Input.Keys.SHIFT_LEFT)
    val selectKey = KeyData("SELECT", Input.Keys.ENTER)
    init {
//        inputHandler.registerAll(   upKey,
//                                    downKey,
//                                    leftKey,
//                                    rightKey,
//                                    clockwiseKey,
//                                    counterclockwiseKey,
//                                    hardDropKey,
//                                    holdKey,
//                                    selectKey)
    }

    val menuEntries = arrayOf(
            arrayOf("Play Game", "Options", "Exit"),
            arrayOf("Controls", "Back"),
            arrayOf("UP=${Input.Keys.toString(inputHandler.keyMap["UP"]!!.keycode)}",
                    "DOWN=${Input.Keys.toString(inputHandler.keyMap["DOWN"]!!.keycode)}",
                    "LEFT=${Input.Keys.toString(inputHandler.keyMap["LEFT"]!!.keycode)}",
                    "RIGHT=${Input.Keys.toString(inputHandler.keyMap["RIGHT"]!!.keycode)}",
                    "CLOCKWISE=${Input.Keys.toString(inputHandler.keyMap["CLOCKWISE"]!!.keycode)}",
                    "COUNTERCLOCKWISE=${Input.Keys.toString(inputHandler.keyMap["COUNTERCLOCKWISE"]!!.keycode)}",
                    "SELECT=${Input.Keys.toString(inputHandler.keyMap["SELECT"]!!.keycode)}",
                    "HARD DROP=${Input.Keys.toString(inputHandler.keyMap["HARD_DROP"]!!.keycode)}",
                    "HOLD=${Input.Keys.toString(inputHandler.keyMap["HOLD"]!!.keycode)}",
                    "Back"))
    var menuNumber = 0
    var cursorNumber = 0
        set(value) {
            field = value
            val size = menuEntries[menuNumber].size
            while(field < 0) field += size
            while(field >= size) field -= size
        }
    var isWaitingForInput = false

    override fun show() {
        super.show()
    }

    override fun render(delta: Float) {
        super.render(delta)
        clear()
        processInput(delta)
        draw()
    }

    private fun clear() {
        clearScreen(0f, 0f, 0f, 1f)
        terminal.resetCursor()
        terminal.clearAll()
    }

    fun processInput(delta: Float) {
        if(!isWaitingForInput) {
            if(inputHandler.keyMap["DOWN"]?.state == KeyState.JUST_RELEASED) {
                cursorNumber++
            }
            if(inputHandler.keyMap["UP"]?.state == KeyState.JUST_RELEASED) {
                cursorNumber--
            }
            if(inputHandler.keyMap["SELECT"]?.state == KeyState.JUST_RELEASED) {
                processSelection()
            }
        } else {
            mapControls()
        }
        inputHandler.tick(delta)
    }

    fun processSelection() {
        when {
            cursorNumber == 0 && menuNumber == 0 -> gotoGameScreen()
            cursorNumber == 1 && menuNumber == 0 -> goToOptionsMenu()
            cursorNumber == 2 && menuNumber == 0 -> System.exit(0)
            cursorNumber == 0 && menuNumber == 1 -> goToControlsMenu()
            cursorNumber == 1 && menuNumber == 1 -> goToMainMenu()
            cursorNumber in 0..8 && menuNumber == 2 -> isWaitingForInput = true
            cursorNumber == 9 && menuNumber == 2 -> goToOptionsMenu()
        }
    }

    fun gotoGameScreen() {
        inputHandler.reset()
        clear()
        game.setScreen<GameScreen>()
    }

    fun mapControls() {
        if(inputHandler.isAnyKeyPressed && inputHandler.keycode != -1) {
            when(cursorNumber) {
                0 -> inputHandler.keyMap["UP"]?.keycode = inputHandler.keycode
                1 -> inputHandler.keyMap["DOWN"]?.keycode = inputHandler.keycode
                2 -> inputHandler.keyMap["LEFT"]?.keycode = inputHandler.keycode
                3 -> inputHandler.keyMap["RIGHT"]?.keycode = inputHandler.keycode
                4 -> inputHandler.keyMap["CLOCKWISE"]?.keycode = inputHandler.keycode
                5 -> inputHandler.keyMap["COUNTERCLOCKWISE"]?.keycode = inputHandler.keycode
                6 -> inputHandler.keyMap["SELECT"]?.keycode = inputHandler.keycode
                7 -> inputHandler.keyMap["HARD_DROP"]?.keycode = inputHandler.keycode
                8 -> inputHandler.keyMap["HOLD"]?.keycode = inputHandler.keycode
            }
            isWaitingForInput = false
            updateControlsMenu()
        }
    }

    fun updateControlsMenu() {
        menuEntries[2][0] = "UP=${Input.Keys.toString(inputHandler.keyMap["UP"]!!.keycode)}"
        menuEntries[2][1] = "DOWN=${Input.Keys.toString(inputHandler.keyMap["DOWN"]!!.keycode)}"
        menuEntries[2][2] = "LEFT=${Input.Keys.toString(inputHandler.keyMap["LEFT"]!!.keycode)}"
        menuEntries[2][3] = "RIGHT=${Input.Keys.toString(inputHandler.keyMap["RIGHT"]!!.keycode)}"
        menuEntries[2][4] = "CLOCKWISE=${Input.Keys.toString(inputHandler.keyMap["CLOCKWISE"]!!.keycode)}"
        menuEntries[2][5] = "COUNTERCLOCKWISE=${Input.Keys.toString(inputHandler.keyMap["COUNTERCLOCKWISE"]!!.keycode)}"
        menuEntries[2][6] = "SELECT=${Input.Keys.toString(inputHandler.keyMap["SELECT"]!!.keycode)}"
        menuEntries[2][7] = "HARD DROP=${Input.Keys.toString(inputHandler.keyMap["HARD_DROP"]!!.keycode)}"
        menuEntries[2][8] = "HOLD=${Input.Keys.toString(inputHandler.keyMap["HOLD"]!!.keycode)}"
    }

    fun goToControlsMenu() {
        menuNumber = 2
        cursorNumber = 0
    }

    fun goToMainMenu() {
        menuNumber = 0
        cursorNumber = 0
    }

    fun goToOptionsMenu() {
        menuNumber = 1
        cursorNumber = 0
    }

    private fun draw() {
        drawMenu()

        if(isWaitingForInput) terminal[0,-1].write("Press a key...")

        batch.use {
            renderer.render(0f, 0f, terminal)
        }
    }

    private fun drawMenu() {
        val menu = menuEntries[menuNumber]
        var maxWidth = 0
        for(i in menu) if(i.length > maxWidth) maxWidth = i.length
        var x = (terminalSize.width / 2) - (maxWidth / 2)
        var y = (terminalSize.height / 2) - (menu.size / 2)

        for(i in 0 until menu.size) terminal[x, y + i].write(menu[i])

        terminal[x - 1, y + cursorNumber]. write('>')
    }

    override fun hide() {
        super.hide()
    }
}