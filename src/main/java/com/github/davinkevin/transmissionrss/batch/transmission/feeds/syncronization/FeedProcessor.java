package com.github.davinkevin.transmissionrss.batch.transmission.feeds.syncronization;

import com.github.davinkevin.transmissionrss.feeds.model.Feed;
import com.github.davinkevin.transmissionrss.feeds.model.PatternMatcher;
import com.github.davinkevin.transmissionrss.rss.model.RssItem;
import com.github.davinkevin.transmissionrss.rss.service.RssTorrentService;
import com.github.davinkevin.transmissionrss.transmission.arguments.AddTorrentArguments;
import com.github.davinkevin.transmissionrss.transmission.arguments.AddTorrentByUrlArguments;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import static io.vavr.API.Tuple;
import static java.util.Objects.nonNull;

/**
 * Created by kevin on 08/04/2018
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FeedProcessor implements ItemProcessor<Feed, List<AddTorrentArguments>> {

    private final RssTorrentService rssTorrentService;

    @Override
    public List<AddTorrentArguments> process(Feed feed) {
        List<RssItem> items = rssTorrentService
                .parse(feed.getUrl())
                .map(v -> v.getRootElement().getChild("channel").getChildren("item"))
                .map(List::ofAll)
                .getOrElse(List::empty)
                .map(RssItem::from);

        return feed.getRegexp()
                .map(v -> Tuple(v, filterOn(v, items)))

                .flatMap(this::toArguments);
    }

    private List<RssItem> filterOn(PatternMatcher p, List<RssItem> items) {
        Predicate<RssItem> include = (RssItem v) -> Pattern.compile(p.getMatcher()).asPredicate().test(v.getTitle());
        Predicate<RssItem> exclude = nonNull(p.getExclude())
                ? v -> Pattern.compile(p.getExclude()).asPredicate().negate().test(v.getTitle())
                : v -> true;

        // items.forEach(i -> log.info(i.toString()));
        List<RssItem> list = items.filter(include.and(exclude));
        log.info("Found {} items with pattern {}", list.size(), p.getMatcher());
        items.filter(include.and(exclude)).forEach(i -> log.info(i.toString()));
        return list;
    }

    private List<AddTorrentByUrlArguments> toArguments(Tuple2<PatternMatcher, List<RssItem>> infos) {
        PatternMatcher elem = infos._1();
        return infos._2().map(v -> AddTorrentByUrlArguments.builder()
                .paused(true)
                .downloadDir(elem.getDownloadPath())
                .filename(v.getLink().toString())
                .build()
        );
    }
}
