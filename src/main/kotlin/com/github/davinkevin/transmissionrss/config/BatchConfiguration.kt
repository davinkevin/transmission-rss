package com.github.davinkevin.transmissionrss.config

import com.github.davinkevin.transmissionrss.batch.cleanup.RssItemFilterProcessor
import com.github.davinkevin.transmissionrss.batch.cleanup.RssItemFromDatabaseReader
import com.github.davinkevin.transmissionrss.batch.cleanup.RssItemToDeleteWriter
import com.github.davinkevin.transmissionrss.batch.cleanup.RssItemWithFeed
import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.items.FeedProcessor
import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.items.FeedUrlReader
import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.items.FeedWithItems
import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.items.RssItemToDatabaseWriter
import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.specification.FeedsSpecificationDatabaseWriter
import com.github.davinkevin.transmissionrss.batch.synchronization.feeds.specification.FeedsSpecificationReader
import com.github.davinkevin.transmissionrss.batch.transmission.start.PatternMatcherReader
import com.github.davinkevin.transmissionrss.batch.transmission.start.RssItemDownloadedWriter
import com.github.davinkevin.transmissionrss.batch.transmission.start.RssItemDownloaderProcessor
import com.github.davinkevin.transmissionrss.batch.transmission.start.RssItemToDownload
import com.github.davinkevin.transmissionrss.specification.FeedSpecification
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
    fun processFeedStep(loadFeedSpecificationToDatabase: Step, loadRssItemsToDatabase: Step, deleteUselessItemsFromDatabase: Step, downloadRssItems: Step): Job =
            jobBuilderFactory.get("syncToTransmission")
                .start(loadFeedSpecificationToDatabase)
                    .next(loadRssItemsToDatabase)
                    .next(deleteUselessItemsFromDatabase)
                    .next(downloadRssItems)
            .build()


    @Bean
    fun loadFeedSpecificationToDatabase(reader: FeedsSpecificationReader, writer: FeedsSpecificationDatabaseWriter): Step =
            stepBuilderFactory.get("loadFeedSpecificationToDatabase")
                    .chunk<FeedSpecification, FeedSpecification>(5)
                    .reader(reader)
                    .writer(writer)
                    .allowStartIfComplete(true)
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
    fun deleteUselessItemsFromDatabase(reader: RssItemFromDatabaseReader, processor: RssItemFilterProcessor, writer: RssItemToDeleteWriter): Step =
            stepBuilderFactory.get("deleteUselessItemsFromDatabase")
                    .chunk<RssItemWithFeed, RssItemWithFeed>(10000)
                    .reader(reader)
                    .processor(processor)
                    .writer(writer)
                    .allowStartIfComplete(true)
                    .build()

    @Bean
    fun downloadRssItems(reader: PatternMatcherReader, processor: RssItemDownloaderProcessor, writer: RssItemDownloadedWriter): Step =
            stepBuilderFactory.get("downloadRssItems")
                    .chunk<FeedSpecification, RssItemToDownload>(10000)
                    .reader(reader)
                    .processor(processor)
                    .writer(writer)
                    .allowStartIfComplete(true)
                    .build()
}

