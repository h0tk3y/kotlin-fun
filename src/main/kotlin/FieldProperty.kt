
import com.github.h0tk3y.kotlinFun.util.WeakIdentityHashMap
import kotlin.reflect.KProperty

/**
 * Created by igushs on 4/9/16.
 */

class FieldProperty<R, T : Any>(val initializer: (R) -> T = { throw IllegalStateException("Not initialized.") }) {
    private val map = WeakIdentityHashMap<R, T>()

    operator fun getValue(thisRef: R, property: KProperty<*>): T =
            map[thisRef] ?: setValue(thisRef, property, initializer(thisRef))

    operator fun setValue(thisRef: R, property: KProperty<*>, value: T): T {
        map[thisRef] = value
        return value
    }
}

class NullableFieldProperty<R, T>(val initializer: (R) -> T? = { null }) {
    private val map = WeakIdentityHashMap<R, T>()

    operator fun getValue(thisRef: R, property: KProperty<*>): T? =
            if (thisRef in map) map[thisRef] else setValue(thisRef, property, initializer(thisRef))

    operator fun setValue(thisRef: R, property: KProperty<*>, value: T?): T? {
        map[thisRef] = value
        return value
    }
}

class SynchronizedFieldProperty<R, T : Any>(val initializer: (R) -> T = { throw IllegalStateException("Not initialized.") }) {
    private val map = WeakIdentityHashMap<R, T>()

    operator fun getValue(thisRef: R, property: KProperty<*>): T = synchronized(map) {
        map[thisRef] ?: setValue(thisRef, property, initializer(thisRef))
    }

    private fun set(thisRef: R, value: T): T {
        map[thisRef] = value
        return value
    }

    operator fun setValue(thisRef: R, property: KProperty<*>, value: T): T = synchronized(map) { set(thisRef, value) }
}

class SynchronizedNullableFieldProperty<R, T>(val initializer: (R) -> T? = { null }) {
    private val map = WeakIdentityHashMap<R, T>()

    operator fun getValue(thisRef: R, property: KProperty<*>): T? = synchronized(map) {
        if (thisRef in map) map[thisRef] else set(thisRef, initializer(thisRef))
    }

    private fun set(thisRef: R, value: T?): T? {
        map[thisRef] = value
        return value
    }

    operator fun setValue(thisRef: R, property: KProperty<*>, value: T?): T? = synchronized(map) { set(thisRef, value) }
}