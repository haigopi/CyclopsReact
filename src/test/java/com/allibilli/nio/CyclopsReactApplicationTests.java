package com.allibilli.nio;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CyclopsReactApplicationTests {

    @Test
    public void contextLoads() {



        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        requestHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        requestHeaders.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> request = new HttpEntity<>("{\"name\": \"Gopi\",\"job\": \"Engineer\"}", requestHeaders);

        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();

        clientHttpRequestFactory.setConnectTimeout(5000);
        clientHttpRequestFactory.setReadTimeout(5000);

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        restTemplate.exchange("https://reqres.in/api/users", HttpMethod.POST, request, Object.class);


    }


}
