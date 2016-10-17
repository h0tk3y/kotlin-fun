package com.github.h0tk3y.kotlinFun

import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

class VarWithObservableSetter<T,
        out TWillSet : (() -> Unit)?,
        out TDidSet : (() -> Unit)?>
internal constructor(
        val initialValue: T,
        internal val wilLSet: TWillSet,
        internal val didSet: TDidSet
) : ObservableProperty<T>(initialValue) {
    override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean {
        wilLSet?.invoke()
        return true
    }

    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
        didSet?.invoke()
    }
}

fun <T> varWithObservableSetter(initialValue: T) = VarWithObservableSetter(initialValue, null, null)

fun <T, R : (() -> Unit)?> VarWithObservableSetter<T, Nothing?, R>.willSet(action: () -> Unit) =
        VarWithObservableSetter(initialValue, action, didSet)

fun <T, R : (() -> Unit)?> VarWithObservableSetter<T, R, Nothing?>.didSet(action: () -> Unit) =
        VarWithObservableSetter(initialValue, wilLSet, action)