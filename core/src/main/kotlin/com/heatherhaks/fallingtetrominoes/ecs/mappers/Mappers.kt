package com.heatherhaks.fallingtetrominoes.ecs.mappers

import com.heatherhaks.fallingtetrominoes.ecs.components.*
import ktx.ashley.mapperFor

object Mappers {
    val canCollideMapper = mapperFor<CanCollideComponent>()
    val fallingMapper = mapperFor<FallingComponent>()
    val glyphMapper = mapperFor<GlyphComponent>()
    val positionMapper = mapperFor<PositionComponent>()
    val rotationMapper = mapperFor<RotationComponent>()
    val stickingMapper = mapperFor<StickingComponent>()
    val tetrominoMapper = mapperFor<TetrominoComponent>()
    val velocityMapper = mapperFor<VelocityComponent>()
    val needsSpawningMapper = mapperFor<NeedsSpawningComponent>()
    val hasPlayerInputMapper = mapperFor<HasPlayerInputComponent>()
}