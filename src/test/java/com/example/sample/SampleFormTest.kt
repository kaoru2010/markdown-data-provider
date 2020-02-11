package com.example.sample

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import markdown.table.parser.RowWithHeaderDecoder
import markdown.table.parser.parseMarkdownTable
import markdown.table.parser.toParamList
import markdown.table.parser.transpose
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

private val MARKDOWN_TABLE = """
| input/output     | 0     | 1       | 2       | 3       | 4     | 5     | 6     |
|------------------|-------|---------|---------|---------|-------|-------|-------|
| <INPUT>          |       |         |         |         |       |       |       |
| email            | valid | valid   | valid   | valid   | valid | valid | blank |
| emailConfirm     | valid | valid   | invalid | invalid | valid | blank | valid |
| password         | valid | invalid | valid   | invalid | blank | valid | valid |
| <OUTPUT>         |       |         |         |         |       |       |       |
| canSubmit        | o     | o       | o       | o       | x     | x     | x     |
| showsErrorDialog | x     | o       | o       | o       | -     | -     | -     |
""".trimIndent()

@RunWith(Parameterized::class)
class SampleFormTest(
    private val param: Param
) {

    private val testTarget = SampleForm()

    @Before
    fun setUp() {
        testTarget.apply {
            email = param.email?.value
            emailConfirm = param.emailConfirm?.value
            password = param.password?.value
        }
    }

    @Test
    fun test() {
        assert(testTarget.canSubmit() == param.canSubmit)
        param.showsErrorDialog?.let {
            assert(testTarget.showsErrorDialog() == it)
        }
    }

    @Serializable
    enum class InputDataSet(val value: String) {
        @SerialName("valid")
        VALID("x".repeat(8)),

        @SerialName("invalid")
        INVALID("_"),

        @SerialName("blank")
        BLANK(""),
    }

    @Serializable
    data class Param(
        // input
        val email: InputDataSet?,
        val emailConfirm: InputDataSet?,
        val password: InputDataSet?,

        // output
        val canSubmit: Boolean,
        val showsErrorDialog: Boolean?
    )

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "[{index}] {0}")
        fun data(): List<Any> {
            val matrix = parseMarkdownTable(MARKDOWN_TABLE)
            val transposedMatrix: List<List<String>> = matrix.transpose()
            return transposedMatrix.toParamList(Param.serializer(), ::RowWithHeaderDecoder)
        }
    }
}
