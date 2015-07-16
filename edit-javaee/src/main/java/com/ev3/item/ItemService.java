package com.ev3.item;

import java.io.IOException;
import java.util.Collection;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
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

    public void createSampleTodoItems(@Observes StartupEvent startupEvent) {
        /*int i = 20;
        for (int j = 1; j <= i; j++) {
            String title = "Item #" + j;
            items.createItem(title);
        }*/
        
        //items.createItem(title)
        
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

    @GET
    @Path("/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItem(@PathParam("itemId") String itemId) {
        Item item = items.findItem(itemId);

        return Response.ok(item).build();
    }
    
    @POST
    @Path("/go")
    public Response test(String go){
        try {
            BC.sendCommand(go);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Response.ok(go).build();
    }
    
    @POST
    @Path("/create")
    public Response create(String s)
    {
        
        
        //Item newItem = items.createItem(s, coorX, coorY, d);
        
        return Response.ok().build();
    }
}