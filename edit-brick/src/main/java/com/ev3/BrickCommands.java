package com.ev3;

import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;

import lejos.hardware.Button;
import lejos.utility.Delay;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;

public class BrickCommands extends AbstractReceiveListener {

    private WebSocketChannel channel;

    @Override
    protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {

    	Log.info(message.toString());
        this.channel = channel;
    	int x = ParseX(message.toString());
    	int y = ParseY(message.toString());
    	MoveToLocation(x, y);
    	
    	
    	//String json = message.getData();
        //Log.info("Trying to parse JSON:" + json);
       

        /*
        try {
        	final JsonObject jsonCommand = Json.createReader(new StringReader(json)).readObject();
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
        */
    }
    
    private Integer ParseX(String message) {
    	int i = message.indexOf(";");
    	int x = Integer.parseInt(message.substring(0, i));
        return x;
    }
    
    private Integer ParseY(String message) {
    	int i = message.indexOf(";");
    	int y = Integer.parseInt(message.substring(i));
        return y;
    }
    
    private void MoveToLocation(int x, int y) {
        // Send robot to X Y
    	Delay.msDelay(1000);
        Log.info("Sending robot to location");
        //WebSockets.sendText("[ev3.brick] > Sending robot to location." , channel, null);
        
        /*
        for(int i=0; i<y; i++){
        	MoveToNextIntersection();
        }
        TurnRight();
        for(int i=0; i<x; i++){
        	MoveToNextIntersection();
        }
        */
        
        Button.LEDPattern(0);
    	Delay.msDelay(1000);
        Log.info("Robot at location.");
        //WebSockets.sendText("[ev3.brick] > Robot at location." , channel, null);
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
    
    private void MoveToNextIntersection(){
    	//move until next yellow dot
    }
    
    private void Turn(String direction){
    	if(direction.equals("right")){
    		//turn right
    	}
    	else if (direction.equals("left")){
    		//turn left
    	}
    	else {
    		//turn around
    	}
    	
    }
}