package com.github.davinkevin.transmissionrss.specification

import java.net.URL
import java.time.Duration

data class FeedSpecification(
    val url: URL,
    val matcher: String,
    val exclude: String?,
    val downloadPath: String,
    val minInterval: Duration
)