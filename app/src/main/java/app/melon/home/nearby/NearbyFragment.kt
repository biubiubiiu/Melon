package app.melon.home.nearby

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import app.melon.R
import app.melon.data.constants.NEARBY_USER
import app.melon.user.UserPageConfig
import app.melon.user.ui.CommonUserFragment


class NearbyFragment : Fragment(R.layout.common_fragment_container) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.commit {
            replace(
                R.id.fragment_container, CommonUserFragment.newInstance(
                    -1,
                    UserPageConfig(
                        pageType = NEARBY_USER,
                        idPrefix = "nearby_user"
                    )
                )
            )
        }
    }
}
