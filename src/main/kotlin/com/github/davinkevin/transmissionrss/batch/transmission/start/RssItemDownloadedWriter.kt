package com.github.davinkevin.transmissionrss.batch.transmission.start

import com.github.davinkevin.transmissionrss.Tables.RSS_ITEM
import org.jooq.DSLContext
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component

@Component
class RssItemDownloadedWriter(val query: DSLContext): ItemWriter<RssItemToDownload>{

    override fun write(items: MutableList<out RssItemToDownload>) {
        val r = RSS_ITEM

        val updates = items.map {
            query
                    .update(r)
                    .set(r.STATUS, it.status.name)
                    .set(r.DOWNLOADED_DATE, it.downloadDate.toOffsetDateTime())
                    .where(r.LINK.eq(it.link.toString()))
        }

        query.batch(updates).execute()
    }

}