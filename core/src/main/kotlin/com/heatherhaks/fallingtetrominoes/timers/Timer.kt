package com.heatherhaks.fallingtetrominoes.timers

import com.heatherhaks.fallingtetrominoes.timers.TimerStatus.*

@Suppress("unused")
class Timer(var goal: Float = 0f) {
    var count: Float = 0f
    var status: TimerStatus = STOPPED
        private set
    var completions = 0

    private fun increaseCount(delta: Float) {
        count += delta
    }

    private fun isGoalMet() : Boolean = count >= goal

    fun setGoal(goal: Float) : Timer {
        this.goal = goal
        return this
    }

    fun isFinished() : Boolean = status == FINISHED
    fun isNotFinished() : Boolean = !isFinished()
    fun isStopped() : Boolean = status == STOPPED
    fun isNotStopped() : Boolean = !isStopped()
    fun isRunning() : Boolean = status == RUNNING
    fun isNotRunning() : Boolean = !isRunning()

    fun start() {
        status = RUNNING
    }

    fun pause() {
        status = PAUSED
    }

    fun stop() {
        count = 0f
        status = STOPPED
        completions = 0
    }

    fun restart() {
        stop()
        start()
    }

    fun loop() {
        status = RUNNING
        completions = 0
    }

    fun reset() {
        count = 0f
        goal = 0f
        status = STOPPED
    }

    fun update(delta: Float) {
        tailrec fun countCompletions(timerCount: Float = count, completionCount: Int = 0) : Pair<Float, Int> {
            require(goal != 0f) { "A goal of 0 results in an infinite loop!" }

            return if (timerCount < goal) Pair(timerCount, completionCount)
            else countCompletions(timerCount - goal, completionCount + 1)
        }

        if (status == RUNNING) {
            increaseCount(delta)
            if(isGoalMet()) status = FINISHED
        }
        if (status == FINISHED) {
            val (newCount,newCompletions) = countCompletions()
            count = newCount
            completions = newCompletions
        }
    }

    override fun toString() : String {
        return "{count=$count, status=$status, completions=$completions, goal=$goal, goalMet=${isGoalMet()}}"
    }


}