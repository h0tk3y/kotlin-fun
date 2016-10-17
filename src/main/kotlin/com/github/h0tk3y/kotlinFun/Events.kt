package com.github.h0tk3y.kotlinFun

import java.util.*

interface HandlersCollector<F : Function<Unit>> {
    fun addHandler(handler: F)
    fun handleOnce(handler: F)
    fun removeHandler(handler: F)

    operator fun plusAssign(handler: F) = addHandler(handler)
    operator fun minusAssign(handler: F) = removeHandler(handler)
}

fun <F : Function<Unit>> Event<F>.asHandlersCollector() = object : HandlersCollector<F> by this {}

open class Event<F : Function<Unit>> : HandlersCollector<F> {
    protected val handlers = LinkedHashSet<F>()
    protected val handleOnceHandlers = HashSet<F>()

    protected inline fun trigger(handlerAction: (F) -> Unit) {
        handlers.toList().forEach {
            if (it in handleOnceHandlers)
                handlers.remove(it)
            handlerAction(it)
        }
    }

    override fun addHandler(handler: F) {
        handlers.add(handler)
    }

    override fun handleOnce(handler: F) {
        handlers.add(handler)
        handleOnceHandlers.add(handler)
    }

    override fun removeHandler(handler: F) {
        handlers.remove(handler)
        handleOnceHandlers.remove(handler)
    }

    fun removeAllHandlers() {
        handlers.clear()
        handleOnceHandlers.clear()
    }
}

class NoArgumentEvent : Event<() -> Unit>() {
    fun trigger() = trigger { it() }
    operator fun invoke() = trigger()
}

class OneArgumentEvent<T> : Event<(T) -> Unit>() {
    fun trigger(arg: T) = trigger { it(arg) }
    operator fun invoke(arg: T) = trigger(arg)
}

class TwoArgumentsEvent<T1, T2> : Event<(T1, T2) -> Unit>() {
    fun trigger(arg1: T1, arg2: T2) = trigger { it(arg1, arg2) }
    operator fun invoke(arg1: T1, arg2: T2) = trigger(arg1, arg2)
}

class ThreeArgumentsEvent<T1, T2, T3> : Event<(T1, T2, T3) -> Unit>() {
    fun trigger(arg1: T1, arg2: T2, arg3: T3) = trigger { it(arg1, arg2, arg3) }
    operator fun invoke(arg1: T1, arg2: T2, arg3: T3) = trigger(arg1, arg2, arg3)
}

fun event0() = NoArgumentEvent()
fun <T> event1() = OneArgumentEvent<T>()
fun <T1, T2> event2() = TwoArgumentsEvent<T1, T2>()
fun <T1, T2, T3> event3() = ThreeArgumentsEvent<T1, T2, T3>()