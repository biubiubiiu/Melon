package app.melon.util.extensions

fun <E> MutableList<E>.addIf(predicate: () -> Boolean, element: E): MutableList<E> {
    if (predicate.invoke()) {
        add(element)
    }
    return this
}