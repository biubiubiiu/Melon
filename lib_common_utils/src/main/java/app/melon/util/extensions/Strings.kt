package app.melon.util.extensions

inline fun String.ifNotBlank(block: (String) -> Unit) {
    if (this.isNotBlank()) {
        block(this)
    }
}

inline fun String.ifNotEmpty(block: (String) -> Unit) {
    if (this.isNotEmpty()) {
        block(this)
    }
}