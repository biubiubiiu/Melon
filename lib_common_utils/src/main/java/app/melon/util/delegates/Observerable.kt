package app.melon.util.delegates

import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * source: https://github.com/chrisbanes/tivi
 */
inline fun <T> observable(
    initialValue: T,
    crossinline onChange: () -> Unit
): ReadWriteProperty<Any?, T> = object : ObservableProperty<T>(initialValue) {
    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) = onChange()
}