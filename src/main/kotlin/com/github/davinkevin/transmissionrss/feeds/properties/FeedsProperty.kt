package com.github.davinkevin.transmissionrss.feeds.properties

import com.github.davinkevin.transmissionrss.feeds.model.FeedProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * Created by kevin on 08/04/2018
 */
@Configuration
@ConfigurationProperties
data class FeedsProperty (
    var feeds: List<FeedProperty> = ArrayList()
)
