package app.melon.util.storage

interface Storage {

    val name: String

    fun setString(key: String, value: String)
    fun getString(key: String): String

    fun registerOnDataChangeListener(listener: (String) -> Unit)
}
