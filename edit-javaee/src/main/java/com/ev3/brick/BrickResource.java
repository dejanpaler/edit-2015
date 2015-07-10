package com.ev3.brick;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/bricks")
public class BrickResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBricks() {

        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        objectBuilder.add("name", "joze");
        objectBuilder.add("age", "222");

        return Response.ok(objectBuilder.build()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @PathParam("brickId")
    public Response createBricks(String brickId) {

        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        objectBuilder.add("name", "joze");
        objectBuilder.add("age", "222");

        return Response.ok(objectBuilder.build()).build();
    }

}
