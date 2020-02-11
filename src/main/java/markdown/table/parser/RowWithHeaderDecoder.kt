package markdown.table.parser

import kotlinx.serialization.NamedValueDecoder
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.elementNames

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
        return enumDescription.elementNames().indexOf(value)
    }

    override fun decodeTaggedBoolean(tag: String): Boolean {
        val value = getValue(tag)
        return value == "o"
    }

    override fun decodeTaggedNotNullMark(tag: String): Boolean {
        val value = getValue(tag)
        return value !in arrayOf("-", "−", "/")
    }

    private fun getValue(tag: String): String {
        val index = requireNotNull(headerMap[tag])
        return row[index]
    }
}
