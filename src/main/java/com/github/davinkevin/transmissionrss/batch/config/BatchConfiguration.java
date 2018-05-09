package com.github.davinkevin.transmissionrss.batch.config;

import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.FeedDatabaseReader;
import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.FeedDatabaseWriter;
import com.github.davinkevin.transmissionrss.batch.synchronization.items.FeedProcessor;
import com.github.davinkevin.transmissionrss.batch.synchronization.items.FeedReader;
import com.github.davinkevin.transmissionrss.batch.synchronization.items.RssItemToDatabaseWriter;
import com.github.davinkevin.transmissionrss.batch.synchronization.items.model.ItemFetchingSpecification;
import com.github.davinkevin.transmissionrss.rss.model.RssItem;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by kevin on 08/04/2018
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job processFeedStep(Step processFeedsStep, Step synchronizeFeeds) {
        return jobBuilderFactory.get("processFeedsJob")
                .start(synchronizeFeeds)
                .next(processFeedsStep)
                .build();
    }

    @Bean
    public Step processFeedsStep(FeedReader feedReader, FeedProcessor processor, RssItemToDatabaseWriter writer) {
        return stepBuilderFactory.get("processFeedsStep")
                .<ItemFetchingSpecification, List<RssItem>> chunk(1)
                .reader(feedReader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step synchronizeFeeds(FeedDatabaseReader reader, FeedDatabaseWriter writer) {
        return stepBuilderFactory.get("syncFeedsStep")
                .<ItemFetchingSpecification, ItemFetchingSpecification>chunk(5)
                .reader(reader)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }
}
