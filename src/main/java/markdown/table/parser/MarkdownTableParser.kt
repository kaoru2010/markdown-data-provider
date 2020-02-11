package markdown.table.parser

import kotlinx.serialization.Decoder
import kotlinx.serialization.KSerializer

typealias DecoderFactory = (headerMap: Map<String, Int>, row: List<String>) -> Decoder

/**
 * マトリックスの行と列を入れ替える
 */
fun <T> List<List<T>>.transpose(): List<List<T>> {
    val numRows = size
    val numColumns = first().size

    return List(numColumns) { row ->
        List(numRows) { column ->
            this[column][row]
        }
    }
}

/**
 * マトリックスをParamリストに変換する
 */
fun <T> List<List<String>>.toParamList(serializer: KSerializer<T>, createDecoder: DecoderFactory): List<T> {
    val headerMap = first().mapIndexed { index, head -> head to index }.toMap()

    return asSequence()
        .drop(1)
        .map { serializer.deserialize(createDecoder(headerMap, it)) }
        .toList()
}

/**
 * マークダウン形式の文字列をパースしてマトリックスを得る
 */
fun parseAsMarkdownTable(src: String): List<List<String>> {
    return src.split("\n")
        .asSequence()
        .drop(2)
        .map { it.split(""" *\| *""".toRegex()).filter { it.isNotEmpty() } }
        .filter { it.size >= 2 }
        .toList()
}
