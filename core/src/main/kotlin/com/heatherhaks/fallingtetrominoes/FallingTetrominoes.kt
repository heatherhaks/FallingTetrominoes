package com.heatherhaks.fallingtetrominoes

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.heatherhaks.fallingtetrominoes.injection.GameModule
import com.heatherhaks.fallingtetrominoes.injection.wrappers.GameplayKeys
import com.heatherhaks.fallingtetrominoes.input.InputHandler
import com.heatherhaks.fallingtetrominoes.screens.GameScreen
import com.heatherhaks.fallingtetrominoes.screens.MenuScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context

class FallingTetrominoes : KtxGame<KtxScreen>() {
    private lateinit var gameModule: GameModule
    private lateinit var  context: Context
    private lateinit var batch: SpriteBatch
    private lateinit var inputHandler: InputHandler
    private lateinit var engine: PooledEngine
    private lateinit var music: Music
    private lateinit var gameplayKeys: GameplayKeys

    val musicOn = false
    val debug = false
    override fun create() {
        if(debug) Gdx.app.logLevel = Application.LOG_DEBUG

        if(musicOn) {
            music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"))
            music.volume = 0.25f
            music.isLooping = true
            music.play()
        }

        batch = SpriteBatch()

        gameModule = GameModule(this)

        context = gameModule.context

        inputHandler = context.inject()

        Gdx.input.inputProcessor = inputHandler

        gameplayKeys = context.inject()

        inputHandler.register(gameplayKeys.leftKey)
        inputHandler.register(gameplayKeys.rightKey)
        inputHandler.register(gameplayKeys.upKey)
        inputHandler.register(gameplayKeys.downKey)
        inputHandler.register(gameplayKeys.hardDropKey)
        inputHandler.register(gameplayKeys.clockwiseKey)
        inputHandler.register(gameplayKeys.counterclockwiseKey)
        inputHandler.register(gameplayKeys.holdKey)

        addScreen(GameScreen(context, this))
//        addScreen(MenuScreen(context))
        setScreen<GameScreen>()
//        setScreen<MenuScreen>()
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
        gameModule.dispose()
    }
}
