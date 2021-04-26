package app.melon.composer.di

import app.melon.base.scope.FragmentScope
import app.melon.composer.selectlocation.SelectLocationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ComposerBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun injectLocationFragment(): SelectLocationFragment
}