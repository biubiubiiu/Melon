package app.melon.account.di

import app.melon.account.LoggerUserScope
import dagger.Subcomponent

@LoggerUserScope
@Subcomponent
interface UserComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): UserComponent
    }
}