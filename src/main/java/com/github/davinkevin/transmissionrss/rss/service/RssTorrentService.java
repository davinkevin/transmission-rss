package com.github.davinkevin.transmissionrss.rss.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.springframework.stereotype.Service;

import java.net.URL;

import static io.vavr.API.Try;

/**
 * Created by kevin on 08/04/2018
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RssTorrentService {

    public Option<Document> parse(URL url) {
        return Try(() -> Unirest.get(url.toString()).asBinary())
                .map(HttpResponse::getBody)
                .mapTry(v -> new SAXBuilder().build(v))
                .onFailure(e -> log.error("Error during parsing of {}", url, e))
                .toOption();
    }


}
