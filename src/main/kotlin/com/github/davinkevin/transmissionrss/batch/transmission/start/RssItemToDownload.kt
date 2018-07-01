package com.github.davinkevin.transmissionrss.batch.transmission.start

import java.net.URL
import java.time.ZonedDateTime

data class RssItemToDownload(val title: String,
                             val link: URL,
                             val pubDate: ZonedDateTime,
                             val fromFeed: URL,
                             val status: ItemStatus,
                             val downloadDate: ZonedDateTime
)