package com.github.davinkevin.transmissionrss.transmission.arguments;

import lombok.Builder;

/**
 * Created by kevin on 08/04/2018
 */

public class AddTorrentByUrlArguments extends AddTorrentArguments {

    @Builder
    public AddTorrentByUrlArguments(String filename, Boolean paused, String downloadDir) {
        super(filename, paused, downloadDir);
    }
}
