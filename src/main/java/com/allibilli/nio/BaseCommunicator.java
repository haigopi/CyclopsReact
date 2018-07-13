package com.allibilli.nio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Created by Gopi K Kancharla
 * 7/10/18 12:30 PM
 */
@Slf4j
@Component
public class BaseCommunicator {


    @Autowired
    ExternalRestConfiguration externalRestConfiguration;

    @PostConstruct
    public void init() {
        log.info("...");
        configureClients();
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();

        if (externalRestConfiguration.isProxyEnabled()) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(externalRestConfiguration.getProxyHost(), externalRestConfiguration.getProxyPort()));
            clientHttpRequestFactory.setProxy(proxy);
        }

        clientHttpRequestFactory.setConnectTimeout(externalRestConfiguration.getTimeout());
        clientHttpRequestFactory.setReadTimeout(externalRestConfiguration.getTimeout());
        return clientHttpRequestFactory;
    }

    public RestTemplate getSyncClient() {
        return new RestTemplate(getClientHttpRequestFactory());

    }

    public void configureClients() {
        externalRestConfiguration.setRestTemplate(getSyncClient());
        externalRestConfiguration.setAsyncRestTemplate(getAsyncClient());
    }

    public AsyncRestTemplate getAsyncClient() {

        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setTaskExecutor(new SimpleAsyncTaskExecutor());
        requestFactory.setConnectTimeout(externalRestConfiguration.getTimeout());
        requestFactory.setReadTimeout(externalRestConfiguration.getTimeout());

        if (externalRestConfiguration.isProxyEnabled()) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(externalRestConfiguration.getProxyHost(), externalRestConfiguration.getProxyPort()));
            requestFactory.setProxy(proxy);
        }

        final AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        asyncRestTemplate.setAsyncRequestFactory(requestFactory);

        return asyncRestTemplate;
    }


    public HttpEntity<String> getRequest(String payload) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        requestHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        requestHeaders.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> request = new HttpEntity<>(payload, requestHeaders);
        return request;
    }

    public Boolean handleError(Throwable exception, String message) {
        log.error("Handling Error Received: Exchange: {}, Exception: {}", message, exception);
        return Boolean.TRUE;
    }

}
