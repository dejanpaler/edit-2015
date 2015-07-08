package com.ev3;

import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import lejos.hardware.Button;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


import java.io.StringReader;

public class BrickCommands extends AbstractReceiveListener {

	private WebSocketChannel channel;

	@Override
	protected void onFullTextMessage(WebSocketChannel channel,
			BufferedTextMessage message) {
		this.channel = channel;
		try{
			final JsonObject jsonCommand = Json.createReader(
					new StringReader(message.getData())).readObject();
			System.out.println("Parsed: " + jsonCommand.toString());
			final String command = jsonCommand.getString("command");
			WebSockets.sendText("[ev3.brick] Received action " + command, channel,
					null);
	
			if (command.isEmpty())
				Log.info("No command given.");
			else if (command.equals("MoveToLocation")) {
				double x = Double.parseDouble(jsonCommand.getString("locationX"));
				double y = Double.parseDouble(jsonCommand.getString("locationY"));
				MoveToLocation(x, y);
			} else if (command.equals("MoveHome")) {
				MoveHome();
			} else {
				Log.info("Command " + command + " doesn't exist.");
			}
			if (command.equals("stop")) {
				Log.info("Press escape to exit.");
				if (Button.waitForAnyPress() == Button.ID_ESCAPE) {
					System.exit(0);
				}
			}
		} catch (JsonException je)
		{
			System.out.print("Couldn't parse JSON.");
		}
	}

	private void MoveToLocation(double x, double y) {
		// Send robot to X Y
		Log.info("Sending robot to location");
		// TODO

		Log.info("Robot at location.");
	}

	private void MoveHome() {
		Log.info("Sending robot home");
		// TODO

		Log.info("I'm home.");
	}

	private void PickupItem() {
		Log.info("Picking up item");
		// TODO

		Log.info("Item picked up");
	}

	private void DropItem() {
		Log.info("Dropping..:");
		// TODO

		Log.info("Item dropped.");
	}
}
