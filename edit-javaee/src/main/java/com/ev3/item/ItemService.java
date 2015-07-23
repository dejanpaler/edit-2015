package com.ev3.item;

import java.io.StringReader;
import java.util.Collection;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ev3.brick.BrickConnection;
import com.ev3.startup.StartupEvent;

@Path("/items")
public class ItemService {

    @Inject
    Items items;

    @Inject
    BrickConnection BC;

    public void createSampleTodoItems(@Observes StartupEvent startupEvent) {
        /*
         * items.ClearDatabase(); items.createItem("prvi", 1, -1, direction.up);
         * items.createItem("drugi", 2, 1, direction.up);
         * items.createItem("tretji", 1, -1, direction.down);
         */
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllItems() {
        Collection<Item> allItems = items.findAllItems();
        GenericEntity<Collection<Item>> list = new GenericEntity<Collection<Item>>(allItems) {
        };

        return Response.ok(list).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createItem(Item item) {
        Item newItem = items.createItem(item.getTitle(), item.getCoorX(), item.getCoorY(), item.getDirection());
        return Response.status(Response.Status.CREATED).entity(newItem).build();
    }

    @GET
    @Path("/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItem(@PathParam("itemId") String itemId) {
        Item item = items.findItem(itemId);

        return Response.ok(item).build();
    }

    @POST
    @Path("/create")
    public Response create(String s) {

        // Item newItem = items.createItem(s, coorX, coorY, d);

        return Response.ok().build();
    }

    @POST
    @Path("/get")
    public Response commandGet(String command) {
        try {
            final JsonObject jsonCommand = Json.createReader(new StringReader(command)).readObject();

            Item item = items.findItem(jsonCommand.getString("id"));
            BC.setId(jsonCommand.getString("id"));
            if (item != null) {
                String coords = Integer.toString(item.getCoorX()) + ";" + Integer.toString(item.getCoorY()) + ";"
                        + Integer.toString(item.getDirection().ordinal());
                JsonObject order = Json.createObjectBuilder().add("command", "get").add("data", coords).build();
                BC.sendCommand(order.toString());

                return Response.ok(order).build();
            }

            else {
                return Response.ok(Status.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok(e.getStackTrace()).build();
        }
    }

    @POST
    @Path("/put")
    @Produces
    public Response commandPut(String command) {
        try {
            final JsonObject jsonCommand = Json.createReader(new StringReader(command)).readObject();

            Location location = items.AddItem(jsonCommand.getString("title"));

            if (location != null) {
                String data = Integer.toString(location.getRow()) + ";" + Integer.toString(location.getCol()) + ";"
                        + Integer.toString(location.getDirection().ordinal());
                JsonObject order = Json.createObjectBuilder().add("command", "put").add("data", data).build();
                BC.sendCommand(order.toString());

                return Response.ok().build();
            } else {
                return Response.status(Status.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok(e.getStackTrace()).build();
        }
    }

    @POST
    @Path("/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public Response commandEdit(String command) {
        try {
            final JsonObject jsonCommand = Json.createReader(new StringReader(command)).readObject();

            if (items.EditItem(jsonCommand.getString("id"), jsonCommand.getString("title"))) {
                return Response.ok().build();
            } else {
                return Response.status(Status.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok(e.getStackTrace()).build();
        }
    }
}
