package com.allibilli.nio.api;

import com.allibilli.nio.async.AsyncMaker;
import com.allibilli.nio.sync.SyncMaker;
import cyclops.async.SimpleReact;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.futureconverter.springjava.FutureConverter;
import org.jooq.lambda.tuple.Tuple3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Service
public class AppService {

    String payload;

    //POST
    ConcurrentLinkedQueue<Tuple3<String, String, HttpMethod>> post;
    //GET
    ConcurrentLinkedQueue<Tuple3<String, String, HttpMethod>> get;

    @Autowired
    SyncMaker syncMaker;

    @Autowired
    AsyncMaker asyncMaker;


    @PostConstruct
    public void init() {
        payload = "{\"name\": \"Gopi\",\"job\": \"Engineer\"}";
        post = new ConcurrentLinkedQueue();
        get = new ConcurrentLinkedQueue();

    }


    public void createGetQ(){

        for (int i = 0; i <= 10; ++i) {

            Tuple3<String, String, HttpMethod> getTuple = new Tuple3("https://reqres.in/api/users/2?delay=3", payload, HttpMethod.GET);
            get.add(getTuple);

        }
    }

    public void createPostQ(){

        for (int i = 0; i <= 10; ++i) {

            Tuple3<String, String, HttpMethod> postTuple = new Tuple3("https://reqres.in/api/users", payload, org.springframework.http.HttpMethod.POST);
            post.add(postTuple);

        }
    }


    public void initiatePost() {
        createPostQ();
        log.info("ASYNC + SYNC POST: ");
        initiateOutboundAsyncReactor(post);
        initiateOutboundSyncReactor(post);
    }

    public void initiateGet() {
        createGetQ();
        log.info("ASYNC + SYNC GET: ");
        initiateOutboundAsyncReactor(get);
        initiateOutboundSyncReactor(get);
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
