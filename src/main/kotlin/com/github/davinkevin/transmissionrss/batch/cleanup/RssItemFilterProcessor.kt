package com.github.davinkevin.transmissionrss.batch.cleanup

import com.github.davinkevin.transmissionrss.Tables.PATTERN_MATCHER
import org.jooq.DSLContext
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

@Component
class RssItemFilterProcessor(val query: DSLContext): ItemProcessor<RssItemWithFeed, RssItemWithFeed> {

    override fun process(item: RssItemWithFeed): RssItemWithFeed? {

        val rules = query
                .select(PATTERN_MATCHER.MATCHER, PATTERN_MATCHER.EXCLUDE)
                .from(PATTERN_MATCHER)
                .where(PATTERN_MATCHER.URL.equal(item.fromFeed.toString()))
                .fetch { MatchingRule(matcher = it[PATTERN_MATCHER.MATCHER], exclude = it[PATTERN_MATCHER.EXCLUDE]) }

        val isMatched = rules.any {
            it.matcher.toRegex().matches(item.title) && when(it.exclude) {
                is String -> !it.exclude.toRegex().matches(item.title)
                else -> true
            }
        }

        return if (isMatched) null else item
    }

}