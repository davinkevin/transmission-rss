package com.github.davinkevin.transmissionrss.batch.writer;

import com.github.davinkevin.transmissionrss.transmission.arguments.AddTorrentArguments;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * Created by kevin on 08/04/2018
 */
@Slf4j
@Component
public class AddTorrentWriter implements ItemWriter<List<AddTorrentArguments>> {

    @Override
    public void write(java.util.List<? extends List<AddTorrentArguments>> items) throws Exception {
        List<AddTorrentArguments> i = List.ofAll(items)
                .flatMap(Function.identity());

        log.info("Number of item in writer {}", i.size());

        i.forEach(v -> log.info("In Writer: {}", v));
    }

}
