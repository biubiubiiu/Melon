package app.melon.account.login.data

import app.melon.account.service.AccountApiService
import app.melon.util.base.ErrorResult
import app.melon.util.base.Result
import app.melon.util.base.Success
import app.melon.util.encrypt.EncryptUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource @Inject constructor(
    private val service: AccountApiService
) {

    suspend fun login(username: String, password: String): Result<String> {
        return try {
//            val response = withContext(Dispatchers.IO) {
//                service.login(
//                    username = username,
//                    password = EncryptUtils.getSHA512HashOfString(password)
//                ).execute()
//            }
//            when {
//                response.isSuccessful && response.body()?.code == 200 -> {
//                    val token = response.body()?.result?.token
//                    if (token != null) {
//                        Success(token)
//                    } else {
//                        ErrorResult(HttpException(response))
//                    }
//                }
//                else -> return ErrorResult(HttpException(response))
//            }
            Success("Fake Token")
        } catch (e: Throwable) {
            ErrorResult(IOException("Error logging in", e))
        }
    }

    suspend fun logout(): Result<Any> {
        // TODO: revoke authentication
//        return withContext(Dispatchers.IO) {
//            service.logout()
//        }
        return Success(Any())
    }
}