package com.github.h0tk3y.kotlinFun

/**
 * Created by igushs on 1/31/16.
 */


fun <T : R, R> Sequence<T>.modifyPrefix(
        operator: Sequence<T>.() -> Sequence<R>
): Sequence<R> {
    val i = iterator()
    return i.asSequence().operator() + i.asSequence()
}

fun <T> Sequence<T>.chain(vararg operators: Sequence<T>.() -> Sequence<T>): Sequence<T> {
    val i = iterator()
    return operators.fold(emptySequence<T>()) { acc, it -> acc + i.asSequence().it() } + i.asSequence()
}

fun <T> Sequence<T>.lazyChain(vararg operators: Sequence<T>.() -> Sequence<T>): Sequence<T> {
    val i = iterator()
    return operators.fold(emptySequence<T>()) { acc, it -> acc + { i.asSequence().it() } } + i.asSequence()
}

inline operator fun <T> Sequence<T>.plus(crossinline otherGenerator: () -> Sequence<T>): Sequence<T> =
        this + object : Sequence<T> {
            val actual: Sequence<T> by lazy { otherGenerator() }
            override fun iterator(): Iterator<T> = actual.iterator()
        }