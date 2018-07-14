package com.allibilli.nio.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Path("tenant")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class AppController {

    @Autowired
    AppService appService;

    @GET
    public Response ping() {
        return Response.ok("Alive").build();
    }

    @GET
    public Response sampleGet() {
        appService.initiateGet();

        return Response.ok("Alive").build();
    }

    @POST
    public Response samplePost(Map payload) {

        UUID uniqueRequestId = UUID.randomUUID();
        Map<String, Object> map = new HashMap<>();
        map.put("id", uniqueRequestId);

        appService.initiatePost();

        return Response.ok(map).build();
    }

}
