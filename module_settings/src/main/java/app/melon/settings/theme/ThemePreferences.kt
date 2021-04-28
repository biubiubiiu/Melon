package app.melon.settings.theme

import kotlinx.coroutines.flow.Flow

interface ThemePreferences {

    var theme: Theme
    fun observeTheme(): Flow<Theme>

    fun notifyUpdate()

    enum class Theme {
        LIGHT, DARK, SYSTEM
    }
}