package app.melon.util.number

import android.content.Context
import androidx.core.os.ConfigurationCompat
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow


@Singleton
class MelonNumberFormatter @Inject constructor(
    context: Context
) {

    private val locale = ConfigurationCompat.getLocales(context.resources.configuration).get(0)

    private val suffix = when (locale) {
        Locale.CHINA -> charArrayOf(' ', '万', '亿')
        else -> charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
    }
    private val unit = when (locale) {
        Locale.CHINA -> 4
        else -> 3
    }
    private val smallNumPattern = when (locale) {
        Locale.CHINA -> "###0"
        else -> "##0"
    }
    private val bigNumPattern = when (locale) {
        Locale.CHINA -> "##0.0"
        else -> "#0.0"
    }

    fun format(number: Number): String {
        val numValue = number.toLong()
        val value = floor(log10(numValue.toDouble())).toInt()
        val base = value / unit
        return if (value >= unit && base < suffix.size) {
            DecimalFormat(bigNumPattern).format(numValue / 10f.pow(base * 3f)) + suffix[base]
        } else {
            DecimalFormat(smallNumPattern).format(numValue)
        }
    }
}