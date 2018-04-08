package com.github.davinkevin.transmissionrss.batch.config;

import com.github.davinkevin.transmissionrss.batch.processor.FeedProcessor;
import com.github.davinkevin.transmissionrss.batch.reader.FeedReader;
import com.github.davinkevin.transmissionrss.batch.writer.AddTorrentWriter;
import com.github.davinkevin.transmissionrss.feeds.model.Feed;
import com.github.davinkevin.transmissionrss.transmission.arguments.AddTorrentArguments;
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
    public Job processFeedStep(Step processFeedsStep) {
        return jobBuilderFactory.get("processFeedsStep")
                .start(processFeedsStep)
                .build();
    }

    @Bean
    public Step processFeedsStep(FeedReader feedReader, FeedProcessor processor, AddTorrentWriter writer) {
        return stepBuilderFactory.get("processFeedsStep")
                .<Feed, List<AddTorrentArguments>> chunk(1)
                .reader(feedReader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
