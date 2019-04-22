package com.heatherhaks.fallingtetrominoes.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.heatherhaks.fallingtetrominoes.collisiondetection.Collision
import com.heatherhaks.fallingtetrominoes.ecs.components.GhostComponent
import com.heatherhaks.fallingtetrominoes.ecs.components.PositionComponent
import com.heatherhaks.fallingtetrominoes.ecs.components.TetrominoComponent
import com.heatherhaks.fallingtetrominoes.ecs.mappers.Mappers
import com.heatherhaks.fallingtetrominoes.tetrominoes.TetrominoHandler
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.has
import ktx.ashley.hasNot
import ktx.log.*

class GhostPositioningSystem(private val tetrominoes: Array<Entity>, private val map: List<Array<Entity>>) : IteratingSystem(
        allOf(GhostComponent::class,
                TetrominoComponent::class).get()) {

    companion object {
        val log = logger<GhostPositioningSystem>()
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity?.let {
            if(tetrominoes[0].has(Mappers.positionMapper) && tetrominoes[0].hasNot(Mappers.stickingMapper)) {
                it[Mappers.tetrominoMapper]!!.type = tetrominoes[0][Mappers.tetrominoMapper]!!.type
                it[Mappers.rotationMapper]!!.rotation = tetrominoes[0][Mappers.rotationMapper]!!.rotation
                if(it.hasNot(Mappers.positionMapper)) it.add(engine.createComponent(PositionComponent::class.java))

                it[Mappers.positionMapper]!!.x = tetrominoes[0][Mappers.positionMapper]!!.x
                it[Mappers.positionMapper]!!.y = tetrominoes[0][Mappers.positionMapper]!!.y

                position(deltaTime, map)
                TetrominoHandler.setRelativePositions(tetrominoes)
            } else if(it.has(Mappers.positionMapper)) it.remove(PositionComponent::class.java)
        }
    }

    fun moveDown(map: List<Array<Entity>>) {
        if(tetrominoes[8].has(Mappers.positionMapper)) {
            val position = tetrominoes[8][Mappers.positionMapper]!!

            position.y++
            TetrominoHandler.setRelativePositions(tetrominoes)

            if(Collision.isCollided(tetrominoes[8][Mappers.tetrominoMapper]?.blocks, map)) {
                position.y--
                TetrominoHandler.setRelativePositions(tetrominoes)
            }
        }
    }

    fun position(delta: Float, map: List<Array<Entity>>) {
        for(i in tetrominoes[8][Mappers.positionMapper]!!.y..19) moveDown(map)
    }
}