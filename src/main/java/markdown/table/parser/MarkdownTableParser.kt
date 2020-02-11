package markdown.table.parser

import kotlinx.serialization.Decoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.NamedValueDecoder
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.elementNames

typealias DecoderFactory = (headerMap: Map<String, Int>, row: List<String>) -> Decoder

fun <T> List<List<T>>.transpose(): List<List<T>> {
    val numRows = size
    val numColumns = first().size

    return List(numColumns) { row ->
        List(numRows) { column ->
            this[column][row]
        }
    }
}

class MyNamedValueDecoder(
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

fun <T> List<List<String>>.toParamList(serializer: KSerializer<T>, createDecoder: DecoderFactory): List<T> {
    val headerMap = first().mapIndexed { index, head -> head to index }.toMap()

    return asSequence()
        .drop(1)
        .map { serializer.deserialize(createDecoder(headerMap, it)) }
        .toList()
}

fun parseAsMarkdownTable(src: String): List<List<String>> {
    return src.split("\n")
        .asSequence()
        .drop(2)
        .map { it.split(""" *\| *""".toRegex()).filter { it.isNotEmpty() } }
        .filter { it.size >= 2 }
        .toList()
}
