package com.heatherhaks.fallingtetrominoes.screens.menus

import ktx.inject.Context

class OptionsMenuScreen(context: Context) : Menu(context) {
    override fun getMenuText(): Array<String> {
        val output = arrayOf(
                "Sound",
                "Controls",
                "Back")

        return output
    }

    override fun menuActions() {
        when(cursorY) {
            0 -> game.setScreen<SoundMenuScreen>()
            1 -> game.setScreen<ControlsMenuScreen>()
            2 -> game.setScreen<MainMenuScreen>()
        }
    }
}