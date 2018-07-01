package com.github.davinkevin.transmissionrss.feeds.model;

import io.vavr.collection.List;
import lombok.Data;

import java.net.URL;

/**
 * Created by kevin on 08/04/2018
 */
@Data
public class FeedProperty {
    private URL url;
    private List<PatternMatcherProperty> regexp;

    public void setRegexp(java.util.List<PatternMatcherProperty> regexp) {
        this.regexp = List.ofAll(regexp);
    }
}
