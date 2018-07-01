package com.github.davinkevin.transmissionrss.batch.synchronization.feeds.items

import com.github.davinkevin.transmissionrss.Tables.RSS_ITEM
import com.github.davinkevin.transmissionrss.rss.RssItem
import com.github.davinkevin.transmissionrss.tables.records.RssItemRecord
import org.jooq.DSLContext
import org.jooq.InsertValuesStep5
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import java.net.URL
import java.time.OffsetDateTime

@Component
class RssItemToDatabaseWriter(val query: DSLContext): ItemWriter<FeedWithItems> {

    override fun write(feeds: MutableList<out FeedWithItems>?) {
        feeds?.forEach { println("${it.url} with ${it.items.size} elements") }

        val inserts = feeds!!
                .flatMap({ feed -> feed.items.map { feed.url to it } })
                .map { toInsertQuery(it) }
                .map { it.onDuplicateKeyIgnore() }

        query.batch(inserts).execute()
    }

    private fun toInsertQuery(it: Pair<URL, RssItem>):  InsertValuesStep5<RssItemRecord, String, String, String, OffsetDateTime, String> {
            val rss = it.second
            val url = it.first
            return query.insertInto(RSS_ITEM, RSS_ITEM.TITLE, RSS_ITEM.GUID, RSS_ITEM.LINK, RSS_ITEM.PUB_DATE, RSS_ITEM.FROM_FEED)
                    .values(rss.title, rss.guid, rss.link.toString(), rss.pubDate.toOffsetDateTime(), url.toString())
    }
}