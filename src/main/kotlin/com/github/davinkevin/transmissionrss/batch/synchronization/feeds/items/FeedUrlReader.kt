package com.github.davinkevin.transmissionrss.batch.synchronization.feeds.items

import com.github.davinkevin.transmissionrss.tables.PatternMatcher.PATTERN_MATCHER
import org.jooq.DSLContext
import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component
import java.net.URL

@Component
class FeedUrlReader(val query: DSLContext): ItemReader<URL>  {

    var urls: List<URL>? = null

    override fun read(): URL? {

        if(urls === null ) {
            urls = readUrls()
        }

        val url = urls?.firstOrNull()
        urls = urls?.filter { it != url }

        return url
    }

    private fun readUrls() = query
            .selectDistinct(PATTERN_MATCHER.URL)
            .from(PATTERN_MATCHER)
            .fetch { URL(it[PATTERN_MATCHER.URL]) }
}