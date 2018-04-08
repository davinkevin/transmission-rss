package com.github.davinkevin.transmissionrss.feeds.properties;

import com.github.davinkevin.transmissionrss.feeds.model.Feed;
import io.vavr.collection.List;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by kevin on 08/04/2018
 */
@Configuration
@ConfigurationProperties
public class FeedsProperty {
    @Getter List<Feed> feeds;

    public void setFeeds(java.util.List<Feed> feeds) {
        this.feeds = List.ofAll(feeds);
    }
}
