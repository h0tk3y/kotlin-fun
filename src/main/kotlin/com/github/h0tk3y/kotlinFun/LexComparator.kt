package com.github.h0tk3y.kotlinFun

import java.util.*

/**
 * @param T Receiver [Comparator]'s items type and resulting [Comparator]'s [Iterable] item type.
 * @receiver Original [Comparator] to use in lexicographical ordering to compare the items.
 *
 * @return [Comparator] that imposes lexicographical ordering on [Iterable]s of [T].
 * The resulting [Comparator] is consistent with equals when the [Iterable] implementations
 * have [equals]-check based on their items in their iteration order AND the receiver [Comparator] is
 * consistent with equals.
 */
fun <T> Comparator<in T>.lexicographically(): Comparator<in Iterable<T>> = Comparator { o1, o2 ->
    val iterator1 = o1!!.iterator()
    val iterator2 = o2!!.iterator()

    while (true) {
        if (!iterator1.hasNext()) return@Comparator if (!iterator2.hasNext()) 0 else -1
        if (!iterator2.hasNext()) return@Comparator 1

        val item1 = iterator1.next()
        val item2 = iterator2.next()

        val comparison = compare(item1, item2)
        if (comparison != 0)
            return@Comparator comparison
    }

    @Suppress("UNREACHABLE_CODE")
    return@Comparator 0
}