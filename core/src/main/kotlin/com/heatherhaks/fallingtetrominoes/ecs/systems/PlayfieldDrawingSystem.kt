package com.heatherhaks.fallingtetrominoes.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.heatherhaks.fallingtetrominoes.ecs.components.BlockComponent
import com.heatherhaks.fallingtetrominoes.ecs.components.GlyphComponent
import com.heatherhaks.fallingtetrominoes.ecs.components.PositionComponent
import com.heatherhaks.fallingtetrominoes.ecs.components.TetrominoComponent
import com.heatherhaks.fallingtetrominoes.ecs.mappers.Mappers
import com.heatherhaks.fallingtetrominoes.injection.wrappers.Terminals
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.inject.Context
import ktx.log.logger

class PlayfieldDrawingSystem(context: Context, val offsetX: Int, val offsetY: Int) : IteratingSystem(
        allOf(PositionComponent::class,
                GlyphComponent::class,
                BlockComponent::class)
                .exclude(TetrominoComponent::class).get()){

    companion object {
        val log = logger<PlayfieldDrawingSystem>()
    }

    private val terminal = context.inject<Terminals>().playBottom

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity?.let {
            val position = it[Mappers.positionMapper] ?: PositionComponent()
            val glyph = it[Mappers.glyphMapper]?.glyph ?: GlyphComponent().glyph

            terminal[offsetX + position.x, offsetY + position.y].write(glyph)
        }
    }
}