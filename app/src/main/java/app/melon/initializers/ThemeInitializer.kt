package app.melon.initializers

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import app.melon.base.initializer.AppInitializer
import app.melon.settings.theme.ThemePreferences
import javax.inject.Inject

class ThemeInitializer @Inject constructor(
    private val preferences: ThemePreferences
) : AppInitializer {

    override fun init(application: Application) {
        updateThemePreference(preferences.theme)
    }

    private fun updateThemePreference(theme: ThemePreferences.Theme) = when (theme) {
        ThemePreferences.Theme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        ThemePreferences.Theme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ThemePreferences.Theme.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}