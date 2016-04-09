/**
 * Created by igushs on 1/31/16.
 */

fun main(args: Array<String>) {
    val seq = (1..10).asSequence()

    seq.iterator().let { iter ->
        iter.asSequence().take(5).map { it * -1 } + iter.asSequence()
    }.forEach { println(it) }

    seq.chain(
            { take(3).map { it * -1 } },
            { drop(1).take(3).map { it * 100 } },
            { map { 0 } }
    ).forEach { println(it) }
}

fun <T : R, R> Sequence<T>.changePrefix(
        operator: Sequence<T>.() -> Sequence<R>
): Sequence<R> {
    val i = iterator()
    return i.asSequence().operator() + i.asSequence()
}

fun <T> Sequence<T>.chain(vararg operators: Sequence<T>.() -> Sequence<T>): Sequence<T> {
    val i = iterator()
    return operators.fold(emptySequence<T>()) { acc, it -> acc + i.asSequence().it() } + i.asSequence()
}

fun <T> Sequence<T>.lazyChain(vararg operators: Sequence<T>.() -> Sequence<T>): Sequence<T> {
    val i = iterator()
    return operators.fold(emptySequence<T>()) { acc, it -> acc + { i.asSequence().it() } } + i.asSequence()
}
