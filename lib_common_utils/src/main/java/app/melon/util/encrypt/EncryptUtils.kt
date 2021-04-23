package app.melon.util.encrypt

import java.nio.charset.StandardCharsets


object EncryptUtils {

    private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()

    /**
     * Encrypt with SHA-512
     */
    fun getSHA512HashOfString(toHash: String): String {
        val hasher = SHA512()
        val bytes = toHash.toByteArray(StandardCharsets.UTF_8)
        hasher.update(bytes)
        return hasher.digest().toHexString()
    }

    private fun ByteArray.toHexString(): String {
        val hexChars = CharArray(this.size * 2)
        for (j in this.indices) {
            val v = this[j].toInt() and 0xFF
            hexChars[j * 2] = HEX_ARRAY[v ushr 4]
            hexChars[j * 2 + 1] = HEX_ARRAY[v and 0x0F]
        }
        return String(hexChars)
    }
}