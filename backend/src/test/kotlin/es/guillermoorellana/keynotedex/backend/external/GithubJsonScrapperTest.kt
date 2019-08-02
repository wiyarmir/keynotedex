package es.guillermoorellana.keynotedex.backend.external

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import es.guillermoorellana.keynotedex.backend.external.json.GithubJsonScrapper
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GithubJsonScrapperTest {
    private val githubHttpClientMock = mock<GithubHttpClient> {
        onBlocking { getDirectoryContent(any(), any()) } doReturn emptyList()
    }

    @Test
    fun testScrapping(): Unit = runBlocking {
        val conferences = GithubJsonScrapper(githubHttpClientMock)
            .fetchAllYears("tech-conferences/conference-data", "conferences/")
    }
}
