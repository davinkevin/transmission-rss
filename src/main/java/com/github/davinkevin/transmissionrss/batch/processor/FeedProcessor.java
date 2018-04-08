package com.github.davinkevin.transmissionrss.batch.processor;

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
    public List<AddTorrentArguments> process(Feed feed) throws Exception {
        List<RssItem> items = rssTorrentService
                .parse(feed.getUrl())
                .map(v -> v.getRootElement().getChild("channel").getChildren("item"))
                .map(List::ofAll)
                .getOrElse(List::empty)
                .map(RssItem::toRssItem);

        return feed.getRegexp()
                .map(v -> Tuple(v, filterOn(v, items)))
                .peek(v -> log.info("Found {} items with pattern {}", v._2().size(), v._1().getMatcher()))
                .flatMap(this::toArguments);
    }

    private List<RssItem> filterOn(PatternMatcher p, List<RssItem> items) {
        Predicate<RssItem> include = (RssItem v) -> Pattern.compile(p.getMatcher()).asPredicate().test(v.getTitle());
        Predicate<RssItem> exclude = nonNull(p.getExclude())
                ? (RssItem v) -> Pattern.compile(p.getExclude()).asPredicate().negate().test(v.getTitle())
                : (RssItem v) -> true;

        return items.filter(include.and(exclude));
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
