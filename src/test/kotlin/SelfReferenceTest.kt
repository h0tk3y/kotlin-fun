import org.junit.Assert.assertEquals
import org.junit.Test

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
}