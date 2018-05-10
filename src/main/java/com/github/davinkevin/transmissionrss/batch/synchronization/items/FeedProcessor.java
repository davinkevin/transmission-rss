package com.github.davinkevin.transmissionrss.batch.synchronization.items;

import com.github.davinkevin.transmissionrss.batch.synchronization.items.model.RssItemsWithFeedUrl;
import com.github.davinkevin.transmissionrss.rss.service.RssTorrentService;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * Created by kevin on 08/04/2018
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FeedProcessor implements ItemProcessor<URL, List<RssItemsWithFeedUrl>> {

    private final RssTorrentService rssTorrentService;

    @Override
    public List<RssItemsWithFeedUrl> process(URL url) {
        return rssTorrentService
                .parse(url)
                .map(d -> d.getRootElement().getChild("channel").getChildren("item"))
                .map(List::ofAll)
                .getOrElse(List::empty)
                .map(e -> RssItemsWithFeedUrl.from(e, url));
    }
}
