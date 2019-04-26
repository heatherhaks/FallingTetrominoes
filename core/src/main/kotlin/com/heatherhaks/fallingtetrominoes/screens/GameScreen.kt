package com.heatherhaks.fallingtetrominoes.screens

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.halfdeadgames.kterminal.KTerminalData
import com.heatherhaks.fallingtetrominoes.FallingTetrominoes
import com.heatherhaks.fallingtetrominoes.GameStates
import com.heatherhaks.fallingtetrominoes.ecs.components.CanCollideComponent
import com.heatherhaks.fallingtetrominoes.ecs.components.HasPlayerInputComponent
import com.heatherhaks.fallingtetrominoes.ecs.mappers.Mappers
import com.heatherhaks.fallingtetrominoes.ecs.systems.*
import com.heatherhaks.fallingtetrominoes.ecs.templates.BlockBuilder
import com.heatherhaks.fallingtetrominoes.ecs.templates.TetrominoBuilder
import com.heatherhaks.fallingtetrominoes.injection.wrappers.*
import com.heatherhaks.fallingtetrominoes.safeAdd
import com.heatherhaks.fallingtetrominoes.input.InputHandler
import com.heatherhaks.fallingtetrominoes.tetrominoes.TetrominoHandler
import com.heatherhaks.fallingtetrominoes.timers.Timer
import ktx.app.KtxScreen
import ktx.ashley.get
import ktx.ashley.has
import ktx.ashley.hasNot
import ktx.graphics.use
import ktx.inject.Context

//TODO implement kicks
//TODO implement score
//TODO refactor pause screen + link to options screen
//TODO implement leveling as you clear lines

class GameScreen(val context: Context, game: FallingTetrominoes) : KtxScreen {
    private val batch = context.inject<SpriteBatch>()

    private val engine = context.inject<PooledEngine>()
    private val inputHandler = context.inject<InputHandler>()
    private val map = MutableList(20) { i -> Array(10) { j -> BlockBuilder.getBlankMapBlock(engine, j, i)} }

    private val terminals = context.inject<Terminals>()
    private val renderers = context.inject<Renderers>()

    val tetrominoes = TetrominoBuilder.getTetrominoEntities(engine)

    //TODO replace this with sane programming
    private val spawningLocation = context.inject<SpawningLocation>()

    val playerEntity = engine.createEntity()

    private val offsetX = 0
    private val offsetY = 0

    private val playfieldDrawingSystem = PlayfieldDrawingSystem(context, offsetX + 3, offsetY + 1)
    private val previewDrawingSystem = PreviewDrawingSystem(context, (offsetX * 2) + 28, (offsetY * 2) + 5)
    private val spawningSystem = SpawningSystem(context, game, tetrominoes, map)
    private val tetrominoDrawingSystem = TetrominoDrawingSystem(context, offsetX + 3, offsetY + 1)
    private val holdDrawingSystem = HoldDrawingSystem(context, (offsetX * 2) + 2, (offsetY * 2) + 5)
    private val gravitySystem = GravitySystem(tetrominoes, map, context)
    private val stickingSystem = StickingSystem(tetrominoes, map, context)
    private val ghostDrawingSystem = GhostDrawingSystem(context, tetrominoes, offsetX + 3, offsetY + 1)
    private val ghostPositioningSystem = GhostPositioningSystem(tetrominoes, map)
    private val inputSystem = GameInputSystem(context, map, tetrominoes)

    var startTimer = Timer(3f)
    val gameState = context.inject<GameState>()
    val gameplayKeys = context.inject<GameplayKeys>()

    private fun resetMap() {
        map.forEachIndexed {j, row ->
            row.forEachIndexed { i, _ ->
                if(map[j][i].has(Mappers.canCollideMapper)){
                    map[j][i].remove(CanCollideComponent::class.java)
                    map[j][i][Mappers.glyphMapper]?.glyph?.set(BlockBuilder.getBlankMapGlyph(i, j))
                }
            }
        }
    }

