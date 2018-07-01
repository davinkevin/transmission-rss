package com.github.davinkevin.transmissionrss.batch.transmission.start

import com.github.davinkevin.transmissionrss.Tables.RSS_ITEM
import com.github.davinkevin.transmissionrss.batch.transmission.start.ItemStatus.*
import com.github.davinkevin.transmissionrss.specification.FeedSpecification
import org.jooq.DSLContext
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import java.net.URL
import java.time.ZonedDateTime

@Component
class RssItemDownloaderProcessor(val query: DSLContext): ItemProcessor<FeedSpecification, RssItemToDownload> {

    override fun process(item: FeedSpecification): RssItemToDownload? {
        val r = RSS_ITEM
        val itemsInDatabase = query
                .selectDistinct(r.TITLE, r.LINK, r.GUID, r.PUB_DATE, r.FROM_FEED, r.STATUS)
                .from(r)
                .where(r.STATUS.eq(WAITING_TO_BE_TREATED.name))
                .orderBy(r.PUB_DATE.asc())
                .fetch {
                    RssItemToDownload(
                            title = it[r.TITLE],
                            link = URL(it[r.LINK]),
                            pubDate = it[r.PUB_DATE].toZonedDateTime(),
                            fromFeed = URL(it[r.FROM_FEED]),
                            status = valueOf(it[r.STATUS]),
                            downloadDate = ZonedDateTime.now()
                    )
                }

        val itemFiltered = itemsInDatabase
                .filter {
                    item.matcher.toRegex().matches(it.title) && when (item.exclude) {
                        is String -> !item.exclude.toRegex().matches(it.title)
                        else -> true
                    }
                }

        val rssItemToDownload = itemFiltered
                .firstOrNull() ?: return null

        println("Download $rssItemToDownload")

        return rssItemToDownload.copy(status = SENT)
    }

}