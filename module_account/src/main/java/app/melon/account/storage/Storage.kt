package app.melon.account.storage

interface Storage {
    fun setString(key: String, value: String)
    fun getString(key: String): String
}
