package app.melon.account.signup.review

import app.melon.account.signup.data.SignUpDataSource
import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.util.base.Result
import app.melon.util.encrypt.EncryptUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SignUpUser @Inject constructor(
    private val dataSource: SignUpDataSource
) : SuspendingWorkInteractor<SignUpUser.Params, Result<Boolean>>() {

    val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: Params): Result<Boolean> {
            loadingState.addLoader()
        return withContext(Dispatchers.IO) {
            dataSource.signUp(
                username = params.username,
                password = EncryptUtils.getSHA512HashOfString(params.password)
            )
        }.also { loadingState.removeLoader() }
    }

    data class Params(
        val username: String,
        val password: String
    )
}