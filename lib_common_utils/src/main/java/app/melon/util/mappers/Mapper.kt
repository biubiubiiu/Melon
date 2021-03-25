package app.melon.util.mappers


/**
 * source: https://github.com/chrisbanes/tivi
 */
interface Mapper<F, T> {
    suspend fun map(from: F): T
}

interface IndexedMapper<F, T> {
    suspend fun map(index: Int, from: F): T
}