package com.github.davinkevin.transmissionrss.batch.synchronization.feeds.specification

import java.net.URL

data class FeedSpecification(
    val url: URL,
    val matcher: String,
    val exclude: String?,
    val downloadPath: String
)