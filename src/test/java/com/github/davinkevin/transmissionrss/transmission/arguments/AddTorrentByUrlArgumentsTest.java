package com.github.davinkevin.transmissionrss.transmission.arguments;

import com.github.davinkevin.transmissionrss.transmission.config.JacksonConfig;
import com.github.davinkevin.transmissionrss.transmission.request.AddTorrentRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by kevin on 08/04/2018
 */
@JsonTest
@RunWith(SpringRunner.class)
@Import({JacksonConfig.class})
public class AddTorrentByUrlArgumentsTest {


    @Autowired
    private JacksonTester<AddTorrentRequest<AddTorrentArguments>> json;

    @Test
    public void should_serialize() throws IOException {
        /* GIVEN */
        AddTorrentByUrlArguments args = AddTorrentByUrlArguments.builder()
                .filename("http://releases.ubuntu.com/17.10/ubuntu-17.10.1-desktop-amd64.iso.torrent")
                .downloadDir("/data/download/")
                .paused(true)
                .build();
        AddTorrentRequest<AddTorrentArguments> request = new AddTorrentRequest<>(args);

        /* WHEN  */
        JsonContent<AddTorrentRequest<AddTorrentArguments>> serialized = this.json.write(request);

        /* THEN  */
        assertThat(serialized).hasJsonPathStringValue("@.method");
        assertThat(serialized).hasJsonPathStringValue("@.arguments.filename");
        assertThat(serialized).hasJsonPathStringValue("@.arguments.download-dir");
        assertThat(serialized).hasJsonPathBooleanValue("@.arguments.paused");
        assertThat(serialized).hasJsonPathStringValue("@.tag");

        assertThat(serialized).extractingJsonPathStringValue("@.method").isEqualTo("torrent-add");
        assertThat(serialized).extractingJsonPathStringValue("@.arguments.filename").isEqualTo("http://releases.ubuntu.com/17.10/ubuntu-17.10.1-desktop-amd64.iso.torrent");
        assertThat(serialized).extractingJsonPathStringValue("@.arguments.download-dir").isEqualTo("/data/download/");
        assertThat(serialized).extractingJsonPathBooleanValue("@.arguments.paused").isEqualTo(true);
    }

}