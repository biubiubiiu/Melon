package app.melon.comment

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class CommentReplyActivity : DaggerAppCompatActivity() {

    @Inject internal lateinit var factory: CommentControllerDelegate.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_reply)
    }
}