package com.github.davinkevin.transmissionrss.feeds.properties;

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
@SpringBootTest
@RunWith(SpringRunner.class)
@Import({FeedsProperty.class})
public class FeedsPropertyTest {

    @Autowired FeedsProperty feedsProperty;

    @Test
    public void should_be_defined() {
        assertThat(feedsProperty).isNotNull();
        System.out.println(feedsProperty.feeds);
    }

}