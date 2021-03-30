package app.melon.di

import app.melon.account.di.UserComponent
import dagger.Module

@Module(subcomponents = [UserComponent::class])
class AppSubComponents