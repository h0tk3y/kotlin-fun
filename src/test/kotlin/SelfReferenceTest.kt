import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.*
import java.lang.Character.MIN_VALUE as nullChar

/**
 * Created by igushs on 1/31/16.
 *
 * Tests for [SelfReference] class.
 */

fun addInvoker(adder: () -> Int = { add() }): Int {
    val a: (Int) -> Int = { add(it) }
    return adder()
}

fun add(num1: Int = 1, num2: Int = 1): Int {
    return num1 + num2
}

class SelfReferenceTest {
    class SomeHolder(var x: Int,
                     val action: () -> Unit)

    @Test fun positive() {
        val c = nullChar
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