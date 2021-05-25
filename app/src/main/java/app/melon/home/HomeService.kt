package app.melon.home

import android.content.Context
import android.content.Intent
import app.melon.home.api.IHomeService
import app.melon.util.extensions.clearTask
import app.melon.util.extensions.newTask
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class HomeService @Inject constructor(): IHomeService {
    override fun routeToHomepage(context: Context, clearTask: Boolean) {
        var intent = Intent(context, MainActivity::class.java)
        if (clearTask) {
            intent = intent.clearTask().newTask()
        }
        context.startActivity(intent)
    }
}