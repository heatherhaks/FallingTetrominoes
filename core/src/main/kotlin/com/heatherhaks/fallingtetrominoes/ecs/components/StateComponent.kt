package com.heatherhaks.fallingtetrominoes.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.heatherhaks.fallingtetrominoes.GameStates

class StateComponent : Pool.Poolable, Component {
    var state = GameStates.STARTUP
    override fun reset() {
        state = GameStates.STARTUP
    }
}