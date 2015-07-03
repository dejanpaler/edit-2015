package com.ev3.item;

import java.util.Collection;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ev3.startup.StartupEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

@Path("/items")
public class ItemService {
	
	@Inject
	Items items;
	
	
	 public void createSampleTodoItems(@Observes StartupEvent startupEvent) {
		int i = 20;
		for(int j = 1; j <= i; j++){
			String title = "Item #" + j;
			items.createItem(title);
		}
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllItems() {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(items.findAllItems());
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response createItem(Item item){
		Item newItem = items.createItem(item.getTitle());
		return Response.status(Response.Status.CREATED).build();
	}
	
	@GET
	@Path("/{itemId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getItem(@PathParam("itemId") String itemId) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Item item = items.findItem(itemId);
		return gson.toJson(item);
	}
}