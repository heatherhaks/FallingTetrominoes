package com.heatherhaks.fallingtetrominoes.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.heatherhaks.fallingtetrominoes.timers.Timer

class FallingComponent : Pool.Poolable, Component{
    val timer = Timer()

    override fun reset() {
        timer.reset()
    }
}