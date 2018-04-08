package com.github.davinkevin.transmissionrss.rss.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jdom2.Element;

import java.net.URL;
import java.util.function.Function;

import static io.vavr.API.Try;

/**
 * Created by kevin on 08/04/2018
 */
@ToString
@RequiredArgsConstructor
public class RssItem {
    @Getter private final String title;
    @Getter private final URL link;
    @Getter private final String guid;

    public static RssItem toRssItem(Element element) {
        String title = element.getChild("title").getTextTrim();
        String link = element.getChild("link").getTextTrim();
        String guid = element.getChild("guid").getTextTrim();

        return Try(() -> new URL(link))
                .map(url -> new RssItem(title, url, guid))
                .getOrElseThrow((Function<Throwable, RuntimeException>) RuntimeException::new);
    }
}
