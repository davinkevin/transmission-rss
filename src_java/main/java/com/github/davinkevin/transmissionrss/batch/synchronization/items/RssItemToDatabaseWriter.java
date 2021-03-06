package com.github.davinkevin.transmissionrss.batch.synchronization.items;

import com.github.davinkevin.transmissionrss.batch.synchronization.items.model.RssItemsWithFeedUrl;
import com.github.davinkevin.transmissionrss.tables.records.RssItemRecord;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.InsertOnDuplicateStep;
import org.jooq.InsertReturningStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static com.github.davinkevin.transmissionrss.tables.RssItem.RSS_ITEM;

/**
 * Created by kevin on 08/04/2018
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RssItemToDatabaseWriter implements ItemWriter<List<RssItemsWithFeedUrl>> {

    private final DSLContext query;

    @Override
    public void write(java.util.List<? extends List<RssItemsWithFeedUrl>> items) {
        List<RssItemsWithFeedUrl> i = List.ofAll(items).flatMap(Function.identity());

        log.info("Number of item in writer {}", i.size());

        i.forEach(v -> log.info("In Writer: {}", v));

        List<InsertReturningStep<RssItemRecord>> inserts  = i
                .map(
                        rssItem -> query
                            .insertInto(RSS_ITEM, RSS_ITEM.TITLE, RSS_ITEM.GUID, RSS_ITEM.LINK, RSS_ITEM.PUB_DATE, RSS_ITEM.FROM_FEED)
                            .values(rssItem.getTitle(), rssItem.getGuid(), rssItem.getLink().toString(), rssItem.getPubDate().toOffsetDateTime(), rssItem.getSourceFeed().toString())
                )
                .map(InsertOnDuplicateStep::onDuplicateKeyIgnore);

        query.batch(inserts.toJavaList()).execute();

        /* Just for debug... */
        query.select(RSS_ITEM.TITLE, RSS_ITEM.PUB_DATE)
                .from(RSS_ITEM)
                .fetch()
                .forEach(r -> log.info("{} - {}", r.getValue(RSS_ITEM.TITLE), r.getValue(RSS_ITEM.PUB_DATE)));
    }

}
