package com.github.davinkevin.transmissionrss.batch.transmission.feeds.syncronization;

import com.github.davinkevin.transmissionrss.batch.transmission.feeds.syncronization.model.ItemFetchingSpecification;
import com.github.davinkevin.transmissionrss.rss.model.RssItem;
import com.github.davinkevin.transmissionrss.rss.service.RssTorrentService;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

/**
 * Created by kevin on 08/04/2018
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FeedProcessor implements ItemProcessor<ItemFetchingSpecification, List<RssItem>> {

    private final RssTorrentService rssTorrentService;

    @Override
    public List<RssItem> process(ItemFetchingSpecification i) {
        Predicate<RssItem> include = (RssItem v) -> Pattern.compile(i.getMatcher()).asPredicate().test(v.getTitle());
        Predicate<RssItem> exclude = nonNull(i.getExclude())
                ? v -> Pattern.compile(i.getExclude()).asPredicate().negate().test(v.getTitle())
                : v -> true;

        return rssTorrentService
                .parse(i.getUrl())
                .map(v -> v.getRootElement().getChild("channel").getChildren("item"))
                .map(List::ofAll)
                .getOrElse(List::empty)
                .map(RssItem::from)
                .filter(include.and(exclude));
    }
}
