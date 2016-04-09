/**
 * Created by igushs on 2/2/16.
 */

operator fun <T> Sequence<T>.plus(otherGenerator: () -> Sequence<T>): Sequence<T> =
        object : Sequence<T> {
            private val thisIterator: Iterator<T> by lazy { this@plus.iterator() }
            private val otherIterator: Iterator<T> by lazy { otherGenerator().iterator() }

            override fun iterator() = object : Iterator<T> {
                override fun next(): T =
                        if (thisIterator.hasNext())
                            thisIterator.next()
                        else
                            otherIterator.next()

                override fun hasNext(): Boolean =
                        thisIterator.hasNext() || otherIterator.hasNext()
            }
        }

fun primes(): Sequence<Int> {
    fun primesFilter(from: Sequence<Int>): Sequence<Int> = from.iterator().let {
        val current = it.next()
        sequenceOf(current) + { primesFilter(it.asSequence().filter { it % current != 0 }) }
    }
    return primesFilter((2..Int.MAX_VALUE).asSequence())
}