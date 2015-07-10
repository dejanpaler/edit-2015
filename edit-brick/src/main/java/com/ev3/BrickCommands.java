package com.ev3;

import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import lejos.hardware.Button;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;

public class BrickCommands extends AbstractReceiveListener {

    private WebSocketChannel channel;

    @Override
    protected void onFullTextMessage(WebSocketChannel channel,
            BufferedTextMessage message) {
        String json = message.getData();
        Log.info("Trying to parse JSON:" + json);
        this.channel = channel;
        try {
            final JsonObject jsonCommand = Json.createReader(
                    new StringReader(json)).readObject();
            System.out.println("Parsed: " + jsonCommand.toString());
            final String command = jsonCommand.getString("command");
            WebSockets.sendText("[ev3.brick] Received command " + command,
                    channel, null);
            if (command.isEmpty())
                Log.info("No command given.");
            else if (command.equals("MoveToLocation")) {
                MoveToLocation();
            } else if (command.equals("MoveHome")) {
                MoveHome();
            } else if (command.equals("PickUpItem")) {
                PickupItem();
            } else if (command.equals("DropItem")) {
                DropItem();
            } else {
                Log.info("Command " + command + " doesn't exist.");
            }
            if (command.equals("Stop")) {
                Log.info("Press escape to exit.");
                if (Button.waitForAnyPress() == Button.ID_ESCAPE) {
                    System.exit(0);
                }
            }
        } catch (JsonException je) {
            System.out.print("Couldn't parse JSON.");
            System.out.print(je.getMessage());
        }
    }

    private void MoveToLocation() {
        // Send robot to X Y
        Log.info("Sending robot to location");
        Button.LEDPattern(0);
        Log.info("Robot at location.");
    }

    private void MoveHome() {
        Log.info("Sending robot home");
        // TODO
        Button.LEDPattern(1);
        Log.info("I'm home.");
    }

    private void PickupItem() {
        Log.info("Picking up item");
        // TODO
        Button.LEDPattern(2);
        Log.info("Item picked up");
    }

    private void DropItem() {
        Log.info("Dropping..:");
        // TODO
        Button.LEDPattern(3);
        Log.info("Item dropped.");
    }
}
