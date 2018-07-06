package com.github.davinkevin.transmissionrss.batch.synchronization.feeds.specification

import com.github.davinkevin.transmissionrss.specification.FeedSpecification
import com.github.davinkevin.transmissionrss.tables.PatternMatcher.PATTERN_MATCHER
import com.github.davinkevin.transmissionrss.tables.records.PatternMatcherRecord
import org.jooq.DSLContext
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component

@Component
class FeedsSpecificationDatabaseWriter(val query: DSLContext) : ItemWriter<FeedSpecification> {


    override fun write(items: MutableList<out FeedSpecification>?) {
        val pm = PATTERN_MATCHER

        query.deleteFrom<PatternMatcherRecord>(pm).execute()

        val insertion = items?.map {
            query
                    .insertInto(pm, pm.URL, pm.MATCHER, pm.EXCLUDE, pm.DOWNLOAD_PATH, pm.MIN_INTERVAL)
                    .values(it.url.toString(), it.matcher, it.exclude, it.downloadPath, it.minInterval.toString())
        }

        query.batch(insertion).execute()
    }

}