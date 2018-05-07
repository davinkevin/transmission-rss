package com.github.davinkevin.transmissionrss.feeds.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by kevin on 08/04/2018
 */
@Data
public class PatternMatcherProperty {
    private String matcher;
    private String exclude;
    private @JsonProperty("download_path") String downloadPath;
}
