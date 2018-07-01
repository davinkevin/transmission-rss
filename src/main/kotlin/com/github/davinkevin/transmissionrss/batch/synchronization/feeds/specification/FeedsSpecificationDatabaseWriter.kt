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

        query.deleteFrom<PatternMatcherRecord>(PATTERN_MATCHER).execute()

        val insertion = items?.map {
            query
                    .insertInto(PATTERN_MATCHER, PATTERN_MATCHER.URL, PATTERN_MATCHER.MATCHER, PATTERN_MATCHER.EXCLUDE, PATTERN_MATCHER.DOWNLOAD_PATH)
                    .values(it.url.toString(), it.matcher, it.exclude, it.downloadPath)
        }

        query.batch(insertion).execute()
    }

}