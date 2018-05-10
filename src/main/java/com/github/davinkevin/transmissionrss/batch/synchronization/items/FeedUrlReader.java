package com.github.davinkevin.transmissionrss.batch.synchronization.items;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.net.URL;

import static com.github.davinkevin.transmissionrss.tables.PatternMatcher.PATTERN_MATCHER;
import static io.vavr.API.Try;
import static java.util.Objects.isNull;

/**
 * Created by kevin on 08/04/2018
 */
@Component
@RequiredArgsConstructor
public class FeedUrlReader implements ItemReader<URL> {

    private final DSLContext query;
    private List<URL> pm;

    @Override
    public URL read() {
        if (isNull(pm)) {
            pm = readUrls();
        }

        if (pm.isEmpty()) {
            return null;
        }

        Tuple2<URL, List<URL>> f = pm.pop2();
        pm = f._2();

        return f._1();
    }

    private List<URL> readUrls() {
        return query
                .selectDistinct(PATTERN_MATCHER.URL)
                .from(PATTERN_MATCHER)
                .fetch()
                .stream()
                .map(v -> Try(() -> new URL(v.getValue(PATTERN_MATCHER.URL))).getOrElseThrow(e -> new RuntimeException(e)))
                .collect(List.collector());
    }
}
