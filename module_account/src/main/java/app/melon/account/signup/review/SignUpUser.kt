package app.melon.account.signup.review

import app.melon.account.api.IAccountService
import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.util.base.Result
import app.melon.util.base.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


internal class SignUpUser @Inject constructor(
    private val service: IAccountService
) : SuspendingWorkInteractor<SignUpUser.Params, Result<Boolean>>() {

    internal val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: Params): Result<Boolean> {
        loadingState.addLoader()
        val signUpSuccess = withContext(Dispatchers.IO) {
            service.registerUser(
                username = params.username,
                password = params.password
            )
        }
        loadingState.removeLoader()
        return Success(signUpSuccess)
    }

    internal data class Params(
        val username: String,
        val password: String
    )
}