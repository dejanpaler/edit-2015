package com.ev3.page;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class PageService {
	@GET
	public Response indexPage() {
		return Response.status(Response.Status.NOT_FOUND).build();
	}

}
