package app.melon.data.mappers


/**
 * source: https://github.com/chrisbanes/tivi
 */
interface Mapper<F, T> {
    suspend fun map(from: F): T
}

interface IndexedMapper<F, T> {
    suspend fun map(index: Int, from: F): T
}