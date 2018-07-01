package com.github.davinkevin.transmissionrss.batch.cleanup

import arrow.core.Eval
import com.github.davinkevin.transmissionrss.tables.RssItem.RSS_ITEM
import org.jooq.DSLContext
import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component
import java.net.URL

@Component
class RssItemFromDatabaseReader(val query: DSLContext): ItemReader<RssItemWithFeed> {

    var items: Eval<List<RssItemWithFeed>> = Eval.later { this.readItems() }

    override fun read(): RssItemWithFeed? {
        val itemsComputed = items.value()

        val url = itemsComputed.firstOrNull()
        items = Eval.now(itemsComputed.filter { it != url })

        return url
    }

    private fun readItems() = query
            .selectDistinct(RSS_ITEM.TITLE, RSS_ITEM.LINK, RSS_ITEM.GUID, RSS_ITEM.PUB_DATE, RSS_ITEM.FROM_FEED)
            .from(RSS_ITEM)
            .fetch { RssItemWithFeed(
                    title = it[RSS_ITEM.TITLE],
                    link = URL(it[RSS_ITEM.LINK]),
                    guid = it[RSS_ITEM.GUID],
                    pubDate = it[RSS_ITEM.PUB_DATE].toZonedDateTime(),
                    fromFeed = URL(it[RSS_ITEM.FROM_FEED])
            )}

}