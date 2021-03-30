package app.melon.account.signup.data

import app.melon.account.service.AccountApiService
import app.melon.account.signup.state.SignUpResult
import app.melon.util.base.ErrorResult
import app.melon.util.base.Result
import app.melon.util.base.Success
import app.melon.util.encrypt.EncryptUtils
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class SignUpDataSource @Inject constructor(
    private val service: AccountApiService
) {

    suspend fun signUp(username: String, password: String): Result<Boolean> {
//        val response = withContext(Dispatchers.IO) {
//            service.register(
//                username = username,
//                password = EncryptUtils.getSHA512HashOfString(password)
//            ).execute()
//        }
//        return when {
//            response.isSuccessful && response.body()?.code == 200 -> Success(true)
//            else -> ErrorResult(HttpException(response))
//        }
        return Success(true)
    }
}