package com.github.davinkevin.transmissionrss.batch.database.feeds.syncrhonization;

import com.github.davinkevin.transmissionrss.feeds.model.PatternMatcherDTO;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import static com.github.davinkevin.transmissionrss.tables.PatternMatcher.PATTERN_MATCHER;

/**
 * Created by kevin on 06/05/2018
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FeedDatabaseWriter implements ItemWriter<PatternMatcherDTO> {

    private final DSLContext query;

    @Override
    public void write(java.util.List<? extends PatternMatcherDTO> items) {

        query.deleteFrom(PATTERN_MATCHER).execute();

        List.ofAll(items).foldLeft(
                this.query.insertInto(PATTERN_MATCHER, PATTERN_MATCHER.URL, PATTERN_MATCHER.MATCHER, PATTERN_MATCHER.EXCLUDE, PATTERN_MATCHER.DOWNLOAD_PATH),
                (v, p) -> v.values(p.getUrl().toString(), p.getMatcher(), p.getExclude(), p.getDownloadPath())
        ).execute();
    }

}
