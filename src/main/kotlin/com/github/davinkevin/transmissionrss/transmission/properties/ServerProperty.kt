package com.github.davinkevin.transmissionrss.transmission.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("server")
data class ServerProperty(
        var host: String,
        var port: Integer,
        var rpcPath: String
)