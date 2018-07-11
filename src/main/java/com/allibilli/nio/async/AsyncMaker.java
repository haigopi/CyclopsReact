package com.allibilli.nio.async;

import com.allibilli.nio.BaseCommunicator;
import com.allibilli.nio.ExternalRestConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * Created by Gopi K Kancharla
 * 7/10/18 2:17 PM
 */
@Component
@Slf4j
public class AsyncMaker extends BaseCommunicator {


    @Autowired
    ExternalRestConfiguration externalRestConfiguration;

    public Boolean handleAsyncResponse(ResponseEntity<Object> responseEntity) {

        log.info("Async Response Received: {}", responseEntity.getStatusCode());
        return Boolean.TRUE;
    }

    public ListenableFuture<ResponseEntity<Object>> post(String lenderId) {

        ListenableFuture<ResponseEntity<Object>> entity = null;
        try {

            entity = externalRestConfiguration.getAsyncRestTemplate().
                    exchange(externalRestConfiguration.getEndpoint(), HttpMethod.POST, getRequest(""), Object.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return entity;

    }
}
