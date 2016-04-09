import com.github.h0tk3y.kotlinFun.util.WeakIdentityHashMap
import java.util.*
import kotlin.reflect.KProperty

/**
 * Created by igushs on 4/9/16.
 */

class FieldProperty<R, T : Any>(val synchronized: Boolean = false,
                                val initializer: (R) -> T = { throw IllegalStateException("Not initialized.") }) {
    val map = WeakIdentityHashMap<R, T>().let { if (synchronized) Collections.synchronizedMap(it) else it }

    operator fun getValue(thisRef: R, property: KProperty<*>): T =
            map[thisRef] ?: setValue(thisRef, property, initializer(thisRef))

    operator fun setValue(thisRef: R, property: KProperty<*>, value: T): T {
        map[thisRef] = value
        return value
    }
}

class NullableFieldProperty<R, T>(val synchronized: Boolean = false,
                                  val initializer: (R) -> T? = { null }) {
    val map = WeakIdentityHashMap<R, T>().let { if (synchronized) Collections.synchronizedMap(it) else it }

    operator fun getValue(thisRef: R, property: KProperty<*>): T? =
            map[thisRef] ?: setValue(thisRef, property, initializer(thisRef))

    operator fun setValue(thisRef: R, property: KProperty<*>, value: T?): T? {
        map[thisRef] = value
        return value
    }
}