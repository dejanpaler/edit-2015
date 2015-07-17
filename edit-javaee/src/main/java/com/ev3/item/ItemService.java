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

import com.ev3.brick.device.BrickClientEndpoint;
import com.ev3.startup.StartupEvent;

@Path("/items")
public class ItemService {

    @Inject
    Items items;

    @Inject
    BrickClientEndpoint BC;

    @Inject
    Items I;

    public void createSampleTodoItems(@Observes StartupEvent startupEvent) {
        /*
         * int i = 20; for (int j = 1; j <= i; j++) { String title = "Item #" +
         * j; items.createItem(title); }
         */

        // items.createItem(title)

        items.createItem("prvi", 0, 0, direction.up);
        items.createItem("drugi", 4, -2, direction.up);
        items.createItem("tretji", 3, 7, direction.down);

        boolean a = items.CheckFreeLocation(0, 0, direction.up);
        boolean b = items.CheckFreeLocation(0, 0, direction.down);

        System.out.println("REZ: " + a + " " + b);
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
    @Path("/getItem")
    public Response getItemById(String id) {
        Item item = items.findItem(id);
        int coordX = item.getCoorX();
        int coordY = item.getCoorY();
        int direction = item.getDirection().ordinal();
        String order = Integer.toString(coordX) + ";" + Integer.toString(coordY) + ";" + Integer.toString(direction);
        try {
            // BC.sendCommand(order);
            return Response.ok("Order sent").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(406).build();
        }

    }

    @POST
    @Path("/do")

    public Response command(String command) {
        System.out.println("Got order");
        try {
            final JsonObject jsonCommand = Json.createReader(new StringReader(command)).readObject();
            if (jsonCommand.getString("command").equals("get")) {
                Item item = items.findItem(jsonCommand.getString("id"));
                String coords = Integer.toString(item.getCoorX()) + ";" + Integer.toString(item.getCoorY()) + ";"
                        + Integer.toString(item.getDirection().ordinal());
                JsonObject order = Json.createObjectBuilder().add("command", "get").add("data", coords).build();
                BC.sendCommand(order.toString());
                return Response.ok(order).build();
            } else {
                Location location = items.AddItem(jsonCommand.getString("title"));
                if (location != null) {
                    String data = Integer.toString(location.getCol()) + ";" + Integer.toString(location.getRow()) + ";"
                            + Integer.toString(location.getDirection().ordinal());
                    JsonObject order = Json.createObjectBuilder().add("command", "put").add("data", data).build();
                    BC.sendCommand(order.toString());
                    return Response.ok("Item: \"" + jsonCommand.getString("title") + "\" added to storage.").build();
                } else {
                    return Response.ok("Storage is full").build();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok(e.getStackTrace()).build();
        }

    }
}
