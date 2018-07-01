package com.github.davinkevin.transmissionrss.rss

import arrow.core.Option
import arrow.core.Try
import com.mashape.unirest.http.Unirest
import org.jdom2.Document
import org.jdom2.input.SAXBuilder
import org.springframework.stereotype.Component
import java.net.URL

@Component
class RssTorrentService {

    fun parse(url: URL): Option<Document> {
        val body = Unirest.get(url.toString()).asBinary().body
        println("Call to $url")
        return Try { SAXBuilder().build(body) }
                .toOption()
    }
}