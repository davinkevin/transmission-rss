package com.github.davinkevin.transmissionrss.batch.synchronization.feeds.items

import com.github.davinkevin.transmissionrss.Tables.RSS_ITEM
import com.github.davinkevin.transmissionrss.tables.records.RssItemRecord
import org.jooq.DSLContext
import org.jooq.InsertValuesStep6
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

    private fun toInsertQuery(it: Pair<URL, ItemsWithStatus>): InsertValuesStep6<RssItemRecord, String, String, String, OffsetDateTime, String, String> {
            val rss = it.second
            val url = it.first
        val r = RSS_ITEM
        return query.insertInto(r, r.TITLE, r.GUID, r.LINK, r.PUB_DATE, r.FROM_FEED, r.STATUS)
                    .values(rss.title, rss.guid, rss.link.toString(), rss.pubDate.toOffsetDateTime(), url.toString(), rss.status.name)
    }
}