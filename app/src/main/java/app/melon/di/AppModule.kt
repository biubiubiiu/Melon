package app.melon.di

import android.content.Context
import app.melon.MelonApplication
import dagger.Module
import dagger.Provides

@Module(includes = [AppModuleBinds::class])
class AppModule {

    @Provides
    fun provideApplicationContext(application: MelonApplication): Context = application.applicationContext
}