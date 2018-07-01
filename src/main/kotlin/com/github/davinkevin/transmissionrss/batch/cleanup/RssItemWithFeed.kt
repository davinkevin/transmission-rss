package com.github.davinkevin.transmissionrss.batch.cleanup

import java.net.URL
import java.time.ZonedDateTime

data class RssItemWithFeed (
        val title: String,
        val link: URL,
        val guid: String,
        val pubDate: ZonedDateTime,
        val fromFeed: URL
)