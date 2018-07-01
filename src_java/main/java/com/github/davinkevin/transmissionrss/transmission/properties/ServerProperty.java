package com.github.davinkevin.transmissionrss.transmission.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by kevin on 08/04/2018
 */
@Configuration
@Getter @Setter @Accessors(chain = true)
@ConfigurationProperties("server")
public class ServerProperty {
    private String host;
    private Integer port;
    private String rpcPath;
}
