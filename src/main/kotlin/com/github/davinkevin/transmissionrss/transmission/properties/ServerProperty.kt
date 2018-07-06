package com.github.davinkevin.transmissionrss.transmission.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("server")
class ServerProperty {
    lateinit var host: String
    lateinit var rpcPath: String
    var port: Int = 0
}