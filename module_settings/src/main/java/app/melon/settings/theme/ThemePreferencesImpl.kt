package app.melon.settings.theme

import android.content.Context
import app.melon.settings.R
import app.melon.util.storage.ApplicationStorage
import app.melon.util.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


/**
 * Source: https://github.com/chrisbanes/tivi/blob/main/ui-settings/src/main/java/app/tivi/settings/TiviPreferencesImpl.kt
 */
class ThemePreferencesImpl @Inject constructor(
    private val context: Context,
    @ApplicationStorage private val storage: Storage
) : ThemePreferences {

    private val preferenceKeyChangedFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)

    override fun notifyUpdate() {
        preferenceKeyChangedFlow.tryEmit(KEY_THEME)
    }

    override var theme: ThemePreferences.Theme
        get() = storage.getString(KEY_THEME).themeValue
        set(value) = storage.setString(KEY_THEME, value.storageKey)

    override fun observeTheme(): Flow<ThemePreferences.Theme> {
        return preferenceKeyChangedFlow
            .onStart { emit(KEY_THEME) }
            .filter { it == KEY_THEME }
            .map { theme }
            .distinctUntilChanged()
    }

    private val ThemePreferences.Theme.storageKey: String
        get() = when (this) {
            ThemePreferences.Theme.DARK -> context.getString(R.string.settings_theme_pref_dark)
            ThemePreferences.Theme.LIGHT -> context.getString(R.string.settings_theme_pref_light)
            ThemePreferences.Theme.SYSTEM -> context.getString(R.string.settings_theme_pref_system)
        }

    private val String.themeValue: ThemePreferences.Theme
        get() = when (this) {
            context.getString(R.string.settings_theme_pref_dark) -> ThemePreferences.Theme.DARK
            context.getString(R.string.settings_theme_pref_light) -> ThemePreferences.Theme.LIGHT
            else -> ThemePreferences.Theme.SYSTEM
        }

    companion object {
        private const val KEY_THEME = "key_theme"
    }
}