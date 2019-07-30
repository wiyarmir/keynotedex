package es.guillermoorellana.keynotedex.backend.frontmatter

import es.guillermoorellana.keynotedex.backend.external.frontmatter.Conference
import org.junit.Test

class FrontMatterParserTest {
    @Test
    fun testComplete() {
        val bufferedReader = javaClass.getResourceAsStream("/frontmatter/complete.md")
            .bufferedReader()
            .readText()

        val parser = FrontMatterParser()

        val conference = parser.parse(Conference.serializer(), bufferedReader)
    }


    @Test
    fun testMinimal() {
        val bufferedReader = javaClass.getResourceAsStream("/frontmatter/minimal.md")
            .bufferedReader()
            .readText()

        val parser = FrontMatterParser()

        val conference = parser.parse(Conference.serializer(), bufferedReader)
    }
}
