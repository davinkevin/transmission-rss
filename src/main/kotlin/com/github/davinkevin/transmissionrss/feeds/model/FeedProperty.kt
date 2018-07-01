package com.github.davinkevin.transmissionrss.feeds.model

import java.net.URL

/**
 * Created by kevin on 08/04/2018
 */
data class FeedProperty(
        var url: URL? = null,
        var regexp: List<PatternMatcherProperty> = ArrayList()
)
