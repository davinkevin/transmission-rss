package com.github.davinkevin.transmissionrss.transmission.request;

import com.github.davinkevin.transmissionrss.transmission.arguments.AddTorrentArguments;

/**
 * Created by kevin on 08/04/2018
 */
public class AddTorrentRequest<T extends AddTorrentArguments> extends RpcRequest<T> {

    private static final String TORRENT_ADD = "torrent-add";

    public AddTorrentRequest(T arguments) {
        super(TORRENT_ADD, arguments, "");
    }
}
