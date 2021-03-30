package app.melon.account.di

import app.melon.account.login.LoginActivity
import app.melon.account.signup.SignUpReviewStepActivity
import app.melon.account.signup.SignUpStepFormActivity
import app.melon.base.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class AccountBuilder {
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun injectLoginActivity(): LoginActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun injectSignUpBasicFormActivity(): SignUpStepFormActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun injectSignUpReviewStepActivity(): SignUpReviewStepActivity
}