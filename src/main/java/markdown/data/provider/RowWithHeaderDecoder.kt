package markdown.data.provider

import kotlinx.serialization.NamedValueDecoder
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.getElementIndexOrThrow

/**
 * ヘッダーと行からdata classへのデコーダー
 */
class RowWithHeaderDecoder(
    private val headerMap: Map<String, Int>,
    private val row: List<String>
) : NamedValueDecoder() {
    override fun decodeTaggedString(tag: String): String {
        return getValue(tag)
    }

    override fun decodeTaggedEnum(tag: String, enumDescription: SerialDescriptor): Int {
        val value = getValue(tag)
        return enumDescription.getElementIndexOrThrow(value)
    }

    override fun decodeTaggedBoolean(tag: String): Boolean {
        val value = getValue(tag)
        return value == "o"
    }

    override fun decodeTaggedNotNullMark(tag: String): Boolean {
        val value = getValue(tag)
        return value !in arrayOf("-", "−", "/")
    }

    override fun decodeTaggedInt(tag: String): Int {
        val value = getValue(tag)
        return value.toInt()
    }

    override fun decodeTaggedLong(tag: String): Long {
        val value = getValue(tag)
        return value.toLong()
    }

    override fun decodeTaggedByte(tag: String): Byte {
        val value = getValue(tag)
        return value.toByte()
    }

    override fun decodeTaggedFloat(tag: String): Float {
        val value = getValue(tag)
        return value.toFloat()
    }

    override fun decodeTaggedDouble(tag: String): Double {
        val value = getValue(tag)
        return value.toDouble()
    }

    override fun decodeTaggedShort(tag: String): Short {
        val value = getValue(tag)
        return value.toShort()
    }

    private fun getValue(tag: String): String {
        val index = requireNotNull(headerMap[tag])
        return row[index]
    }
}
