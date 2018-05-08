package com.github.davinkevin.transmissionrss.batch.database.feeds.syncrhonization;

import com.github.davinkevin.transmissionrss.batch.database.items.syncronization.model.ItemFetchingSpecification;
import com.github.davinkevin.transmissionrss.feeds.properties.FeedsProperty;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import static io.vavr.API.Tuple;

/**
 * Created by kevin on 06/05/2018
 */
@Slf4j
@Component
public class FeedDatabaseReader implements ItemReader<ItemFetchingSpecification> {

    private List<ItemFetchingSpecification> pm;

    public FeedDatabaseReader(FeedsProperty feedsProperty) {
        pm = feedsProperty.getFeeds()
                .map(f -> Tuple(f, f.getRegexp()))
                .flatMap(t -> t._2().map(v -> ItemFetchingSpecification.builder()
                        .url(t._1().getUrl())
                        .downloadPath(v.getDownloadPath())
                        .exclude(v.getExclude())
                        .matcher(v.getMatcher())
                        .build())
                )
        ;
    }

    @Override
    public ItemFetchingSpecification read() {
        if (pm.isEmpty()) {
            return null;
        }

        Tuple2<ItemFetchingSpecification, List<ItemFetchingSpecification>> p = pm.pop2();
        pm = p._2();

        return p._1();
    }
}
