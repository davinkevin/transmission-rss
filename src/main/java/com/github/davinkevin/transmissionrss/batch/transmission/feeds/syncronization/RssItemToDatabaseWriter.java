package com.github.davinkevin.transmissionrss.batch.transmission.feeds.syncronization;

import com.github.davinkevin.transmissionrss.rss.model.RssItem;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static com.github.davinkevin.transmissionrss.tables.RssItem.*;

/**
 * Created by kevin on 08/04/2018
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RssItemToDatabaseWriter implements ItemWriter<List<RssItem>> {

    private final DSLContext query;

    @Override
    public void write(java.util.List<? extends List<RssItem>> items) {
        List<RssItem> i = List.ofAll(items).flatMap(Function.identity());

        log.info("Number of item in writer {}", i.size());

        i.forEach(v -> log.info("In Writer: {}", v));

        List.ofAll(i).foldLeft(
                query.insertInto(RSS_ITEM, RSS_ITEM.TITLE, RSS_ITEM.GUID, RSS_ITEM.LINK),
                (v, p) -> v.values(p.getTitle(), p.getGuid(), p.getLink().toString())
        ).execute();
    }

}
