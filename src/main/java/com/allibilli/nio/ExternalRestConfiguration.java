package com.allibilli.nio;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Gopi K Kancharla
 * 7/10/18 12:47 PM
 */

@Slf4j
@Component
@Data
public class ExternalRestConfiguration {

    //private String name;
    private int timeout = 10000;
    private boolean proxyEnabled = false;
    private String proxyHost = "localhost";
    private int proxyPort = 8080;
}
