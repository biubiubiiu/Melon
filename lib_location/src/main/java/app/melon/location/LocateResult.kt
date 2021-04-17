package app.melon.location

sealed class LocateResult

class LocateSuccess(

    /**
     * longitude under GCJ-02 coordinate
     */
    val longitude: Double,

    /**
     * latitude under GCJ-02 coordinate
     */
    val latitude: Double,

    /**
     * the estimated  accuracy of this location, unit: meter
     */
    val accuracy: Float,

    /**
     * point of interest name
     */
    val poiName: String
) : LocateResult() {
    override fun toString(): String {
        return "[longitude]: $longitude;" +
                "[latitude]: $latitude;" +
                "[accuracy]: $accuracy;" +
                "[poiName]: $poiName"
    }
}

class LocateFail(
    private val errorCode: Int,
    private val errorInfo: String
) : LocateResult() {
    val errorMessage get() = "[$errorCode]: $errorInfo"

    override fun toString(): String = errorMessage
}
