package com.github.davinkevin.transmissionrss.feeds.model;

import io.vavr.collection.List;
import lombok.Data;

import java.net.URL;

/**
 * Created by kevin on 08/04/2018
 */
@Data
public class Feed {
    private URL url;
    private List<PatternMatcher> regexp;

    public void setRegexp(java.util.List<PatternMatcher> regexp) {
        this.regexp = List.ofAll(regexp);
    }
}
