package com.github.davinkevin.transmissionrss.batch.synchronization.feeds.items

import arrow.core.getOrElse
import com.github.davinkevin.transmissionrss.rss.RssTorrentService
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import java.net.URL
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME

@Component
class FeedProcessor(val rssTorrentService: RssTorrentService): ItemProcessor<URL, FeedWithItems> {

    override fun process(url: URL?): FeedWithItems {
        val items = rssTorrentService
                .parse(url!!)
                .map { it.rootElement.getChild("channel").getChildren("item") }
                .getOrElse { ArrayList() }
                .map {
                    ItemsWithStatus(
                            title = it.getChild("title").textTrim,
                            link = URL(it.getChild("link").textTrim),
                            guid = it.getChild("guid").textTrim,
                            pubDate = ZonedDateTime.parse(it.getChild("pubDate").textTrim, RFC_1123_DATE_TIME)
                    )
                }
        return FeedWithItems(url, items)
    }

}