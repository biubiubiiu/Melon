package app.melon.im.initializer

import android.app.Application
import android.util.Log
import app.melon.account.api.IAccountService
import app.melon.base.initializer.AppInitializer
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.api.BasicCallback
import javax.inject.Inject

internal class IMInitializer @Inject constructor(
    private val service: IAccountService
) : AppInitializer {

    override fun init(application: Application) {


        service.registerObserver(object : IAccountService.Observer {
            override suspend fun onLoginSuccess() {
//                val username = service.savedUsername
//                val password = service.savedPassword
                val username = "raymond"
                val password = "raymond0719"

                // TODO should wait until JMessageClient initialized
                JMessageClient.login(username, password, object : BasicCallback() {
                    override fun gotResult(responseCode: Int, responseMessage: String?) {
                        Log.d("raymond", "JMessage Login: $responseCode - $responseMessage")
                    }
                })
            }

            override suspend fun onLoginCancel() {
            }

            override suspend fun onLoginFailure() {
            }

            override suspend fun onLogout() {
            }
        })
    }
}