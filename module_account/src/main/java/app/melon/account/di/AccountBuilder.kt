package app.melon.account.di

import app.melon.account.login.LoginActivity
import app.melon.account.signup.SignUpReviewStepActivity
import app.melon.account.signup.SignUpStepFormActivity
import app.melon.base.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
internal abstract class AccountBuilder {
    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun injectLoginActivity(): LoginActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun injectSignUpBasicFormActivity(): SignUpStepFormActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun injectSignUpReviewStepActivity(): SignUpReviewStepActivity
}