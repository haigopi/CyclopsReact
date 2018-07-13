package com.allibilli.nio;

import com.allibilli.nio.async.AsyncMaker;
import com.allibilli.nio.sync.SyncMaker;
import cyclops.async.SimpleReact;
import net.javacrumbs.futureconverter.springjava.FutureConverter;
import org.jooq.lambda.tuple.Tuple3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpMethod;

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

        String payload = "{\"name\": \"Gopi\",\"job\": \"Engineer\"}";
        //POST
        ConcurrentLinkedQueue<Tuple3<String, String, HttpMethod>> linkedPayloadQueue = new ConcurrentLinkedQueue();


        Tuple3<String, String, HttpMethod> r1 = new Tuple3("https://reqres.in/api/users", payload, HttpMethod.POST);
        Tuple3<String, String, HttpMethod> r2 = new Tuple3("https://reqres.in/api/users", payload, HttpMethod.POST);
        Tuple3<String, String, HttpMethod> r3 = new Tuple3("https://reqres.in/api/users", payload, HttpMethod.POST);
        Tuple3<String, String, HttpMethod> r4 = new Tuple3("https://reqres.in/api/users", payload, HttpMethod.POST);
        Tuple3<String, String, HttpMethod> r5 = new Tuple3("https://reqres.in/api/users", payload, HttpMethod.POST);
        Tuple3<String, String, HttpMethod> r6 = new Tuple3("https://reqres.in/api/users", payload, HttpMethod.POST);
        Tuple3<String, String, HttpMethod> r7 = new Tuple3("https://reqres.in/api/users", payload, HttpMethod.POST);
        Tuple3<String, String, HttpMethod> r8 = new Tuple3("https://reqres.in/api/users", payload, HttpMethod.POST);
        linkedPayloadQueue.add(r1);
//        linkedPayloadQueue.add(r2);
//        linkedPayloadQueue.add(r3);
//        linkedPayloadQueue.add(r4);
//        linkedPayloadQueue.add(r5);
//        linkedPayloadQueue.add(r6);
//        linkedPayloadQueue.add(r7);
//        linkedPayloadQueue.add(r8);

        //GET
        ConcurrentLinkedQueue<Tuple3<String, String, HttpMethod>> linkedQueue = new ConcurrentLinkedQueue();


        Tuple3<String, String, HttpMethod> r9 = new Tuple3("https://reqres.in/api/users/2?delay=3", payload, HttpMethod.GET);
        Tuple3<String, String, HttpMethod> r10 = new Tuple3("https://reqres.in/api/users/2", payload, HttpMethod.GET);
        Tuple3<String, String, HttpMethod> r11 = new Tuple3("https://reqres.in/api/users/2", payload, HttpMethod.GET);
        Tuple3<String, String, HttpMethod> r12 = new Tuple3("https://reqres.in/api/users/2", payload, HttpMethod.GET);
        Tuple3<String, String, HttpMethod> r13 = new Tuple3("https://reqres.in/api/users/2", payload, HttpMethod.GET);
        Tuple3<String, String, HttpMethod> r14 = new Tuple3("https://reqres.in/api/users/2?delay=3", payload, HttpMethod.GET);
        Tuple3<String, String, HttpMethod> r15 = new Tuple3("https://reqres.in/api/users/2", payload, HttpMethod.GET);
        Tuple3<String, String, HttpMethod> r16 = new Tuple3("https://reqres.in/api/users/2", payload, HttpMethod.GET);
        linkedQueue.add(r9);
//        linkedQueue.add(r10);
//        linkedQueue.add(r11);
//        linkedQueue.add(r12);
//        linkedQueue.add(r13);
//        linkedQueue.add(r14);
//        linkedQueue.add(r15);
//        linkedQueue.add(r16);


        //POST
        local.initiateOutboundSyncReactor(linkedPayloadQueue);
        local.initiateOutboundAsyncReactor(linkedPayloadQueue);


        //GET
        local.initiateOutboundSyncReactor(linkedPayloadQueue);
        local.initiateOutboundAsyncReactor(linkedPayloadQueue);
    }

    public void initiateOutboundSyncReactor(ConcurrentLinkedQueue<Tuple3<String, String, HttpMethod>> q) {

        new SimpleReact().
                from(q.stream().map(it -> syncMaker.post(q.poll()))).
                then(resp -> syncMaker.handleSyncResponse(resp)).
                onFail(e -> syncMaker.handleError(e, e.getValue()));

    }

    public void initiateOutboundAsyncReactor(ConcurrentLinkedQueue<Tuple3<String, String, HttpMethod>> q) {

        new SimpleReact().
                fromStream(q.stream().map(it -> FutureConverter.toCompletableFuture(asyncMaker.post(q.peek())))).
                then(resp -> asyncMaker.handleAsyncResponse(resp)).
                onFail(e -> asyncMaker.handleError(e, e.getValue()));

    }


}
