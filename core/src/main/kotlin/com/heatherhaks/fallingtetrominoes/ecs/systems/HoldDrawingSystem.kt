package com.heatherhaks.fallingtetrominoes.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.heatherhaks.fallingtetrominoes.ecs.components.GlyphComponent
import com.heatherhaks.fallingtetrominoes.ecs.components.HoldComponent
import com.heatherhaks.fallingtetrominoes.ecs.components.PositionComponent
import com.heatherhaks.fallingtetrominoes.ecs.components.TetrominoComponent
import com.heatherhaks.fallingtetrominoes.ecs.mappers.Mappers
import com.heatherhaks.fallingtetrominoes.injection.wrappers.SpawningLocation
import com.heatherhaks.fallingtetrominoes.injection.wrappers.Terminals
import com.heatherhaks.fallingtetrominoes.tetrominoes.TetrominoTypes
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.inject.Context
import ktx.log.logger

class HoldDrawingSystem(context: Context, val offsetX: Int, val offsetY: Int) : IteratingSystem(allOf(
        PositionComponent::class,
        TetrominoComponent::class,
        HoldComponent::class).get()) {

    companion object {
        val log = logger<HoldDrawingSystem>()
    }

    private val terminals = context.inject<Terminals>()
    private val spawningLocation = context.inject<SpawningLocation>()

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity?.let {
            if(spawningLocation.isHoldActive) {
                val blocks = it[Mappers.tetrominoMapper]?.blocks ?: TetrominoComponent().blocks
                blocks.forEach { block ->
                    val position = block[Mappers.positionMapper] ?: PositionComponent()
                    val glyph = block[Mappers.glyphMapper]?.glyph ?: GlyphComponent().glyph
                    glyph.offsetX = 0.25f
                    glyph.offsetY = 0.25f
                    var typeOffset = 0
                    when(it[Mappers.tetrominoMapper]?.type) {
                        TetrominoTypes.I -> {
                            typeOffset = 0
                            glyph.offsetX = 0f
                            glyph.offsetY = 0.25f
                        }
                        TetrominoTypes.O -> {
                            typeOffset = 1
                            glyph.offsetY = 0.25f
                        }
                        else ->  {
                            typeOffset = 0
                            glyph.offsetY = 0.75f
                        }
                    }
                    terminals.halfRes[position.x + offsetX, position.y + offsetY + typeOffset].write(glyph)
                }
            }
        }
    }
}