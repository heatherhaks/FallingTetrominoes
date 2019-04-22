package com.heatherhaks.fallingtetrominoes

import com.badlogic.ashley.core.*
import ktx.ashley.get
import ktx.ashley.has
import ktx.ashley.hasNot

fun <T: Component> Entity.safeAdd(component: Class<T>, engine: Engine) {
    if(this.hasNot(ComponentMapper.getFor(component))) this.add(engine.createComponent(component))
}

fun <T: Component> Entity.safeRemove(mapper: ComponentMapper<T>) {
    if(this.has(mapper)) this.remove(this[mapper]!!.javaClass)
}

//extends list to return a list with a replaced value
fun <E> List<E>.updated(index: Int, newElement: E) = this.mapIndexed { i, e -> if (i == index) newElement else e }