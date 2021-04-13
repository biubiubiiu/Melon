package app.melon.group

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
        setController(controller)
    }

    // you need to override this to prevent NPE of Carousel.setModels()
    @ModelProp
    override fun setModels(models: List<EpoxyModel<*>>) {
        // remove super method because we use Custom Controller for models build.
    }
}