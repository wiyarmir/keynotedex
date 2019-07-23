package es.guillermoorellana.keynotedex.backend.frontmatter

import com.charleskorn.kaml.Yaml
import java.io.BufferedReader
import java.io.StringReader

class FrontMatterParser(
    private val yamlParser: Yaml = Yaml.default
) {
    private val frontMatterDelimiter = Regex("[-]{3,}")

    companion object {
        private val instance = FrontMatterParser()
        fun parseConference(conference: String): FMConference =
            instance.parseConference(BufferedReader(StringReader(conference)))

        fun parseConference(bufferedReader: BufferedReader): FMConference = instance.parseConference(bufferedReader)
    }

    fun parseConference(bufferedReader: BufferedReader): FMConference = bufferedReader
        .lineSequence()
        .dropWhile { frontMatterDelimiter.matches(it) }
        .takeWhile { !frontMatterDelimiter.matches(it) }
        .joinToString(separator = "\n")
        .let { yamlParser.parse(FMConference.serializer(), it) }
}

