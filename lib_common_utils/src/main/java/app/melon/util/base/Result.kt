package app.melon.util.base


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<T> {
    open fun get(): T? = null

    fun getOrThrow(): T = when (this) {
        is Success -> get()
        is ErrorResult -> throw throwable
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is ErrorResult -> "Error[throwable=$throwable]"
        }
    }
}

data class Success<T>(val data: T) : Result<T>() {
    override fun get(): T = data
}

data class ErrorResult<T>(val throwable: Throwable) : Result<T>()