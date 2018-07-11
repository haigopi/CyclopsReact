package com.allibilli.nio.sync;

import com.allibilli.nio.BaseCommunicator;
import com.allibilli.nio.ExternalRestConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by Gopi K Kancharla
 * 7/10/18 2:24 PM
 */
@Component
@Slf4j
public class SyncMaker extends BaseCommunicator {

    @Autowired
    ExternalRestConfiguration externalRestConfiguration;

    public ResponseEntity<Object> post(String payload) {

        ResponseEntity<Object> entity;
        try {
            entity = getSyncClient().
                    exchange(externalRestConfiguration.getEndpoint(), HttpMethod.POST, getRequest(""), Object.class);
            return entity;
        } catch (Exception e) {
            log.error("Error ", e);

        }
        return null;
    }


    public Boolean handleSyncResponse(ResponseEntity<Object> response) {


        if (Objects.isNull(response)) {
            log.error("Null Response");
            return Boolean.FALSE;
        }
        Object responseBody = response.getBody();
        if (Objects.isNull(responseBody)) {
            log.error("Null Response Body");
            return Boolean.FALSE;
        }
        log.info("Response Processed", responseBody);

        return Boolean.TRUE;
    }

}
