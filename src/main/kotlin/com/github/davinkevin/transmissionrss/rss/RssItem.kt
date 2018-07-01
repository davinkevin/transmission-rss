package com.github.davinkevin.transmissionrss.rss

import java.net.URL
import java.time.ZonedDateTime

data class RssItem(
        val title: String,
        val link: URL,
        val guid: String,
        val pubDate: ZonedDateTime
)