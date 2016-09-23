package com.github.h0tk3y.kotlinFun

import java.util.*

class ComparatorContext<T>(val comparator: Comparator<T>) : Comparator<T> by comparator {
    operator fun T.compareTo(other: T) = comparator.compare(this, other)
}

inline fun <T, R> Comparator<in T>.withOperators(action: ComparatorContext<in T>.() -> R) = ComparatorContext(this).action()

@Deprecated("Should not be called for Comparator<Comparable<R>>.", level = DeprecationLevel.ERROR)
@JvmName("withOperators\$Comparable")
fun <R, T : Comparable<R>> Comparator<T>.withOperators(action: ComparatorContext<T>.() -> Unit): Nothing {
    throw IllegalArgumentException("withOperators cannot be used with comparators that override natural order.")
}