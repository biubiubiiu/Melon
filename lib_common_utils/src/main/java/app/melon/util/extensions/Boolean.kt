package app.melon.util.extensions

fun Boolean.ifTrue(block: () -> Unit) {
    if (this) {
        block()
    }
}

fun Boolean.ifFalse(block: () -> Unit) {
    if (!this) {
        block()
    }
}