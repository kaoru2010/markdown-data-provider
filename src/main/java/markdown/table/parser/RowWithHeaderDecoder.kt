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
        val index = requireNotNull(headerMap[tag])
        return row[index]
    }

    override fun decodeTaggedEnum(tag: String, enumDescription: SerialDescriptor): Int {
        val index = requireNotNull(headerMap[tag])
        val value = row[index]
        return enumDescription.elementNames().indexOf(value)
    }

    override fun decodeTaggedBoolean(tag: String): Boolean {
        val index = requireNotNull(headerMap[tag])
        val value = row[index]
        return value == "o"
    }

    override fun decodeTaggedNotNullMark(tag: String): Boolean {
        val index = requireNotNull(headerMap[tag])
        val value = row[index]
        return value !in arrayOf("-", "−", "/")
    }
}
