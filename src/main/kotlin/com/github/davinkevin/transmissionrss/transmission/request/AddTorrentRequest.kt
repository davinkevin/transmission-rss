package com.github.davinkevin.transmissionrss.transmission.request

import com.github.davinkevin.transmissionrss.transmission.arguments.AddTorrentArguments

data class AddTorrentRequest(val arguments: AddTorrentArguments) {
    private val TORRENT_ADD = "torrent-add"

    fun toRpcRequest() = RpcRequest(
            method = TORRENT_ADD,
            arguments = arguments,
            tag = ""
    )
}