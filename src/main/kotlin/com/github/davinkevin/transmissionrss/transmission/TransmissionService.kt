package com.github.davinkevin.transmissionrss.transmission

import arrow.core.Failure
import arrow.core.Success
import arrow.core.Try
import arrow.core.getOrElse
import arrow.syntax.collections.firstOption
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.davinkevin.transmissionrss.transmission.arguments.AddTorrentArguments
import com.github.davinkevin.transmissionrss.transmission.properties.ServerProperty
import com.github.davinkevin.transmissionrss.transmission.request.AddTorrentRequest
import com.mashape.unirest.http.Headers
import com.mashape.unirest.http.Unirest
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class TransmissionService(val mapper: ObjectMapper, val property: ServerProperty) {

    private val log = org.slf4j.LoggerFactory.getLogger(TransmissionService::class.java)

    private val X_TRANSMISSION_SESSION_ID = "X-Transmission-Session-Id"

    lateinit var token: String

    fun add(arguments: AddTorrentArguments) {
        val response = Try {
            Unirest.post(generateUrl())
                    .header(X_TRANSMISSION_SESSION_ID, token)
                    .body(AddTorrentRequest(arguments))
                    .asString()
        }

        when(response) {
            is Success -> log.info("Torrent added: {}", arguments)
            is Failure -> log.error("Error during add {}", response.exception)
        }

    }

    @PostConstruct
    fun setup() {
        Unirest.setObjectMapper(object : com.mashape.unirest.http.ObjectMapper {
            override fun <T> readValue(value: String, valueType: Class<T>): T = mapper.readValue(value, valueType)
            override fun writeValue(value: Any): String = mapper.writeValueAsString(value)
        })
    }

    @PostConstruct
    private fun fetchToken() {
        token = Try { Unirest.post(generateUrl()).asBinary() }
                .map { it.headers }
                .getOrElse { Headers() }
                .entries
                .first { it.key === X_TRANSMISSION_SESSION_ID }
                .value
                .firstOption()
                .getOrElse { throw RuntimeException("No X-Transmission-Session-Id header found") }
    }

    private fun generateUrl(): String {
        return """http://${property.host}:${property.port}/${property.rpcPath}"""
    }
}