package com.github.davinkevin.transmissionrss.batch.cleanup

import com.github.davinkevin.transmissionrss.Tables.RSS_ITEM
import org.jooq.DSLContext
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component

@Component
class RssItemToDeleteWriter(val query: DSLContext): ItemWriter<RssItemWithFeed> {

    override fun write(items: MutableList<out RssItemWithFeed>) {
        val deletes = items.map {
            query
                    .deleteFrom(RSS_ITEM)
                    .where(RSS_ITEM.LINK.equal(it.link.toString()))
        }

        query.batch(deletes).execute()
    }

}