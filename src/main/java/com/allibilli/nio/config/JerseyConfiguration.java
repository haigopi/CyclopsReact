package com.allibilli.nio.config;

import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class JerseyConfiguration extends ResourceConfig {

    public JerseyConfiguration() {
        log.info("Jersey & MultiPart");
        packages("com.allibilli");
    }

}
