package com.heatherhaks.fallingtetrominoes.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.heatherhaks.fallingtetrominoes.GameStates
import com.heatherhaks.fallingtetrominoes.collisiondetection.Collision
import com.heatherhaks.fallingtetrominoes.ecs.components.*
import com.heatherhaks.fallingtetrominoes.ecs.mappers.Mappers
import com.heatherhaks.fallingtetrominoes.injection.wrappers.GameState
import com.heatherhaks.fallingtetrominoes.tetrominoes.TetrominoHandler
import ktx.ashley.*
import ktx.inject.Context
import ktx.log.logger

class GravitySystem(val tetrominoes: Array<Entity>, val map: List<Array<Entity>>, val context: Context) : IteratingSystem(
        allOf(FallingComponent::class,
            PositionComponent::class)
                .exclude(NeedsSpawningComponent::class).get()) {

    companion object {
        val log = logger<GravitySystem>()
    }
    val gameState = context.inject<GameState>()

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if(gameState.state == GameStates.RUNNING) entity?.let {
            val fallingTimer = it[Mappers.fallingMapper]!!.timer

            when {
                fallingTimer.isStopped() -> {

                    //TODO implement leveling up
                    val level = 1
                    val baseTime = 0.55f
                    val multiplier = 0.9f
                    val minTimer = 0.0007f

                    fallingTimer.goal = baseTime / (level * multiplier)
                    if(fallingTimer.goal < minTimer) fallingTimer.goal = 0.0007f

                    fallingTimer.start()
                }
                fallingTimer.isFinished() -> {
                    fun fall() {
                        val positionComponent = it[Mappers.positionMapper]!!
                        positionComponent.y++
                        TetrominoHandler.setRelativePositions(tetrominoes)

                        if(it.has(Mappers.tetrominoMapper) && Collision.isCollided(it[Mappers.tetrominoMapper]?.blocks, map)) {
                            positionComponent.y--
                            if(it.hasNot(Mappers.stickingMapper)) it.add(engine.createComponent(StickingComponent::class.java))
                            TetrominoHandler.setRelativePositions(tetrominoes)
                        } else {
                            if(it.has(Mappers.stickingMapper)) it.remove(StickingComponent::class.java)
                        }
                        fallingTimer.restart()
                    }

                    repeat(fallingTimer.completions) {
                        fall()
                    }
                    fallingTimer.loop()
                }
                else -> {
                    fallingTimer.update(deltaTime)
                }
            }
        }
    }
}