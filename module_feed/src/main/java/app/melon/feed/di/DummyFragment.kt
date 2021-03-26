package app.melon.feed.di

import app.melon.feed.FeedControllerDelegate
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class DummyFragment : DaggerFragment() {

    @Inject internal lateinit var dummyFactory: FeedControllerDelegate.Factory
}