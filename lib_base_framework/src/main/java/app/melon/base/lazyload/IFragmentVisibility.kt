package app.melon.base.lazyload


/**
 * Taken from: https://github.com/TaylorKunZhang/fragment-visibility
 */
internal interface IFragmentVisibility {

    /**
     * if the fragment is currently visible to the user
     */
    val isVisibleToUser: Boolean

    /**
     * Called when the fragment is visible to user
     */
    fun onVisible() {}

    /**
     * Called when the Fragment is not visible to user
     */
    fun onInvisible() {}

    /**
     * Called when the fragment is visible for the first time
     */
    fun onFirstVisible() {}

    /**
     * Called when the fragment is visible after first time
     */
    fun onPostFirstVisible() {}
}