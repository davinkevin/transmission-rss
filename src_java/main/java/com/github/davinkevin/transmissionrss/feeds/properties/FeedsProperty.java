package com.github.davinkevin.transmissionrss.feeds.properties;

import com.github.davinkevin.transmissionrss.feeds.model.FeedProperty;
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
    @Getter List<FeedProperty> feeds;

    public void setFeeds(java.util.List<FeedProperty> feedProperties) {
        this.feeds = List.ofAll(feedProperties);
    }
}
