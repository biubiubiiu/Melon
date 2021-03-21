package app.melon.util

import kotlinx.coroutines.CoroutineDispatcher

/**
 * source: https://github.com/chrisbanes/tivi
 */
data class AppCoroutineDispatchers(
    val io: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
    val main: CoroutineDispatcher
)