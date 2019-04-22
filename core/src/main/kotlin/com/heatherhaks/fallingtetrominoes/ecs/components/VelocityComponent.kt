package com.heatherhaks.fallingtetrominoes.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool

class VelocityComponent : Pool.Poolable, Component{
    var x = 0
    var y = 0

    override fun reset() {
        x = 0
        y = 0
    }
}