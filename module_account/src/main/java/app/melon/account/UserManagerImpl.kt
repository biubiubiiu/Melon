package app.melon.account

import app.melon.account.api.IAccountService
import app.melon.account.api.UserManager
import app.melon.account.data.AccountApiService
import app.melon.account.data.mapper.RemoteUserDetailToUser
import app.melon.data.MelonDatabase
import app.melon.data.entities.User
import app.melon.data.util.mergeUser
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class UserManagerImpl @Inject constructor(
    private val service: AccountApiService,
    private val mapper: RemoteUserDetailToUser,
    private val database: MelonDatabase
) : UserManager(), IAccountService.Observer {

    private val currentUserIdFlow = MutableSharedFlow<String?>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    // in-memory cache of the logged in User object
    private var currentUser: User? = null

    override val user: User?
        get() = currentUser

    @OptIn(FlowPreview::class)
    override fun observeUser(): Flow<User?> {
        return currentUserIdFlow
            .onStart { emit(null) }
            .flatMapMerge {
                when (it) {
                    null -> flowOf(null)
                    else -> database.userDao().getUserWithIdFlow(it)
                }
            }
            .distinctUntilChanged()
            .shareIn(
                GlobalScope,
                replay = 1,
                started = SharingStarted.WhileSubscribed()
            )
    }

    init {
        GlobalScope.launch {
            observeUser().collectLatest { user ->
                currentUser = user
            }
        }
    }

    override suspend fun onLoginSuccess() {
        val user = fetchUserDetail() ?: return
        database.runWithTransaction {
            val localUser = database.userDao().getUserWithId(user.id) ?: User()
            database.userDao().insertOrUpdate(mergeUser(localUser, user))
        }
        currentUserIdFlow.tryEmit(user.id)
    }

    override suspend fun onLoginCancel() {
    }

    override suspend fun onLoginFailure() {
    }

    override suspend fun onLogout() {
        currentUserIdFlow.tryEmit(null)
    }

    private suspend fun fetchUserDetail(): User? {
        val response = withContext(Dispatchers.IO) {
            service.fetchUserDetail()
                .executeWithRetry()
                .toResult()
                .getOrThrow()
        }
        if (!response.isSuccess) {
            return null
        }
        val data = response.data ?: return null
        return withContext(Dispatchers.Default) {
            mapper.map(data)
        }
    }
}
