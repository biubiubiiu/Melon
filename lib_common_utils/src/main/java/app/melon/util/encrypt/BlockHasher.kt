package app.melon.util.encrypt

import kotlin.math.min

abstract class BlockHasher(blockLen: Int) {

    protected val block: ByteArray = ByteArray(blockLen)

    protected var blockFilled: Int = 0
    protected var length: Long = 0

    @JvmOverloads
    fun update(b: ByteArray, off: Int = 0, len: Int = b.size) {
        var currOffset = off
        var remainLen = len
        val blockLen = block.size
        length += remainLen.toLong()
        if (blockFilled > 0) {
            val n = min(blockLen - blockFilled, remainLen)
            System.arraycopy(b, currOffset, block, blockFilled, n)
            blockFilled += n
            if (blockFilled == blockLen) {
                compress(block, 0, blockLen)
                currOffset += n
                remainLen -= n
            } else return
        }
        if (remainLen >= blockLen) {
            val n = remainLen / blockLen * blockLen
            compress(b, currOffset, n)
            currOffset += n
            remainLen -= n
        }
        System.arraycopy(b, currOffset, block, 0, remainLen)
        blockFilled = remainLen
    }

    fun digest(): ByteArray {
        return engineDigest()
    }

    protected abstract fun compress(msg: ByteArray, off: Int, len: Int)
    protected abstract fun engineDigest(): ByteArray
}