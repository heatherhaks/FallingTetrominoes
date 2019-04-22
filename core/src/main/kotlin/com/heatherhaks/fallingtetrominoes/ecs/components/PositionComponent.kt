package com.heatherhaks.fallingtetrominoes.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool

class PositionComponent : Pool.Poolable, Component{
    var x = 0
    var y = 0

    override fun reset() {
        x = 0
        y = 0
    }

    override fun toString() : String = "{x=$x, y=$y}"
}