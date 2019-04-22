package com.heatherhaks.fallingtetrominoes.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.heatherhaks.fallingtetrominoes.ecs.components.*
import com.heatherhaks.fallingtetrominoes.ecs.mappers.Mappers
import com.heatherhaks.fallingtetrominoes.injection.wrappers.Terminals
import com.heatherhaks.fallingtetrominoes.tetrominoes.TetrominoTypes
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.inject.Context
import ktx.log.logger

class PreviewDrawingSystem(context: Context, val offsetX: Int, val offsetY: Int) : IteratingSystem(allOf(
        PositionComponent::class,
        TetrominoComponent::class,
        PreviewComponent::class).get()){

    companion object {
        val log = logger<PreviewDrawingSystem>()
    }

    private val terminals = context.inject<Terminals>()

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity?.let { workingEntity ->
            val blocks = workingEntity[Mappers.tetrominoMapper]?.blocks ?: TetrominoComponent().blocks
            val typeOffset = when(workingEntity[Mappers.tetrominoMapper]?.type) {
                TetrominoTypes.I -> 0.5f
                else -> 0f
            }
            blocks.forEach { block ->
                val position = block[Mappers.positionMapper] ?: PositionComponent()
                val glyph = block[Mappers.glyphMapper]?.glyph ?: GlyphComponent().glyph
                glyph.offsetX = -0.25f + typeOffset
                glyph.offsetY = 0.25f
                terminals.halfRes[position.x + offsetX, position.y + offsetY].write(glyph)
            }
        }
    }
}