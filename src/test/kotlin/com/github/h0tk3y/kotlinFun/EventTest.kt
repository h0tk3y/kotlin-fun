package com.github.h0tk3y.kotlinFun

import org.junit.Assert.assertEquals
import org.junit.Test

class EventTest {
    @Test fun singleHandler() {
        val e = event1<String>()

        var calledWith: String? = null
        val handler: (String) -> Unit = { calledWith = it }

        e += handler
        e("called once")

        assertEquals("called once", calledWith)

        e -= handler
        e("called again")

        assertEquals("called once", calledWith)
    }

    @Test fun multipleHandlers() {
        val e = event1<Int>()

        var calls = 1
        e.addHandler { calls += it }
        e.addHandler { calls *= it }

        e(3)

        assertEquals(12, calls)
    }

    @Test fun handleOnce() {
        val e = event0()

        var calls = 0
        e.handleOnce { calls++ }
        e.handleOnce { calls++ }

        e.trigger()

        e.handleOnce { calls++ }

        e.trigger()

        assertEquals(3, calls)
    }
}