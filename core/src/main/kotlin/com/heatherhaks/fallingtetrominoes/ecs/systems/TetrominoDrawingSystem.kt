package com.heatherhaks.fallingtetrominoes.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.heatherhaks.fallingtetrominoes.ecs.components.*
import com.heatherhaks.fallingtetrominoes.ecs.mappers.Mappers
import com.heatherhaks.fallingtetrominoes.injection.wrappers.Terminals
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.inject.Context
import ktx.log.logger

class TetrominoDrawingSystem(context: Context, private val offsetX: Int, private val offsetY: Int) : IteratingSystem(
        allOf(PositionComponent::class,
                TetrominoComponent::class,
                MainTetrominoComponent::class).get()){

    companion object {
        val log = logger<TetrominoDrawingSystem>()
    }

    private val terminals = context.inject<Terminals>()

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity?.let {
            val tetromino = it[Mappers.tetrominoMapper] ?: TetrominoComponent()

            tetromino.blocks.forEach { block ->
                val blockPosition = block[Mappers.positionMapper] ?: PositionComponent()
                val glyph = block[Mappers.glyphMapper]?.glyph ?: GlyphComponent().glyph
                terminals.playTop[blockPosition.x + offsetX, blockPosition.y + offsetY].write(glyph)
            }
        }
    }
}