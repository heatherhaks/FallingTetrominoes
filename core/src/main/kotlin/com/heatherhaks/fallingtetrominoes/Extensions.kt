package com.heatherhaks.fallingtetrominoes

import com.badlogic.ashley.core.*
import ktx.ashley.get
import ktx.ashley.has
import ktx.ashley.hasNot
import kotlin.math.max
import kotlin.math.min

fun <T: Component> Entity.safeAdd(component: Class<T>, engine: Engine) {
    if(this.hasNot(ComponentMapper.getFor(component))) this.add(engine.createComponent(component))
}

fun <T: Component> Entity.safeRemove(mapper: ComponentMapper<T>) {
    if(this.has(mapper)) this.remove(this[mapper]!!.javaClass)
}

//extends list to return a list with a replaced value
fun <E> List<E>.updated(index: Int, newElement: E) = this.mapIndexed { i, e -> if (i == index) newElement else e }

fun Int.clamp(minimum: Int, maximum: Int): Int = min(max(minimum, this), maximum)
fun Short.clamp(minimum: Short, maximum: Short): Short = min(max(minimum.toInt(), this.toInt()), maximum.toInt()).toShort()
fun Long.clamp(minimum: Long, maximum: Long): Long = min(max(minimum, this), maximum)
fun Float.clamp(minimum: Float, maximum: Float): Float = min(max(minimum, this), maximum)
fun Double.clamp(minimum: Double, maximum: Double): Double = min(max(minimum, this), maximum)

fun Float.format(digits: Int): String = java.lang.String.format("%.${digits}f", this)
fun Double.format(digits: Int): String = java.lang.String.format("%.${digits}f", this)