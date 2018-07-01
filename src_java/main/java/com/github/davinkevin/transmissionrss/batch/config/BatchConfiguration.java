package com.github.davinkevin.transmissionrss.batch.config;

import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.specification.FeedsSpecificationDatabaseWriter;
import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.specification.FeedsSpecificationReader;
import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.specification.model.FeedSpecification;
import com.github.davinkevin.transmissionrss.batch.synchronization.items.FeedProcessor;
import com.github.davinkevin.transmissionrss.batch.synchronization.items.FeedUrlReader;
import com.github.davinkevin.transmissionrss.batch.synchronization.items.RssItemToDatabaseWriter;
import com.github.davinkevin.transmissionrss.batch.synchronization.items.model.RssItemsWithFeedUrl;
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

import java.net.URL;

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
    public Job processFeedStep(Step loadRssItemsToDatabase, Step loadFeedSpecificationToDatabase) {
        return jobBuilderFactory.get("syncToTransmission")
                .start(loadFeedSpecificationToDatabase)
                .next(loadRssItemsToDatabase)
                .build();
    }

    @Bean
    public Step loadRssItemsToDatabase(FeedUrlReader feedUrlReader, FeedProcessor processor, RssItemToDatabaseWriter writer) {
        return stepBuilderFactory.get("loadRssItemsToDatabase")
                .<URL, List<RssItemsWithFeedUrl>> chunk(1)
                .reader(feedUrlReader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step loadFeedSpecificationToDatabase(FeedsSpecificationReader reader, FeedsSpecificationDatabaseWriter writer) {
        return stepBuilderFactory.get("loadFeedSpecificationToDatabase")
                .<FeedSpecification, FeedSpecification>chunk(5)
                .reader(reader)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }
}
