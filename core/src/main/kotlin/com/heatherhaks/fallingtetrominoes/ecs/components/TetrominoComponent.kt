package com.heatherhaks.fallingtetrominoes.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import com.heatherhaks.fallingtetrominoes.tetrominoes.TetrominoTypes

class TetrominoComponent : Pool.Poolable, Component{
    var type = TetrominoTypes.NONE
    val blocks = MutableList(0) { Entity()}

    override fun reset() {
        blocks.clear()
    }
}