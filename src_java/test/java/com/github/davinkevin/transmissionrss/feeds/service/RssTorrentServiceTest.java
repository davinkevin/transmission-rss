package com.github.davinkevin.transmissionrss.feeds.service;

import com.github.davinkevin.transmissionrss.rss.service.RssTorrentService;
import io.vavr.API;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kevin on 08/04/2018
 */

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RssTorrentServiceTest {

    @Autowired
    RssTorrentService service;

    @Test
    public void should_parse() throws MalformedURLException {
        /* GIVEN */
        /* WHEN  */
        Option<Document> document = service.parse(new URL(""));

        /* THEN  */
        document.peek(API::println);
    }
}