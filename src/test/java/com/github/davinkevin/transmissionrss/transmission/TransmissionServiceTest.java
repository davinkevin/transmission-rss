package com.github.davinkevin.transmissionrss.transmission;

import com.github.davinkevin.transmissionrss.transmission.arguments.AddTorrentByUrlArguments;
import com.github.davinkevin.transmissionrss.transmission.config.JacksonConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by kevin on 08/04/2018
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@Import({JacksonConfig.class})
public class TransmissionServiceTest {

    @Autowired TransmissionService service;

    @Test
    public void should_load() {
        /* GIVEN */
        /* WHEN  */
        /* THEN  */
        assertThat(service).isNotNull();
    }

    @Test
    public void should_fetch_token() {
        /* GIVEN */
        /* WHEN  */
        /* THEN  */
        assertThat(service.getToken()).isEqualToIgnoringCase("tVYzO7Fxc8BCL1w9cQskZU8cAYJ8VCdlxzBoDifZ5ZOZQm0B");
    }

    @Test
    public void should_add_torrent_by_url() {
        /* GIVEN */
        AddTorrentByUrlArguments arg = AddTorrentByUrlArguments.builder()
                .filename("http://releases.ubuntu.com/17.10/ubuntu-17.10.1-desktop-amd64.iso.torrent")
                .downloadDir("/data/download/")
                .paused(true)
                .build();

        /* WHEN  */
        service.add(arg);

        /* THEN  */
    }

}