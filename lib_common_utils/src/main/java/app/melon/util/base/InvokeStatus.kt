package app.melon.util.base

/**
 * source: https://github.com/chrisbanes/tivi
 */
sealed class InvokeStatus

object InvokeIdle : InvokeStatus()
object InvokeStarted : InvokeStatus()

object InvokeSuccess : InvokeStatus()

data class InvokeError(val throwable: Throwable) : InvokeStatus()
object InvokeTimeout : InvokeStatus()