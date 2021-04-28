package app.melon.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.melon.settings.theme.ThemePreferences
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


class SettingsListFragment : PreferenceFragmentCompat(), HasAndroidInjector {

    @Inject
    @JvmField
    var androidInjector: DispatchingAndroidInjector<Any>? = null

    @Inject internal lateinit var themePreference: ThemePreferences

    override fun androidInjector(): AndroidInjector<Any> = androidInjector!!

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            themePreference.observeTheme().collectLatest {
                updateThemePreference(it)
            }
        }
    }

    private fun updateThemePreference(theme: ThemePreferences.Theme) = when (theme) {
        ThemePreferences.Theme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        ThemePreferences.Theme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ThemePreferences.Theme.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    private val themeChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        themePreference.notifyUpdate()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = "app"
        addPreferencesFromResource(R.xml.preference_settings)

        findPreference<Preference>("key_theme")
            ?.sharedPreferences
            ?.registerOnSharedPreferenceChangeListener(themeChangeListener)
    }

    override fun onDestroy() {
        findPreference<Preference>("key_theme")
            ?.sharedPreferences
            ?.unregisterOnSharedPreferenceChangeListener(themeChangeListener)
        super.onDestroy()
    }
}