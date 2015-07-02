package com.ev3.item;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/items")
public class ItemService {
	
	@Inject
	Items items;
	
	
	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllItems() {
		//Collection<Item> allItems = items.findAllItems();
		//System.out.println(gson.toJson(allItems));
		return "WORK!";
	}
}