package com.github.h0tk3y.kotlinFun

import com.github.h0tk3y.kotlinFun.selfReference
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.*

/**
 * Created by igushs on 1/31/16.
 *
 * Tests for [SelfReference] class.
 */

class SelfReferenceTest {
    class SomeHolder(var x: Int,
                     val action: () -> Unit)

    @Test fun positive() {
        val expected = 100

        val s: SomeHolder = selfReference { SomeHolder(0) { self.x = expected } }
        s.action()
        assertEquals(expected, s.x)
    }

    @Test(expected = IllegalStateException::class)
    fun negative() {
        selfReference<SomeHolder> { SomeHolder(self.x) { }; }
    }

    @Test
    fun lambda() {
        class Holder(val actions: MutableSet<Runnable> = HashSet())

        val h = Holder()
        h.actions.add(selfReference { Runnable { h.actions.remove(self) } })
        h.actions.add(selfReference { Runnable { h.actions.remove(self) } })
        h.actions.toList().forEach { it.run() }
        assertTrue(h.actions.isEmpty())
    }
}