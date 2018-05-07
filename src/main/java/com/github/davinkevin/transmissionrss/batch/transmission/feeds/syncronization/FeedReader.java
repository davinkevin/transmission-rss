package com.github.davinkevin.transmissionrss.batch.transmission.feeds.syncronization;

import com.github.davinkevin.transmissionrss.batch.transmission.feeds.syncronization.model.ItemFetchingSpecification;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;

import static com.github.davinkevin.transmissionrss.tables.PatternMatcher.PATTERN_MATCHER;
import static io.vavr.API.Try;

/**
 * Created by kevin on 08/04/2018
 */
@Component
@RequiredArgsConstructor
public class FeedReader implements ItemReader<ItemFetchingSpecification> {

    private final DSLContext query;
    private List<ItemFetchingSpecification> pm;

    @Override
    public ItemFetchingSpecification read() {
        if (pm.isEmpty()) {
            return null;
        }

        Tuple2<ItemFetchingSpecification, List<ItemFetchingSpecification>> f = pm.pop2();
        pm = f._2();

        return f._1();
    }

    @PostConstruct
    public void postConstruct() {
        pm = query
                .select(PATTERN_MATCHER.URL, PATTERN_MATCHER.MATCHER, PATTERN_MATCHER.EXCLUDE, PATTERN_MATCHER.DOWNLOAD_PATH)
                .from(PATTERN_MATCHER)
                .fetch()
                .stream()
                .map(v -> ItemFetchingSpecification.builder()
                            .url(Try(() -> new URL(v.getValue(PATTERN_MATCHER.URL))).getOrElseThrow(e -> new RuntimeException(e)))
                            .matcher(v.getValue(PATTERN_MATCHER.MATCHER))
                            .exclude(v.getValue(PATTERN_MATCHER.EXCLUDE))
                            .downloadPath(v.getValue(PATTERN_MATCHER.DOWNLOAD_PATH))
                        .build()
                )
                .collect(List.collector());
    }
}