    private fun addSystems() {
        engine.addSystem(inputSystem)
        engine.addSystem(playfieldDrawingSystem)
        engine.addSystem(previewDrawingSystem)
        engine.addSystem(holdDrawingSystem)
        engine.addSystem(ghostDrawingSystem)
        engine.addSystem(tetrominoDrawingSystem)
        engine.addSystem(spawningSystem)
        engine.addSystem(gravitySystem)
        engine.addSystem(ghostPositioningSystem)
        engine.addSystem(stickingSystem)
    }
    private fun removeSystems() {
        engine.removeSystem(inputSystem)
        engine.removeSystem(playfieldDrawingSystem)
        engine.removeSystem(previewDrawingSystem)
        engine.removeSystem(holdDrawingSystem)
        engine.removeSystem(tetrominoDrawingSystem)
        engine.removeSystem(spawningSystem)
        engine.removeSystem(gravitySystem)
        engine.removeSystem(stickingSystem)
        engine.removeSystem(ghostDrawingSystem)
        engine.removeSystem(ghostPositioningSystem)
    }
    init {

        map.forEach {
            it.forEach{ block ->
                engine.addEntity(block)
            }
        }

        tetrominoes.forEach {
            engine.addEntity(it)
            it[Mappers.tetrominoMapper]?.blocks?.forEach{ entity ->
                engine.addEntity(entity)
            }
        }

        TetrominoHandler.initTetrominoes(tetrominoes)
        TetrominoHandler.setRelativePositions(tetrominoes)

        playerEntity.safeAdd(HasPlayerInputComponent::class.java, engine)
        engine.addEntity(playerEntity)

        addSystems()
    }

    override fun show() {
        super.show()

        startTimer.start()
        resetMap()
        TetrominoHandler.reset(tetrominoes)
    }

    override fun render(delta: Float) {
        super.render(delta)

        resetTerminals()

        updateTetrominoes()
        drawBorder()
        clearLines()
        engine.update(delta)

        pauseHandler(delta)
        startCountdownHandler(delta)

        batch.use {
            renderTerminals(0f, 0f)
        }
    }

    private fun pauseHandler(delta: Float) {
        when(gameState.state) {
            GameStates.PAUSED -> {
                if(startTimer.isNotRunning() && startTimer.isNotFinished()) displayPausePanel()
                if(gameplayKeys.pauseKey.isJustPressed()) {
                    startTimer.restart()
                }
            }
            GameStates.RUNNING -> {
                if(gameplayKeys.pauseKey.isJustPressed()) {
                    gameState.state = GameStates.PAUSED
                }
            }
        }

        inputHandler.tick(delta)
    }

    private fun startCountdownHandler(delta: Float) {
        if(startTimer.isFinished()) {
            gameState.state = GameStates.RUNNING
            startTimer.stop()
        }

        if(startTimer.isRunning()) {
            gameState.state = GameStates.UNPAUSING
            handleStartTimerAndDisplay(delta)
        }

        startTimer.update(delta)
    }

    private fun displayPausePanel() {
        terminals.main[offsetX + 4,offsetY + 9][Color.WHITE, Color.BLACK]
                .setCursorOffset(-0.3f, 0f).drawSingleBox(9, 3, true)
        terminals.main[offsetX + 6, offsetY + 10].write("Pause")
    }

    private fun handleStartTimerAndDisplay(delta: Float) {
        val timeLeft = 3 - startTimer.count.toInt()
        terminals.main[offsetX + 4,offsetY + 9][Color.WHITE, Color.BLACK]
                .setCursorOffset(-0.3f, 0f).drawSingleBox(9, 3, true)
        terminals.main[offsetX + 8, offsetY + 10].write("$timeLeft")
    }

    private fun clearLines() {
        for(i in 19 downTo 0) {
            var clearable = true

            map[i].forEach { if(it.hasNot(Mappers.canCollideMapper)) clearable = false }
            
            if(clearable) {
                map[i].forEach { engine.removeEntity(it) }
                map.removeAt(i)
                map.add(0, Array(10) { j -> BlockBuilder.getBlankMapBlock(engine, j, 0)} )
                map[0].forEach { engine.addEntity(it) }

                for(j in i downTo 1) {
                    for(x in 0..9)  map[j][x][Mappers.positionMapper]?.y = j
                }
            }
        }
    }

    private fun updateTetrominoes() {
        TetrominoHandler.populatePreviewPositions(tetrominoes)
        TetrominoHandler.setRelativePositions(tetrominoes)
        TetrominoHandler.setGlyphs(tetrominoes)
    }

    private fun resetTerminals() {
        terminals.main.resetCursor()
        terminals.main.clearAll()
        terminals.halfRes.resetCursor()
        terminals.halfRes.clearAll()
        terminals.playTop.resetCursor()
        terminals.playTop.clearAll()
        terminals.playBottom.resetCursor()
        terminals.playBottom.clearAll()
    }

    private fun renderTerminals(x: Float, y: Float) {
        renderers.tetromino.render(x, y, terminals.playBottom)
        renderers.tetromino.render(x, y, terminals.playTop)
        renderers.halfRes.render(x, y, terminals.halfRes)
        renderers.main.render(x, y, terminals.main)
    }

