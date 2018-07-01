package com.github.davinkevin.transmissionrss.config

import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.items.FeedProcessor
import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.items.FeedUrlReader
import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.items.FeedWithItems
import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.items.RssItemToDatabaseWriter
import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.specification.FeedSpecification
import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.specification.FeedsSpecificationDatabaseWriter
import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.specification.FeedsSpecificationReader
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URL

@Configuration
@EnableBatchProcessing
class BatchConfiguration(
        val jobBuilderFactory: JobBuilderFactory,
        val stepBuilderFactory: StepBuilderFactory
) {
    @Bean
    fun processFeedStep(loadFeedSpecificationToDatabase: Step, loadRssItemsToDatabase: Step): Job =
            jobBuilderFactory.get("syncToTransmission")
            .start(loadFeedSpecificationToDatabase)
            .next(loadRssItemsToDatabase)
            .build()

    @Bean
    fun loadRssItemsToDatabase(feedUrlReader: FeedUrlReader, processor: FeedProcessor, writer: RssItemToDatabaseWriter): Step =
            stepBuilderFactory.get("loadRssItemsToDatabase")
                    .chunk<URL, FeedWithItems>(1)
                    .reader(feedUrlReader)
                    .processor(processor)
                    .writer(writer)
                    .allowStartIfComplete(true)
                    .build()

    @Bean
    fun loadFeedSpecificationToDatabase(reader: FeedsSpecificationReader, writer: FeedsSpecificationDatabaseWriter): Step =
            stepBuilderFactory.get("loadFeedSpecificationToDatabase")
                    .chunk<FeedSpecification, FeedSpecification>(5)
                    .reader(reader)
                    .writer(writer)
                    .allowStartIfComplete(true)
                    .build()
}

