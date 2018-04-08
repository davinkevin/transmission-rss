package com.github.davinkevin.transmissionrss.batch.reader;

import com.github.davinkevin.transmissionrss.feeds.model.Feed;
import com.github.davinkevin.transmissionrss.feeds.properties.FeedsProperty;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

/**
 * Created by kevin on 08/04/2018
 */
@Component
public class FeedReader implements ItemReader<Feed> {

    private List<Feed> feeds;

    public FeedReader(FeedsProperty feedsProperty) {
        feeds = feedsProperty.getFeeds();
    }

    @Override
    public Feed read() {
        if (feeds.isEmpty()) {
            return null;
        }

        Tuple2<Feed, List<Feed>> f = feeds.pop2();
        feeds = f._2();

        return f._1();
    }
}
