package com.allibilli.nio;

import com.allibilli.nio.async.AsyncMaker;
import com.allibilli.nio.sync.SyncMaker;
import cyclops.async.SimpleReact;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.futureconverter.springjava.FutureConverter;
import org.jooq.lambda.tuple.Tuple3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpMethod;

import java.util.concurrent.ConcurrentLinkedQueue;

@SpringBootApplication
@Slf4j
public class CyclopsReactApplication {

    @Autowired
    AsyncMaker asyncMaker;

    @Autowired
    SyncMaker syncMaker;


    public static void main(String[] args) {

        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(CyclopsReactApplication.class, args);
        CyclopsReactApplication local = configurableApplicationContext.getBean(CyclopsReactApplication.class);

        String payload = "{\"name\": \"Gopi\",\"job\": \"Engineer\"}";

        //POST
        ConcurrentLinkedQueue<Tuple3<String, String, HttpMethod>> post = new ConcurrentLinkedQueue();
        //GET
        ConcurrentLinkedQueue<Tuple3<String, String, HttpMethod>> get = new ConcurrentLinkedQueue();

        Tuple3<String, String, HttpMethod> postTuple;
        Tuple3<String, String, HttpMethod> getTuple;

        for (int i = 0; i <= 10; ++i) {

            postTuple = new Tuple3("https://reqres.in/api/users", payload, HttpMethod.POST);
            post.add(postTuple);

            getTuple = new Tuple3("https://reqres.in/api/users/2?delay=3", payload, HttpMethod.GET);
            get.add(getTuple);

        }

        //ASYNC: POST + GET  TEST (60 Requests)
        log.info("ASYNC POST/GET: ");
        local.initiateOutboundAsyncReactor(post);
        local.initiateOutboundAsyncReactor(get);


        //SYNC: POST + GET TEST (60 Requests)
        log.info("SYNC POST/GET: ");
        local.initiateOutboundSyncReactor(post);
        local.initiateOutboundSyncReactor(get);




    }

    public void initiateOutboundSyncReactor(ConcurrentLinkedQueue<Tuple3<String, String, HttpMethod>> q) {

        new SimpleReact().
                from(q.stream().map(it -> syncMaker.call(q.poll()))).
                then(resp -> syncMaker.handleSyncResponse(resp)).
                onFail(e -> syncMaker.handleError(e, e.getValue()));

    }

    public void initiateOutboundAsyncReactor(ConcurrentLinkedQueue<Tuple3<String, String, HttpMethod>> q) {

        new SimpleReact().
                fromStream(q.stream().map(it -> FutureConverter.toCompletableFuture(asyncMaker.call(q.peek())))).
                then(resp -> asyncMaker.handleAsyncResponse(resp)).
                onFail(e -> asyncMaker.handleError(e, e.getValue()));

    }


}
