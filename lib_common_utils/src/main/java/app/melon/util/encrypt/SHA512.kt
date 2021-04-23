package app.melon.util.encrypt

import java.util.Arrays


class SHA512 : BlockHasher(128) {

    companion object {

        @JvmStatic
        private external fun compress(state: LongArray, msg: ByteArray, off: Int, len: Int): Boolean

        init {
            System.loadLibrary("encryptor")
        }
    }

    private val state: LongArray = longArrayOf(
        0x6A09E667F3BCC908L,
        -0x4498517a7b3558c5L,
        0x3C6EF372FE94F82BL,
        -0x5ab00ac5a0e2c90fL,
        0x510E527FADE682D1L,
        -0x64fa9773d4c193e1L,
        0x1F83D9ABFB41BD6BL,
        0x5BE0CD19137E2179L
    )

    override fun compress(msg: ByteArray, off: Int, len: Int) {
        if (!compress(state, msg, off, len)) throw RuntimeException("Native call failed")
    }

    override fun engineDigest(): ByteArray {
        block[blockFilled] = 0x80.toByte()
        blockFilled++
        Arrays.fill(block, blockFilled, block.size, 0.toByte())
        if (blockFilled + 16 > block.size) {
            compress(block, 0, block.size)
            Arrays.fill(block, 0.toByte())
        }
        length = length shl 3
        for (i in 0..7) block[block.size - 1 - i] = (length ushr i * 8).toByte()
        compress(block, 0, block.size)
        val result = ByteArray(state.size * 8)
        for (i in result.indices) result[i] = (state[i / 8] ushr 56 - i % 8 * 8).toByte()
        return result
    }
}