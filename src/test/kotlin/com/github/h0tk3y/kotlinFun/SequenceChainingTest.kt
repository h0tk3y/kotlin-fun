package com.github.h0tk3y.kotlinFun

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SequenceChainingTest {

    @Test fun prefixModifying() {
        val seq = (1..5).asSequence()
        val result = seq.modifyPrefix { drop(1).take(3).map { -it } }.toList()

        assertEquals(listOf(-2, -3, -4, 5), result)
    }

    @Test fun sequenceChain() {
        val seq = (1..5).asSequence()
        val result = seq.chain(
                { take(1).map { 0 } },
                { sequenceOf(-1) },
                { take(2).map { -it } },
                { map { it * 2 } }
        ).toList()
        assertEquals(listOf(0, -1, -2, -3, 8, 10), result)
    }

    @Test fun notTakingIterator() {
        val seq = (1..5).asSequence().constrainOnce()
        seq.chain({ this })

        seq.toList()
    }

    @Test fun lazyPlus() {
        fun primes(): Sequence<Int> {
            fun primesFilter(from: Sequence<Int>): Sequence<Int> = with(from.iterator()) {
                val current = next()
                sequenceOf(current) + { primesFilter(asSequence().filter { it % current != 0 }) }
            }
            return primesFilter((2..Int.MAX_VALUE).asSequence())
        }

        val result = primes().take(13).sum()
        assertEquals(238, result)
    }

    @Test fun lazySequenceChain() {
        var called1 = false
        var called2 = false
        var called3 = false

        val seq = (1..5).asSequence()
        val result = seq.lazyChain(
                { called1 = true; take(1).map { 0 } },
                { called2 = true; sequenceOf(-1) },
                { called3 = true; take(2).map { -it } }
        ).take(2).toList()

        assertEquals(listOf(0, -1), result)
        assertTrue(called1 && called2 && !called3)
    }

}