package com.heatherhaks.fallingtetrominoes.ecs.templates

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.Color
import com.halfdeadgames.kterminal.KTerminalGlyph
import com.heatherhaks.fallingtetrominoes.ecs.components.BlockComponent
import com.heatherhaks.fallingtetrominoes.ecs.components.GlyphComponent
import com.heatherhaks.fallingtetrominoes.ecs.components.PositionComponent
import com.heatherhaks.fallingtetrominoes.ecs.mappers.Mappers
import ktx.ashley.get
import ktx.graphics.copy

object BlockBuilder {
    fun getPositionlessBlock(engine: PooledEngine) : Entity {
        val glyphComponent = engine.createComponent(GlyphComponent::class.java)
        val blockComponent = engine.createComponent(BlockComponent::class.java)

        return engine.createEntity().add(glyphComponent).add(blockComponent)
    }

    fun getPositionedBlock(engine: PooledEngine, x: Int, y: Int, glyph: KTerminalGlyph) : Entity {
        val entity = getPositionlessBlock(engine)

        entity[Mappers.glyphMapper]?.glyph?.set(glyph)

        val position = engine.createComponent(PositionComponent::class.java)
        position.x = x
        position.y = y

        return entity.add(position)
    }

    fun getBlankMapGlyph(x: Int, y: Int) : KTerminalGlyph {
        val value = when {
            x in 0..8 && y != 0 -> 1
            x in 0..8 && y == 0 -> 2
            x == 9 && y == 0 -> 3
            else -> 4 }

        return KTerminalGlyph(value, Color.WHITE.copy(alpha = 0.1f).toFloatBits(), Color.BLACK.toFloatBits())
    }

    fun getBlankMapBlock(engine: PooledEngine, x: Int, y: Int) : Entity {

        return getPositionedBlock(engine, x, y, getBlankMapGlyph(x, y))
    }
}