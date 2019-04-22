package com.heatherhaks.fallingtetrominoes.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool

class CanCollideComponent : Pool.Poolable, Component{
    override fun reset() {}
}