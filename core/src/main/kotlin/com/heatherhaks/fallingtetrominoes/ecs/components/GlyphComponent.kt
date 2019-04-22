package com.heatherhaks.fallingtetrominoes.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.halfdeadgames.kterminal.KTerminalGlyph

class GlyphComponent : Pool.Poolable, Component{
    var glyph = KTerminalGlyph()
    override fun reset() {
        glyph.reset()
    }
}