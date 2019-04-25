package com.heatherhaks.fallingtetrominoes.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.heatherhaks.fallingtetrominoes.collisiondetection.Collision
import com.heatherhaks.fallingtetrominoes.ecs.components.*
import com.heatherhaks.fallingtetrominoes.ecs.mappers.Mappers
import com.heatherhaks.fallingtetrominoes.injection.wrappers.PauseStatus
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.inject.Context
import ktx.log.logger

class StickingSystem(val tetrominoes: Array<Entity>, val map: List<Array<Entity>>, val context: Context) : IteratingSystem(
        allOf(StickingComponent::class,
                PositionComponent::class,
                TetrominoComponent::class).get()) {

    companion object {
        val log = logger<StickingSystem>()
    }

    val pauseStatus = context.inject<PauseStatus>()

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if(pauseStatus.isNotPaused) entity?.let {
            val stickingTimer = it[Mappers.stickingMapper]!!.timer

            log.debug { "Entity with Sticking Component found: Timer: $stickingTimer" }
            when {
                stickingTimer.isStopped() -> {
                    stickingTimer.goal = 0.5f
                    stickingTimer.start()
                    log.debug{"Timer isn't started, starting timer"}
                }
                stickingTimer.isFinished() -> {
                    val blocks = it[Mappers.tetrominoMapper]!!.blocks

                    log.debug{ "Checking to make sure move down is impossible"}

                    blocks.forEach {blockEntities ->
                        blockEntities[Mappers.positionMapper]!!.y++
                    }
                    if(!Collision.isCollided(blocks, map)) {
                        blocks.forEach {blockEntities ->
                            blockEntities[Mappers.positionMapper]!!.y--
                        }

                        it.remove(StickingComponent::class.java)
                    } else {
                        blocks.forEach {blockEntities ->
                            blockEntities[Mappers.positionMapper]!!.y--
                        }

                        log.debug { "Sticking tetromino" }
                        blocks.let { blockArray ->
                            blockArray.forEach{blockEntity ->
                                val position = blockEntity[Mappers.positionMapper]!!
                                val glyph = blockEntity[Mappers.glyphMapper]!!.glyph.copy()
                                glyph.value = 5
                                glyph.foregroundColor = Color.BLACK.toFloatBits()
                                val mapCell = map[position.y][position.x]

                                log.debug{ "Committing block data to map at x${position.x}/y${position.y}" }
                                mapCell[Mappers.glyphMapper]!!.glyph = glyph
                                mapCell[Mappers.positionMapper]!!.x = position.x
                                mapCell[Mappers.positionMapper]!!.y = position.y
                                mapCell.add(engine.createComponent(CanCollideComponent::class.java))
                            }
                        }

                        log.debug{"Getting tetromino ready for respawn"}
                        it.remove(FallingComponent::class.java)
                        it.remove(PositionComponent::class.java)
                        it.remove(StickingComponent::class.java)
                        val spawningComponent = engine.createComponent(NeedsSpawningComponent::class.java)
                        spawningComponent.timer.goal = 0.5f
                        spawningComponent.timer.start()
                        it.add(spawningComponent)
                    }
                }
                else -> {
                    stickingTimer.update(deltaTime)
                }
            }
        }
    }
}