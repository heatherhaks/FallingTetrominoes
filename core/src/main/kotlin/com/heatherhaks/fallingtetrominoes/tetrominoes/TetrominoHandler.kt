package com.heatherhaks.fallingtetrominoes.tetrominoes

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.halfdeadgames.kterminal.KTerminalGlyph
import com.heatherhaks.fallingtetrominoes.ecs.components.GlyphComponent
import com.heatherhaks.fallingtetrominoes.ecs.components.PositionComponent
import com.heatherhaks.fallingtetrominoes.ecs.components.TetrominoComponent
import com.heatherhaks.fallingtetrominoes.ecs.mappers.Mappers
import ktx.ashley.get

object TetrominoHandler {
    val bag = TetrominoBag()

    fun initTetrominoes(tetrominoes: Array<Entity>) {
        for(i in 1..6) {
            tetrominoes[i][Mappers.tetrominoMapper]?.type = bag.getTetrominoType()
        }
        populateHoldPosition(tetrominoes)
    }

    fun setRelativePositions(tetrominoes: Array<Entity>) {
        for(i in 0..8) {
            val rotation = tetrominoes[i][Mappers.rotationMapper]?.rotation ?: 0
            val tetromino = tetrominoes[i][Mappers.tetrominoMapper] ?: TetrominoComponent()
            val position = tetrominoes[i][Mappers.positionMapper] ?: PositionComponent()

            tetromino.blocks.forEachIndexed { block, it ->
                val blockPosition = it[Mappers.positionMapper] ?: PositionComponent()

                val delta = when(tetromino.type) {
                    TetrominoTypes.I -> TetrominoRotations.I.layout[rotation][block]
                    TetrominoTypes.J -> TetrominoRotations.J.layout[rotation][block]
                    TetrominoTypes.L -> TetrominoRotations.L.layout[rotation][block]
                    TetrominoTypes.O -> TetrominoRotations.O.layout[rotation][block]
                    TetrominoTypes.S -> TetrominoRotations.S.layout[rotation][block]
                    TetrominoTypes.T -> TetrominoRotations.T.layout[rotation][block]
                    else -> TetrominoRotations.Z.layout[rotation][block]
                }

                blockPosition.x = delta.first + position.x
                blockPosition.y = delta.second + position.y
            }
        }
    }

    fun setGlyphs(tetrominoes: Array<Entity>) {
        for(i in 0..7) {
            val tetromino = tetrominoes[i][Mappers.tetrominoMapper] ?: TetrominoComponent()

            tetromino.blocks.forEach {
                val blockGlyph = it[Mappers.glyphMapper]?.glyph ?: GlyphComponent().glyph

                val glyph = when(tetromino.type) {
                    TetrominoTypes.I -> KTerminalGlyph(5, Color.BLACK.toFloatBits(), TetrominoColors.I)
                    TetrominoTypes.J -> KTerminalGlyph(5, Color.BLACK.toFloatBits(), TetrominoColors.J)
                    TetrominoTypes.L -> KTerminalGlyph(5, Color.BLACK.toFloatBits(), TetrominoColors.L)
                    TetrominoTypes.O -> KTerminalGlyph(5, Color.BLACK.toFloatBits(), TetrominoColors.O)
                    TetrominoTypes.S -> KTerminalGlyph(5, Color.BLACK.toFloatBits(), TetrominoColors.S)
                    TetrominoTypes.T -> KTerminalGlyph(5, Color.BLACK.toFloatBits(), TetrominoColors.T)
                    else -> KTerminalGlyph(5, Color.BLACK.toFloatBits(), TetrominoColors.Z)
                }

                blockGlyph.set(glyph)
            }
        }
    }

    fun newTetromino(tetrominoes: Array<Entity>) {
        for(i in 0..5) {
            tetrominoes[i][Mappers.tetrominoMapper]?.type = tetrominoes[i + 1][Mappers.tetrominoMapper]?.type ?: TetrominoTypes.NONE
        }

        tetrominoes[6][Mappers.tetrominoMapper]?.type = bag.getTetrominoType()

        setRelativePositions(tetrominoes)
    }

    private fun populateHoldPosition(entities: Array<Entity>) {
        val position = entities[7][Mappers.positionMapper] ?: PositionComponent()

        position.x = when(entities[7][Mappers.tetrominoMapper]?.type ?: TetrominoComponent().type) {
            TetrominoTypes.I -> -1
            else -> 0
        }
        position.y = 0
    }

    fun populatePreviewPositions(entities: Array<Entity>) {
        var offset = 0

        for(i in 1..6) {
            val position = entities[i][Mappers.positionMapper] ?: PositionComponent()

            position.x = when(entities[i][Mappers.tetrominoMapper]?.type ?: TetrominoComponent().type) {
                TetrominoTypes.I -> -1
                else -> 0
            }

            position.y = offset

            offset += when(entities[i][Mappers.tetrominoMapper]?.type ?: TetrominoComponent().type) {
                TetrominoTypes.I -> 5
                TetrominoTypes.O -> 3
                else -> 4
            }
        }
    }

    fun reset(tetrominoes: Array<Entity>) {
        bag.reset()
        initTetrominoes(tetrominoes)
        setRelativePositions(tetrominoes)
        setGlyphs(tetrominoes)
        populateHoldPosition(tetrominoes)
        populatePreviewPositions(tetrominoes)
    }
}