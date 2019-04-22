package com.heatherhaks.fallingtetrominoes.injection

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.heatherhaks.fallingtetrominoes.FallingTetrominoes
import com.heatherhaks.fallingtetrominoes.injection.wrappers.*
import com.heatherhaks.fallingtetrominoes.input.InputHandler
import ktx.inject.Context

class GameModule(game: FallingTetrominoes) {
    val context = Context()

    init {
        context.register {
            bindSingleton(Sound())
            bindSingleton(game)
            bindSingleton(SpriteBatch())
            bindSingleton(PooledEngine())
            bindSingleton(SpawningLocation())
            bindSingleton(GameplayKeys())
            bindSingleton(MenuKeys())
        }

        context.register {
            bindSingleton(InputHandler())
            bindSingleton(TerminalSize())
        }

        context.register {
            bindSingleton(Terminals(context))
            bindSingleton(Renderers(context))
        }
    }

    fun dispose() {
        val renderers = context.inject<Renderers>()
        renderers.dispose()

        context.dispose()
    }
}