package com.github.davinkevin.transmissionrss.transmission.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {

    @Bean
    fun mapper() = ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
}