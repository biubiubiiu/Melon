package app.melon.user.interactor

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.user.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


internal class UpdateAvatar @Inject constructor(
    private val repo: UserRepository
) : SuspendingWorkInteractor<UpdateAvatar.Params, Result<String>>() {

    val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: Params): Result<String> {
        loadingState.addLoader()
        return withContext(Dispatchers.IO) {
            repo.updateAvatar(params.file)
        }.also { loadingState.removeLoader() }
    }

    internal data class Params(
        val file: File
    )
}