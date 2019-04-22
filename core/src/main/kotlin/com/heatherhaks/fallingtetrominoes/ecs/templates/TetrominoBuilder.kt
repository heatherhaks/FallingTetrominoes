package com.heatherhaks.fallingtetrominoes.ecs.templates

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.heatherhaks.fallingtetrominoes.ecs.components.*
import com.heatherhaks.fallingtetrominoes.ecs.mappers.Mappers
import com.heatherhaks.fallingtetrominoes.tetrominoes.TetrominoTypes
import ktx.ashley.get

object TetrominoBuilder {
    fun getTetrominoEntities(engine: PooledEngine) : Array<Entity> {
        val entities = Array(9) { engine.createEntity()
                .add(engine.createComponent(TetrominoComponent::class.java))
        }

        entities.forEach { entity ->
            repeat(4) {
                entity[Mappers.tetrominoMapper]?.blocks?.add(engine.createEntity()
                    .add(engine.createComponent(GlyphComponent::class.java))
                    .add(engine.createComponent(PositionComponent::class.java)))
            }
        }

        val spawningComponent = engine.createComponent(NeedsSpawningComponent::class.java)
        spawningComponent.timer.goal = 0.5f
        spawningComponent.timer.start()
        entities[0]
                .add(engine.createComponent(RotationComponent::class.java))
                .add(spawningComponent)
                .add(engine.createComponent(MainTetrominoComponent::class.java))

        for(i in 1..6) {
            val rotationComponent = engine.createComponent(RotationComponent::class.java)
            rotationComponent.rotation = 1

            entities[i]
                    .add(rotationComponent)
                    .add(engine.createComponent(PreviewComponent::class.java))
                    .add(engine.createComponent(PositionComponent::class.java))

            entities[i][Mappers.tetrominoMapper]?.blocks?.forEach {
                it.add(engine.createComponent(PreviewComponent::class.java))
            }
        }

        val rotationComponent = engine.createComponent(RotationComponent::class.java)
        rotationComponent.rotation = 1

        entities[7]
                .add(engine.createComponent(HoldComponent::class.java))
                .add(rotationComponent)
                .add(engine.createComponent(PositionComponent::class.java))

        entities[8]
                .add(engine.createComponent(GhostComponent::class.java))
                .add(engine.createComponent(RotationComponent::class.java))

        return entities
    }
}