    private fun drawBorder() {

        //main border
        terminals.main[offsetX + 3, offsetY + 0].setCursorOffset(0f, 0.25f).drawLine(offsetX + 12, offsetY + 0, KTerminalData.BOX_DOUBLE_HORIZONTAL)
        terminals.main[offsetX + 3, offsetY + 21].setCursorOffset(0f, -0.25f).drawLine(offsetX + 12, offsetY + 21, KTerminalData.BOX_DOUBLE_HORIZONTAL)
        terminals.main[offsetX + 2, offsetY + 1].setCursorOffset(0.25f, 0f).drawLine(offsetX + 2, offsetY + 20, KTerminalData.BOX_DOUBLE_VERTICAL)
        terminals.main[offsetX + 13, offsetY + 1].setCursorOffset(-0.25f, 0f).drawLine(offsetX + 13, offsetY + 20, KTerminalData.BOX_DOUBLE_VERTICAL)
        terminals.main[offsetX + 2, offsetY + 0].setCursorOffset(0.25f, 0.25f).write(KTerminalData.BOX_DOUBLE_DOWN_RIGHT)
        terminals.main[offsetX + 13, offsetY + 0].setCursorOffset(-0.25f, 0.25f).write(KTerminalData.BOX_DOUBLE_DOWN_LEFT)
        terminals.main[offsetX + 2, offsetY + 21].setCursorOffset(0.25f, -0.25f).write(KTerminalData.BOX_DOUBLE_UP_RIGHT)
        terminals.main[offsetX + 13, offsetY + 21].setCursorOffset(-0.25f, -0.25f).write(KTerminalData.BOX_DOUBLE_UP_LEFT)

        //hold border
        terminals.main[offsetX + 1, offsetY + 1].setCursorOffset(0.25f, 0.25f).write(KTerminalData.BOX_SINGLE_HORIZONTAL)
        terminals.main[offsetX + 1, offsetY + 4].setCursorOffset(0.25f, 0f).write(KTerminalData.BOX_SINGLE_HORIZONTAL)
        terminals.main[offsetX + 0, offsetY + 2].setCursorOffset(0.25f, 0.25f).drawLine(offsetX + 0, offsetY + 3, KTerminalData.BOX_SINGLE_VERTICAL)
        terminals.main[offsetX + 0, offsetY + 1].setCursorOffset(0.25f, 0.25f).write(KTerminalData.BOX_SINGLE_DOWN_RIGHT)
        terminals.main[offsetX + 0, offsetY + 4].setCursorOffset(0.25f, 0f).write(KTerminalData.BOX_SINGLE_UP_RIGHT)
        terminals.main[offsetX + 2, offsetY + 1].setCursorOffset(0.25f, 0.25f).write(KTerminalData.BOX_VERTICAL_DOUBLE_LEFT_SINGLE)
        terminals.main[offsetX + 2, offsetY + 4].setCursorOffset(0.25f, 0f).write(KTerminalData.BOX_VERTICAL_DOUBLE_LEFT_SINGLE)
        if(spawningLocation.spawnFromHold) terminals.main[offsetX + 1, offsetY + 5].setCursorOffset(0.25f, 0f).write(24)

        //preview border
        //TODO make this adjust for block height
        terminals.main[offsetX + 14, offsetY + 1].setCursorOffset(-0.25f, 0.25f).write(KTerminalData.BOX_SINGLE_HORIZONTAL)
        terminals.main[offsetX + 14, offsetY + 15].setCursorOffset(-0.25f, 0f).write(KTerminalData.BOX_SINGLE_HORIZONTAL)
        terminals.main[offsetX + 15, offsetY + 2].setCursorOffset(-0.25f, 0.25f).drawLine(offsetX + 15, offsetY + 14, KTerminalData.BOX_SINGLE_VERTICAL)
        terminals.main[offsetX + 15, offsetY + 1].setCursorOffset(-0.25f, 0.25f).write(KTerminalData.BOX_SINGLE_DOWN_LEFT)
        terminals.main[offsetX + 15, offsetY + 15].setCursorOffset(-0.25f, 0f).write(KTerminalData.BOX_SINGLE_UP_LEFT)
        terminals.main[offsetX + 13, offsetY + 1].setCursorOffset(-0.25f, 0.25f).write(KTerminalData.BOX_VERTICAL_DOUBLE_RIGHT_SINGLE)
        terminals.main[offsetX + 13, offsetY + 15].setCursorOffset(-0.25f, 0f).write(KTerminalData.BOX_VERTICAL_DOUBLE_RIGHT_SINGLE)
        if(!spawningLocation.spawnFromHold) terminals.main[offsetX + 14, offsetY + 16].setCursorOffset(-0.25f, 0f).write(24)
    }
}