package com.heatherhaks.fallingtetrominoes.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.heatherhaks.fallingtetrominoes.collisiondetection.Collision
import com.heatherhaks.fallingtetrominoes.ecs.components.HasPlayerInputComponent
import com.heatherhaks.fallingtetrominoes.ecs.components.RotationComponent
import com.heatherhaks.fallingtetrominoes.ecs.components.StickingComponent
import com.heatherhaks.fallingtetrominoes.ecs.mappers.Mappers
import com.heatherhaks.fallingtetrominoes.injection.wrappers.GameplayKeys
import com.heatherhaks.fallingtetrominoes.input.InputHandler
import com.heatherhaks.fallingtetrominoes.input.KeyState
import com.heatherhaks.fallingtetrominoes.safeAdd
import com.heatherhaks.fallingtetrominoes.tetrominoes.TetrominoHandler
import com.heatherhaks.fallingtetrominoes.timers.Timer
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.has
import ktx.inject.Context
import ktx.log.*

//TODO DAS should be able to be charged while waiting for a piece to spawn

class InputSystem(val context: Context, val map: List<Array<Entity>>, val tetrominoes: Array<Entity>) : IteratingSystem(allOf(HasPlayerInputComponent::class).get()) {

    companion object {
        val log = logger<InputSystem>()
    }

    private val DAS_DELAY = 0.25f
    private val DAS_GOAL = 0.05f

    private val inputHandler = context.inject<InputHandler>()

    private var lateralDelta = 0
    private var verticalDelta = 0

    private class Status {
        var isActive = false
    }
    private var leftStatus = Status()
    private var rightStatus = Status()
    private val leftTimer = Timer(DAS_DELAY)
    private val rightTimer = Timer(DAS_DELAY)
    private val dropTimer = Timer(DAS_GOAL)

    private var isSticking = false
    private val gameplayKeys = context.inject<GameplayKeys>()

    //TODO implement holding
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity?.let {
            lateralDelta = 0
            verticalDelta = 0

//            val leftKey = inputHandler.keyMap["LEFT"]!!
//            val rightKey = inputHandler.keyMap["RIGHT"]!!
//            val downKey = inputHandler.keyMap["DOWN"]!!
//            val hardDropKey = inputHandler.keyMap["HARD_DROP"]!!
//            val clockwiseKey = inputHandler.keyMap["CLOCKWISE"]!!
//            val counterclockwiseKey = inputHandler.keyMap["COUNTERCLOCKWISE"]!!

            if(gameplayKeys.leftKey.isActive() && gameplayKeys.rightKey.isNotActive()) {
                lateralMove(deltaTime, leftTimer, leftStatus, -1)
            } else {
                leftStatus.isActive = false
                leftTimer.goal = DAS_DELAY
                leftTimer.stop()
            }
            if(gameplayKeys.rightKey.isActive() && gameplayKeys.leftKey.isNotActive()) {
                lateralMove(deltaTime, rightTimer, rightStatus, 1)
            } else {
                rightStatus.isActive = false
                rightTimer.goal = DAS_DELAY
                rightTimer.stop()
            }

            if(gameplayKeys.downKey.isActive()) {
                softDrop(deltaTime, dropTimer)
            }

            if(gameplayKeys.hardDropKey.isJustPressed()) {
                hardDrop()
            }

            if(gameplayKeys.clockwiseKey.isJustPressed()) {
                tetrominoes[0].safeAdd(RotationComponent::class.java, engine)
                tetrominoes[0][Mappers.rotationMapper]!!.rotation++
            }

            if(gameplayKeys.counterclockwiseKey.isJustPressed()) {
                tetrominoes[0].safeAdd(RotationComponent::class.java, engine)
                tetrominoes[0][Mappers.rotationMapper]!!.rotation--
            }

            commitMove()

            if(isSticking) {
                log.debug { "Adding sticking component" }
                tetrominoes[0].safeAdd(StickingComponent::class.java, engine)
                isSticking = false
            }
        }
    }

    private fun commitMove() {

        if(tetrominoes[0].has(Mappers.positionMapper)) {
            tetrominoes[0][Mappers.positionMapper]!!.x += lateralDelta
            TetrominoHandler.setRelativePositions(tetrominoes)

            if(Collision.isCollided(tetrominoes[0][Mappers.tetrominoMapper]!!.blocks, map)) {
                tetrominoes[0][Mappers.positionMapper]!!.x -= lateralDelta
                TetrominoHandler.setRelativePositions(tetrominoes)
            }
        }

        tailrec fun undoDrop() {
            tetrominoes[0][Mappers.positionMapper]!!.y--
            TetrominoHandler.setRelativePositions(tetrominoes)

            isSticking = true

            if(Collision.isCollided(tetrominoes[0][Mappers.tetrominoMapper]!!.blocks, map)) {
                undoDrop()
            }
        }

        if(verticalDelta != 0) {
            if(tetrominoes[0].has(Mappers.positionMapper)) {
                tetrominoes[0][Mappers.positionMapper]!!.y += verticalDelta
                TetrominoHandler.setRelativePositions(tetrominoes)

                if(Collision.isCollided(tetrominoes[0][Mappers.tetrominoMapper]!!.blocks, map)) {
                    undoDrop()
                }
            }
        }

        TetrominoHandler.setRelativePositions(tetrominoes)
    }

    private fun softDrop(deltaTime: Float, timer: Timer) {
        log.debug { "softDrop={deltaTime=$deltaTime, timer=$timer}" }

        if(timer.isStopped()) {
            log.debug { "softDrop={verticalDelta += 1}" }
            verticalDelta += 1
            timer.start()
        }
        if(timer.isFinished()) {
            log.debug { "softDrop={verticalDelta += 1}" }
            verticalDelta += 1
            timer.restart()
        }

        timer.update(deltaTime)
    }

    private fun hardDrop() {
        log.debug { "hardDrop={verticalDelta += 20}" }
        verticalDelta += 20
    }

    private fun lateralMove(deltaTime: Float, timer: Timer, status: Status, moveDelta: Int) {
        log.debug { "lateralMove={deltaTime=$deltaTime, timer=$timer, status=$status, moveDelta=$moveDelta}" }

        when(status.isActive) {
            false -> {
                if(timer.isStopped()) {
                    timer.start()
                    lateralDelta += moveDelta
                }
                if(timer.isFinished()) {
                    timer.goal = DAS_GOAL
                    timer.restart()
                    status.isActive = true
                }
            }
            true -> {
                if(timer.isFinished()) {
                    lateralDelta += moveDelta
                    timer.restart()
                }
            }
        }

        timer.update(deltaTime)
    }
}