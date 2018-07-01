package com.github.davinkevin.transmissionrss.batch.synchronization.feeds.items

import com.github.davinkevin.transmissionrss.batch.transmission.start.ItemStatus
import java.net.URL
import java.time.ZonedDateTime

data class FeedWithItems(val url: URL, val items: List<ItemsWithStatus>)

data class ItemsWithStatus(
        val title: String,
        val link: URL,
        val guid: String,
        val pubDate: ZonedDateTime,
        val status: ItemStatus = ItemStatus.WAITING_TO_BE_TREATED
)