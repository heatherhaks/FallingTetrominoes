package com.heatherhaks.fallingtetrominoes.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool

class RotationComponent : Pool.Poolable, Component {
    var rotation = 0
        set(value) {
            field = value and 3
        }

    override fun reset() {
        rotation = 0
    }
}