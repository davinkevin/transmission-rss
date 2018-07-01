package com.github.davinkevin.transmissionrss.batch.synchronization.feeds.specification;

import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.specification.model.FeedSpecification;
import com.github.davinkevin.transmissionrss.tables.records.PatternMatcherRecord;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep4;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import static com.github.davinkevin.transmissionrss.tables.PatternMatcher.PATTERN_MATCHER;

/**
 * Created by kevin on 06/05/2018
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FeedsSpecificationDatabaseWriter implements ItemWriter<FeedSpecification> {

    private final DSLContext query;

    @Override
    public void write(java.util.List<? extends FeedSpecification> items) {

        query.deleteFrom(PATTERN_MATCHER).execute();

        List<InsertValuesStep4<PatternMatcherRecord, String, String, String, String>> inserts = List.ofAll(items).map(
                p -> query
                        .insertInto(PATTERN_MATCHER, PATTERN_MATCHER.URL, PATTERN_MATCHER.MATCHER, PATTERN_MATCHER.EXCLUDE, PATTERN_MATCHER.DOWNLOAD_PATH)
                        .values(p.getUrl().toString(), p.getMatcher(), p.getExclude(), p.getDownloadPath())
        );

        query.batch(inserts.toJavaList()).execute();
    }

}
