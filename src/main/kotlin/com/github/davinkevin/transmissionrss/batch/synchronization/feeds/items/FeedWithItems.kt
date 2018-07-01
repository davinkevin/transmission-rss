package com.github.davinkevin.transmissionrss.batch.synchronization.feeds.items

import com.github.davinkevin.transmissionrss.rss.RssItem
import java.net.URL

data class FeedWithItems(val url: URL, val items: List<RssItem>)