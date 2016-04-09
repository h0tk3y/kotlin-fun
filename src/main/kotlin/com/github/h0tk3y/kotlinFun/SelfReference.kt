package com.github.h0tk3y.kotlinFun

/**
 * Created by igushs on 1/29/16.
 *
 * Provides initializers an ability to reference the variable in lambdas and object expressions.
 */
class SelfReference<T>(val initializer: SelfReference<T>.() -> T) {
    val self: T by lazy {
        inner ?: throw IllegalStateException("Do not use `value` until `initializer` finishes.")
    }

    private val inner = initializer()
}

/**
 * Creates a value of [T] providing its [initializer] a [SelfReference.self] reference to use it in functions.
 *
 * @param initializer An initializer that is able to reference [SelfReference.self].
 * @throws [IllegalStateException] when [SelfReference.self] is used directly and not in lambdas and object expressions.
 */
fun <T> selfReference(initializer: SelfReference<T>.() -> T): T {
    return SelfReference(initializer).self
}