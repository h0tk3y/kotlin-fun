package com.github.h0tk3y.kotlinFun

fun <T : R, R> Sequence<T>.modifyPrefix(
        operator: Sequence<T>.() -> Sequence<R>
): Sequence<R> = iterator().let { it.asSequence().operator() + it.asSequence() }

fun <T> Sequence<T>.chain(vararg operators: Sequence<T>.() -> Sequence<T>) = object : Sequence<T> {
    val i by lazy { this@chain.iterator() }
    val underlying by lazy { operators.map { it(i.asSequence()) }.reduce { a, b -> a + b } + i.asSequence() }

    override fun iterator(): Iterator<T> = underlying.iterator()
}

fun <T> Sequence<T>.lazyChain(vararg operators: Sequence<T>.() -> Sequence<T>) = object : Sequence<T> {
    val i by lazy { this@lazyChain.iterator() }
    val underlying by lazy { operators.fold(emptySequence<T>()) { acc, it -> acc + { it(i.asSequence()) } } + i.asSequence() }

    override fun iterator(): Iterator<T> = underlying.iterator()
}

inline operator fun <T> Sequence<T>.plus(crossinline otherGenerator: () -> Sequence<T>): Sequence<T> =
        this + object : Sequence<T> {
            val actual: Sequence<T> by lazy { otherGenerator() }
            override fun iterator(): Iterator<T> = actual.iterator()
        }