package com.github.davinkevin.transmissionrss.batch.synchronization.feeds.specification

import com.github.davinkevin.transmissionrss.feeds.properties.FeedsProperty
import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component

@Component
class FeedsSpecificationReader(feedsProperty: FeedsProperty) : ItemReader<FeedSpecification> {

    lateinit var pm: List<FeedSpecification>

    init {
        pm = feedsProperty
                .feeds
                .map { it to it.regexp }
                .flatMap { v -> v.second
                        .map { r ->
                            FeedSpecification(
                                    url = v.first.url!!,
                                    matcher = r.matcher!!,
                                    exclude = r.exclude,
                                    downloadPath = r.downloadPath!!
                            )
                        }
                }
    }

    override fun read(): FeedSpecification? {
        val p = pm.firstOrNull()
        pm = pm.filter { it != p }

        return p
    }
}
