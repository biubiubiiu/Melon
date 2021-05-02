package app.melon.account.signup.data

import app.melon.account.data.AccountApiService
import app.melon.util.base.Result
import app.melon.util.base.Success
import javax.inject.Inject

internal class SignUpDataSource @Inject constructor(
    private val service: AccountApiService
) {

    suspend fun signUp(username: String, password: String): Result<Boolean> {
//        return try {
//            val response = withContext(Dispatchers.IO) {
//                service.register(
//                    username = username,
//                    password = EncryptUtils.getSHA512HashOfString(password)
//                )
//                    .executeWithRetry()
//            }
//            when {
//                response.isSuccessful && response.body()?.code == 200 -> Success(true)
//                else -> ErrorResult(HttpException(response))
//            }
//        } catch (e: Exception) {
//            ErrorResult(e)
//        }

        return Success(true)
    }
}