package com.github.davinkevin.transmissionrss.feeds.model;

import io.vavr.collection.List;
import lombok.Data;
import lombok.experimental.Accessors;

import java.net.URL;

/**
 * Created by kevin on 08/04/2018
 */
@Data
@Accessors(chain = true)
public class Feed {
    private URL url;
    private List<PatternMatcher> regexp;

    public Feed setRegexp(java.util.List<PatternMatcher> regexp) {
        this.regexp = List.ofAll(regexp);
        return this;
    }
}
