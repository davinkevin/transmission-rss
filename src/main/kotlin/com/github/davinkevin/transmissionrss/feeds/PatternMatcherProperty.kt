package com.github.davinkevin.transmissionrss.feeds

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Duration

/**
 * Created by kevin on 08/04/2018
 */
data class PatternMatcherProperty(
    var matcher: String? = null,
    var exclude: String? = null,
    @JsonProperty("download_path") var downloadPath: String? = null,
    var minInterval: Duration = Duration.ZERO

)
