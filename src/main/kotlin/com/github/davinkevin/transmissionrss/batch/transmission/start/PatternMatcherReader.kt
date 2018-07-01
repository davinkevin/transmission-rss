package com.github.davinkevin.transmissionrss.batch.transmission.start

import arrow.core.Eval
import com.github.davinkevin.transmissionrss.Tables.PATTERN_MATCHER
import com.github.davinkevin.transmissionrss.specification.FeedSpecification
import org.jooq.DSLContext
import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component
import java.net.URL

@Component
class PatternMatcherReader(val query: DSLContext): ItemReader<FeedSpecification> {

    var specs: Eval<List<FeedSpecification>> = Eval.later { this.readItems() }

    override fun read(): FeedSpecification? {
        val specsComputed = specs.value()

        val spec = specsComputed.firstOrNull()
        specs = Eval.now(specsComputed.filter { it != spec })

        return spec
    }

    private fun readItems() = query
            .select(PATTERN_MATCHER.URL, PATTERN_MATCHER.MATCHER, PATTERN_MATCHER.EXCLUDE,PATTERN_MATCHER.DOWNLOAD_PATH)
            .from(PATTERN_MATCHER)
            .fetch { FeedSpecification(
                    url = URL(it[PATTERN_MATCHER.URL]),
                    matcher = it[PATTERN_MATCHER.MATCHER],
                    exclude = it[PATTERN_MATCHER.EXCLUDE],
                    downloadPath = it[PATTERN_MATCHER.DOWNLOAD_PATH]
            )}
}