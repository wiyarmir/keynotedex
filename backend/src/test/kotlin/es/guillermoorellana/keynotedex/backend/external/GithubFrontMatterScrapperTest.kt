package es.guillermoorellana.keynotedex.backend.external

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import es.guillermoorellana.keynotedex.backend.external.frontmatter.GithubFrontMatterScrapper
import es.guillermoorellana.keynotedex.backend.frontmatter.FrontMatterParser
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GithubFrontMatterScrapperTest {
    private val githubHttpClientMock = mock<GithubHttpClient> {
        onBlocking { getDirectoryFiles(any(), any()) } doReturn emptyList()
    }

    private val frontMatterParserMock = mock<FrontMatterParser> {}

    @Test
    fun testScrapping() = runBlocking {
        val conferences = GithubFrontMatterScrapper(
            githubHttpClient = githubHttpClientMock,
            frontMatterParser = frontMatterParserMock
        ).fetch("npatarino/tech-conferences-spain", "_conferences/")
    }
}
