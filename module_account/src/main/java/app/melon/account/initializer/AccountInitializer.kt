package app.melon.account.initializer

import android.app.Application
import app.melon.account.UserManagerImpl
import app.melon.account.api.IAccountService
import app.melon.base.initializer.AppInitializer
import javax.inject.Inject

internal class AccountInitializer @Inject constructor(
    private val service: IAccountService,
    private val userManager: UserManagerImpl
) : AppInitializer {

    override fun init(application: Application) {
        service.registerObserver(userManager)
    }
}