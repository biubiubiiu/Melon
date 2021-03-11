package app.melon.home.recommend.groups

import android.content.Context
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView

/**
 * source: https://github.com/enginebai/MovieHunt
 */

@ModelView(saveViewState = true, autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class GroupCarousel(context: Context) : Carousel(context) {

    override fun getSnapHelperFactory(): SnapHelperFactory? {
        return null
    }

    @ModelProp(ModelProp.Option.DoNotHash)
    fun setEpoxyController(controller: EpoxyController) {
//        setControllerAndBuildModels(controller) // TODO open this line
    }

    // you need to override this to prevent NPE of Carousel.setModels()
    @ModelProp
    override fun setModels(models: List<EpoxyModel<*>>) {
        // remove super method because we use PagedController for models build.
        super.setModels(models)
    }
}