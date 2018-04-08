package com.github.davinkevin.transmissionrss.transmission.arguments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by kevin on 08/04/2018
 */
@Getter
@RequiredArgsConstructor
public abstract class AddTorrentArguments {
    private final String filename;
    private final Boolean paused;
    private final @JsonProperty("download-dir") String downloadDir;
}
