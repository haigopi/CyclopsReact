package com.allibilli.nio;

import com.allibilli.nio.async.AsyncMaker;
import com.allibilli.nio.sync.SyncMaker;
import cyclops.async.SimpleReact;
import net.javacrumbs.futureconverter.springjava.FutureConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ConcurrentLinkedQueue;

@SpringBootApplication
public class CyclopsReactApplication {

    @Autowired
    AsyncMaker asyncMaker;

    @Autowired
    SyncMaker syncMaker;


    public static void main(String[] args) {

        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(CyclopsReactApplication.class, args);
        CyclopsReactApplication local = configurableApplicationContext.getBean(CyclopsReactApplication.class);

        //POST
        ConcurrentLinkedQueue linkedPayloadQueue = new ConcurrentLinkedQueue();

        linkedPayloadQueue.add("Message Payload 1");
        linkedPayloadQueue.add("Message Payload 2");
        linkedPayloadQueue.add("Message Payload 3");
        linkedPayloadQueue.add("Message Payload 4");

        local.initiateOutboundSyncReactor(linkedPayloadQueue);
        local.initiateOutboundAsyncReactor(linkedPayloadQueue);


        //GET
        ConcurrentLinkedQueue linkedQueue = new ConcurrentLinkedQueue();
        local.initiateOutboundSyncReactor(linkedPayloadQueue);
        local.initiateOutboundAsyncReactor(linkedPayloadQueue);
    }

    public void initiateOutboundSyncReactor(ConcurrentLinkedQueue<String> q) {

        new SimpleReact().
                from(q.stream().map(it -> syncMaker.post(q.poll()))).
                then(resp -> syncMaker.handleSyncResponse(resp)).
                onFail(e -> syncMaker.handleError(e, e.getValue()));

    }

    public void initiateOutboundAsyncReactor(ConcurrentLinkedQueue<String> q) {

        new SimpleReact().
                fromStream(q.stream().map(it -> FutureConverter.toCompletableFuture(asyncMaker.post(q.peek())))).
                then(resp -> asyncMaker.handleAsyncResponse(resp)).
                onFail(e -> asyncMaker.handleError(e, e.getValue()));

    }


}
