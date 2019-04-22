package com.heatherhaks.fallingtetrominoes.collisiondetection

import com.badlogic.ashley.core.Entity
import com.heatherhaks.fallingtetrominoes.ecs.mappers.Mappers
import ktx.ashley.get
import ktx.ashley.has

object Collision {
    fun isCollided(blocks: MutableList<Entity>?, map: List<Array<Entity>>) : Boolean {
        var isCollided = false

        blocks?.let {
            it.forEach {entity ->
                val x = entity[Mappers.positionMapper]?.x ?: -1
                val y = entity[Mappers.positionMapper]?.y ?: -1
                if(x !in 0..9 || y !in 0..19 || map[y][x].has(Mappers.canCollideMapper)) isCollided = true
            }
        }

        return isCollided
    }
}