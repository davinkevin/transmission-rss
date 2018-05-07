package com.github.davinkevin.transmissionrss.batch.transmission.feeds.syncronization;

import com.github.davinkevin.transmissionrss.feeds.model.FeedProperty;
import com.github.davinkevin.transmissionrss.feeds.properties.FeedsProperty;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

/**
 * Created by kevin on 08/04/2018
 */
@Component
public class FeedReader implements ItemReader<FeedProperty> {

    private List<FeedProperty> feeds;

    public FeedReader(FeedsProperty feedsProperty) {
        feeds = feedsProperty.getFeeds();
    }

    @Override
    public FeedProperty read() {
        if (feeds.isEmpty()) {
            return null;
        }

        Tuple2<FeedProperty, List<FeedProperty>> f = feeds.pop2();
        feeds = f._2();

        return f._1();
    }
}
