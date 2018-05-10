package com.github.davinkevin.transmissionrss.batch.synchronization.feeds.specification;

import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.specification.model.FeedSpecification;
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
public class FeedsSpecificationReader implements ItemReader<FeedSpecification> {

    private List<FeedSpecification> pm;

    public FeedsSpecificationReader(FeedsProperty feedsProperty) {
        pm = feedsProperty.getFeeds()
                .map(f -> Tuple(f, f.getRegexp()))
                .flatMap(t -> t._2().map(v -> FeedSpecification.builder()
                        .url(t._1().getUrl())
                        .downloadPath(v.getDownloadPath())
                        .exclude(v.getExclude())
                        .matcher(v.getMatcher())
                        .build())
                )
        ;
    }

    @Override
    public FeedSpecification read() {
        if (pm.isEmpty()) {
            return null;
        }

        Tuple2<FeedSpecification, List<FeedSpecification>> p = pm.pop2();
        pm = p._2();

        log.info("Push value {}", p._1());

        return p._1();
    }
}
