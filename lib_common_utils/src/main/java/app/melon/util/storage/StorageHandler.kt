package app.melon.util.storage

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.webkit.MimeTypeMap
import app.melon.util.graphics.GraphicsUtil
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StorageHandler @Inject constructor(
    private val context: Context
) {

    fun copyFileToCacheDir(uri: Uri, filename: String = "cache"): File {
        val resolver = context.contentResolver
        val mediaType = resolver.getType(uri)
        val fileExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mediaType)
        val outputFile = File(context.cacheDir, "$filename.$fileExtension")
        resolver.openInputStream(uri).use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input?.read(buffer) ?: -1
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
        return outputFile
    }
}