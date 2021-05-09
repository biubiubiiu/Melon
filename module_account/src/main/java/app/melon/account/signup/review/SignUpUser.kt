package app.melon.account.signup.review

import app.melon.account.api.IAccountService
import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


internal class SignUpUser @Inject constructor(
    private val service: IAccountService
) : SuspendingWorkInteractor<SignUpUser.Params, Result<Boolean>>() {

    internal val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: Params): Result<Boolean> {
        loadingState.addLoader()
        val result = runCatching {
            val signUpSuccess = withContext(Dispatchers.IO) {
                service.registerUser(
                    username = params.username,
                    password = params.password
                )
            }
            signUpSuccess
        }.fold(
            onSuccess = {
                Result.success(it)
            },
            onFailure = {
                Result.failure(it)
            }
        )
        loadingState.removeLoader()
        return result
    }

    internal data class Params(
        val username: String,
        val password: String
    )
}