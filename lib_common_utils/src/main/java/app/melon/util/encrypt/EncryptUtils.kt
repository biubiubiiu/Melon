package app.melon.util.encrypt

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import kotlin.experimental.and

object EncryptUtils {
    private val HEX_ARRAY = "0123456789abcdef".toCharArray()

    /**
     * Encrypt with SHA-512
     */
    fun getSHA512HashOfString(toHash: String): String {
        var hash = ""
        val digest: MessageDigest
        try {
            digest = MessageDigest.getInstance("SHA-512")
            var bytes = toHash.toByteArray(StandardCharsets.UTF_8)
            digest.update(bytes, 0, bytes.size)
            bytes = digest.digest()
            hash = bytesToHex(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return hash
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v = (bytes[j] and 0xFF.toByte()).toInt()
            hexChars[j * 2] = HEX_ARRAY[v ushr 4]
            hexChars[j * 2 + 1] = HEX_ARRAY[v and 0x0F]
        }
        return String(hexChars)
    }
}