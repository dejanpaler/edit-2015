package com.ev3.brick.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mysql.jdbc.util.ResultSetUtil;

@Path("/items")
public class ItemResource {
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllItems() throws SQLException {
		//Class.forName("com.mysql.jdbc.Driver");
		JsonObjectBuilder json = Json.createObjectBuilder();
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/edit-dev?user=root&password=");
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM items");
		int i = 0;
		while(resultSet.next()) {
			JsonObjectBuilder obj = Json.createObjectBuilder();
			obj.add("id", resultSet.getString("id"));
			obj.add("title", resultSet.getString("title"));
			obj.add("locationX", resultSet.getString("locationX"));
			obj.add("locationY", resultSet.getString("locationY"));
			json.add(Integer.toString(i), obj);
			i++;
		}
		return Response.ok(json.build()).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@GET
	@Path("/{itemId: [0-9]+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getItem(@PathParam("itemId") String id) throws SQLException {
		int itemId = Integer.parseInt(id);
		JsonObjectBuilder json = Json.createObjectBuilder();
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/edit-dev?user=root&password=");
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM items WHERE id = " + itemId);		
		if(resultSet.next()){
			json.add("id", resultSet.getString("id"));
			json.add("title", resultSet.getString("title"));
			json.add("locationX", resultSet.getString("locationX"));
			json.add("locationY", resultSet.getString("locationY"));
		}

		return Response.ok(json.build()).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
	@Path("/order")
	@Consumes(MediaType.APPLICATION_JSON)
	public void postOrder() {
		//TODO: send order to brick
	}
}
