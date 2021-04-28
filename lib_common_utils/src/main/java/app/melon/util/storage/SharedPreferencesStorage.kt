package app.melon.util.storage

import android.content.Context
import javax.inject.Inject

class SharedPreferencesStorage @Inject constructor(
    override val name: String,
    context: Context
) : Storage {

    private val sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    override fun setString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun getString(key: String): String {
        return sharedPreferences.getString(key, "")!!
    }

    override fun registerOnDataChangeListener(listener: (String) -> Unit) {
        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            listener.invoke(key)
        }
    }
}
