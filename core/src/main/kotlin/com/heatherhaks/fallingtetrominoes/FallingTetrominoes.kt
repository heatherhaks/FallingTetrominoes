package com.heatherhaks.fallingtetrominoes

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.heatherhaks.fallingtetrominoes.injection.GameModule
import com.heatherhaks.fallingtetrominoes.injection.wrappers.GameplayKeys
import com.heatherhaks.fallingtetrominoes.injection.wrappers.MenuKeys
import com.heatherhaks.fallingtetrominoes.injection.wrappers.Sound
import com.heatherhaks.fallingtetrominoes.input.InputHandler
import com.heatherhaks.fallingtetrominoes.screens.GameScreen
import com.heatherhaks.fallingtetrominoes.screens.menus.*
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context

class FallingTetrominoes : KtxGame<KtxScreen>() {
    private lateinit var gameModule: GameModule
    private lateinit var context: Context
    private lateinit var batch: SpriteBatch
    private lateinit var inputHandler: InputHandler
    private lateinit var music: Music
    private lateinit var gameplayKeys: GameplayKeys
    private lateinit var menuKeys: MenuKeys

    val debug = false
    override fun create() {
        if(debug) Gdx.app.logLevel = Application.LOG_DEBUG

        batch = SpriteBatch()

        gameModule = GameModule(this)

        context = gameModule.context

        inputHandler = context.inject()

        Gdx.input.inputProcessor = inputHandler

        gameplayKeys = context.inject()

        gameplayKeys.keyArray.forEach {
            inputHandler.register(it)
        }

        menuKeys = context.inject()
        menuKeys.keyArray.forEach {
            inputHandler.register(it)
        }

        val music = context.inject<Sound>().music
        val musicVolume = context.inject<Sound>().musicVolume

        music.volume = musicVolume
        music.isLooping = true
        music.play()

        addScreen(GameScreen(context, this))
        addScreen(MainMenuScreen(context))
        addScreen(OptionsMenuScreen(context))
        addScreen(SoundMenuScreen(context))
        addScreen(ControlsMenuScreen(context))
        addScreen(GameControlsMenuScreen(context))
        addScreen(MenuControlsMenuScreen(context))

//        addScreen(OldMenuScreen(context))
//        setScreen<GameScreen>()
        setScreen<MainMenuScreen>()
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
        gameModule.dispose()
    }
}
