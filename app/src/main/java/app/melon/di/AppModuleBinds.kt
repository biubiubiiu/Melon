package app.melon.di

import android.app.Application
import app.melon.MelonApplication
import app.melon.base.initializer.AppInitializer
import app.melon.initializers.CoilInitializer
import app.melon.initializers.ThemeInitializer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
abstract class AppModuleBinds {

    @Binds
    abstract fun bindApplication(application: MelonApplication): Application

    @Binds
    @IntoSet
    abstract fun bindThemeInitialzer(initializer: ThemeInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun bindCoilInitialzer(initializer: CoilInitializer): AppInitializer
}