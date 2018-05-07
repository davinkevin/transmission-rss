package com.github.davinkevin.transmissionrss.batch.transmission.feeds.syncronization.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.net.URL;

/**
 * Created by kevin on 06/05/2018
 */
@Data
@Builder
public class ItemFetchingSpecification {
    private URL url;
    private String matcher;
    private String exclude;
    private @JsonProperty("download_path") String downloadPath;
}
