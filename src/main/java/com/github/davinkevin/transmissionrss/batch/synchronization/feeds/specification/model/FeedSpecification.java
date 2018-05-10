package com.github.davinkevin.transmissionrss.batch.synchronization.feeds.specification.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.net.URL;

/**
 * Created by kevin on 06/05/2018
 */
@Data
@Builder
public class FeedSpecification {
    private URL url;
    private String matcher;
    private String exclude;
    private @JsonProperty("download_path") String downloadPath;
}
