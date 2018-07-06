package com.github.davinkevin.transmissionrss.transmission.arguments

import com.fasterxml.jackson.annotation.JsonProperty

data class AddTorrentArguments(
        val filename: String,
        val paused: Boolean,
        @JsonProperty("download-dir") val downloadDir: String
)