package es.guillermoorellana.keynotedex.backend.frontmatter

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.KSerializer

class FrontMatterParser(
    val yamlParser: Yaml = Yaml.default
) {
    val frontMatterDelimiter = Regex("[-]{3,}")

    inline fun <reified T> parse(serializer: KSerializer<T>, text: String): T = text
        .lineSequence()
        .dropWhile { frontMatterDelimiter.matches(it) }
        .takeWhile { !frontMatterDelimiter.matches(it) }
        .joinToString(separator = "\n")
        .let { yamlParser.parse(serializer, it) }
}

