package com.heatherhaks.fallingtetrominoes.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.heatherhaks.fallingtetrominoes.ecs.components.*
import com.heatherhaks.fallingtetrominoes.ecs.mappers.Mappers
import com.heatherhaks.fallingtetrominoes.injection.wrappers.Terminals
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.copy
import ktx.inject.Context
import ktx.log.*

class GhostDrawingSystem(context: Context, private val tetrominoes: Array<Entity>, private val offsetX: Int, private val offsetY: Int) : IteratingSystem(
        allOf(TetrominoComponent::class,
                GhostComponent::class,
                PositionComponent::class).get()){

    companion object {
        val log = logger<GhostDrawingSystem>()
    }

    private val terminals = context.inject<Terminals>()

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity?.let {

            val tetromino = it[Mappers.tetrominoMapper] ?: TetrominoComponent()

            tetromino.blocks.forEach { block ->
                val blockPosition = block[Mappers.positionMapper] ?: PositionComponent()
                val glyph = block[Mappers.glyphMapper]?.glyph ?: GlyphComponent().glyph

                glyph.backgroundColor = Color.CLEAR.toFloatBits()
                glyph.foregroundColor = Color.WHITE.toFloatBits()
                glyph.value = 5

                terminals.playTop[blockPosition.x + offsetX, blockPosition.y + offsetY].write(glyph)
            }
        }
    }
}