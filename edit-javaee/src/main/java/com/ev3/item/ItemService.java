package com.ev3.item;

import java.io.IOException;
import java.util.Collection;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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

    public void createSampleTodoItems(@Observes StartupEvent startupEvent) {
    	items.createItem("prvi", 0, 0, direction.left);
    	items.createItem("drugi", 4, -2, direction.left);
    	items.createItem("tretji", 3, 7, direction.right);
    	items.createItem("test", 4, 4, direction.right);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllItems() {
        Collection<Item> allItems = items.findAllItems();
        GenericEntity<Collection<Item>> list = new GenericEntity<Collection<Item>>(
                allItems) {
        };

        return Response.ok(list).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createItem(Item item) {
        Item newItem = items.createItem(item.getTitle(), item.getCoorX(), item.getCoorY(), item.getDirection());
        return Response.status(Response.Status.CREATED).entity(newItem).build();
    }

/*    @GET
    @Path("/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItem(@PathParam("itemId") String itemId) {
        Item item = items.findItem(itemId);

        return Response.ok(item).build();
    }*/
    @POST
    @Path("/go")
    public Response test(String go){

        try {
            BC.sendCommand(go);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok(go).build();
    }

    @POST
    @Path("/getItem")
    public Response getItem(String id){
           Item item = items.findItem(id);
           int x=item.getCoorX();
           int y=item.getCoorY();
           int dir=item.getDirection().ordinal();
           String order = Integer.toString(x)+";"+Integer.toString(y)+";"+Integer.toString(dir);
           try {
            BC.sendCommand(order);
            return Response.ok("Order sent").build();
        } catch (Exception e) {
            return Response.status(406).build();
        }

    }


}
