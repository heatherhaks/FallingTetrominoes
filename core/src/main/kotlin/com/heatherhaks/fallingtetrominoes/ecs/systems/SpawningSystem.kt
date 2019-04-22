package com.heatherhaks.fallingtetrominoes.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.heatherhaks.fallingtetrominoes.FallingTetrominoes
import com.heatherhaks.fallingtetrominoes.collisiondetection.Collision
import com.heatherhaks.fallingtetrominoes.ecs.components.FallingComponent

import com.heatherhaks.fallingtetrominoes.ecs.components.NeedsSpawningComponent
import com.heatherhaks.fallingtetrominoes.ecs.components.PositionComponent
import com.heatherhaks.fallingtetrominoes.ecs.mappers.Mappers
import com.heatherhaks.fallingtetrominoes.injection.wrappers.SpawningLocation
import com.heatherhaks.fallingtetrominoes.screens.MenuScreen
import com.heatherhaks.fallingtetrominoes.tetrominoes.TetrominoHandler
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.has
import ktx.ashley.remove
import ktx.inject.Context
import ktx.log.logger

class SpawningSystem(context: Context, val game: FallingTetrominoes, val tetrominoes: Array<Entity>, val map: List<Array<Entity>>) : IteratingSystem(allOf(NeedsSpawningComponent::class).get()) {
    companion object {
        val log = logger<SpawningSystem>()
    }

    val spawningLocation = context.inject<SpawningLocation>()

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity?.let {
            val timer = it[Mappers.needsSpawningMapper]?.timer ?: NeedsSpawningComponent().timer

            log.debug { "Entity with spawning component found: Timer: $timer" }
            if(timer.isFinished()) {
                log.debug { "Spawning" }

                it.remove<NeedsSpawningComponent>()
                val fallingComponent = engine.createComponent(FallingComponent::class.java)
                fallingComponent.timer.goal = 0.5f
                //TODO Implement level up effect on falling timer
                //TODO implement game over if can't spawn

                it.add(fallingComponent)

                val position = engine.createComponent(PositionComponent::class.java)
                position.x = 4
                position.y = 1
                it.add(position)

                if(it.has(Mappers.rotationMapper)) it[Mappers.rotationMapper]!!.reset()

                TetrominoHandler.setRelativePositions(tetrominoes)

                when(spawningLocation.spawnFromHold) {
                    true -> {
                        tetrominoes[0][Mappers.tetrominoMapper]!!.type = tetrominoes[7][Mappers.tetrominoMapper]!!.type
                        spawningLocation.isHoldActive = false
                        spawningLocation.spawnFromHold = false
                    }
                    false -> TetrominoHandler.newTetromino(tetrominoes)
                }

                if(Collision.isCollided(tetrominoes[0][Mappers.tetrominoMapper]!!.blocks, map)) {
                    game.setScreen<MenuScreen>()
                }

            } else {
                timer.update(deltaTime)
            }
        }
    }
}