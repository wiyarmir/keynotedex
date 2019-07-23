package es.guillermoorellana.keynotedex.backend.frontmatter

import org.junit.Test

class FrontMatterParserTest {
    @Test
    fun testComplete() {
        val bufferedReader = javaClass.getResourceAsStream("/frontmatter/complete.md")
            .bufferedReader()

        val conference = FrontMatterParser.parseConference(bufferedReader)
    }

    @Test
    fun testFromString() {
        val text = javaClass.getResource("/frontmatter/complete.md").readText()

        val conference = FrontMatterParser.parseConference(text)
    }

    @Test
    fun testMinimal() {
        val bufferedReader = javaClass.getResourceAsStream("/frontmatter/minimal.md")
            .bufferedReader()

        val conference = FrontMatterParser.parseConference(bufferedReader)
    }
}
