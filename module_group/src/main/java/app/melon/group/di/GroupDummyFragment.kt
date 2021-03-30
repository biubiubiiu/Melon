package app.melon.group.di

import app.melon.group.GroupRenderer
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class GroupDummyFragment : DaggerFragment() {

    @Inject internal lateinit var dummyFactory: GroupRenderer.Factory
}