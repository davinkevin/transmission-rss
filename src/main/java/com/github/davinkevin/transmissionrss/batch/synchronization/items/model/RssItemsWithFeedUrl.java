package com.github.davinkevin.transmissionrss.batch.synchronization.items.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jdom2.Element;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.function.Function;

import static io.vavr.API.Try;
import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;

/**
 * Created by kevin on 10/05/2018
 */
@ToString
@RequiredArgsConstructor
public class RssItemsWithFeedUrl {
    @Getter private final String title;
    @Getter private final URL link;
    @Getter private final String guid;
    @Getter private final ZonedDateTime pubDate;
    @Getter private final URL sourceFeed;

    public static RssItemsWithFeedUrl from(Element element, URL url) {
        String title = element.getChild("title").getTextTrim();
        String link = element.getChild("link").getTextTrim();
        String guid = element.getChild("guid").getTextTrim();
        ZonedDateTime pubDate = ZonedDateTime.parse(element.getChild("pubDate").getTextTrim(), RFC_1123_DATE_TIME);

        return Try(() -> new URL(link))
                .map(ownUrl -> new RssItemsWithFeedUrl(title, ownUrl, guid, pubDate, url))
                .getOrElseThrow((Function<Throwable, RuntimeException>) RuntimeException::new);
    }
}
