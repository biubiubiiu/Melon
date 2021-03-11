package app.melon.di

import android.app.Application
import app.melon.MelonApplication
import dagger.Binds
import dagger.Module

@Module
abstract class AppModuleBinds {

    @Binds
    abstract fun provideApplication(bind: MelonApplication): Application
}