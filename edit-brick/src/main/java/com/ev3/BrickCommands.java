package com.ev3;

import io.undertow.server.session.Session;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;

import lejos.hardware.Sound;
import lejos.hardware.Button;
import lejos.hardware.motor.*;
import lejos.utility.Delay;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;

class Order
{
    public int x, y, side, getItem;
}

public class BrickCommands extends AbstractReceiveListener {

    private WebSocketChannel channel;
    public Boolean directControl = false;

    @Override
    protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {

    	String json = message.getData();
        Log.info("Trying to parse JSON:" + json);

        try {
        	final JsonObject jsonCommand = Json.createReader(new StringReader(json)).readObject();
            System.out.println("Parsed: " + jsonCommand.toString());
            final String command = jsonCommand.getString("command");
            //respond to client
            if (command.isEmpty())
                Log.info("No command given.");
            else if (command.equals("MoveToLocation")) {
                final String data = jsonCommand.getString("data");
                Order order = ParseCommand(data);
                MoveToLocation(order);
            } else if (command.equals("terminate")) {
                Log.info("Press escape to exit.");
                if (Button.waitForAnyPress() == Button.ID_ESCAPE) {
                    System.exit(0);
                }
            } else if (command.equals("control")) {
            	directControl = true;
            } else if(directControl){
            	switch (command){
            	case "grab":
            		PickupItem();
            		break;

            	case "drop":
            		DropItem();
            		break;

            	case "turn right":
            		Turn("right");
            		break;

            	case "turn left":
            		Turn("left");
            		break;

            	case "forward":
                    final String data = jsonCommand.getString("data");
                    int howFar = Integer.parseInt(data);
            		Forward(360);
            		Delay.msDelay(howFar*1000);
            		break;

            	case "nocontrol":
            		directControl = false;
            		break;
            	}
            } else {
                Log.info("Command " + command + " doesn't exist.");
            }
        } catch (JsonException je) {
            System.out.print("Couldn't parse JSON.");
            System.out.print(je.getMessage());
        }
    }

    private Order ParseCommand(String message) {
    	int i = message.indexOf(";");
    	int j = message.indexOf(";", i+1);
    	int k = message.indexOf(";", j+1);
    	Order order = new Order();
    	order.x = Integer.parseInt(message.substring(0, i));
    	order.y = Integer.parseInt(message.substring(i+1, j));
    	order.side = Integer.parseInt(message.substring(j+1, k));
    	order.getItem = Integer.parseInt(message.substring(k+1));
        return order;
    }

    private void MoveToLocation(Order order) {
        // Send robot to X Y
    	Boolean sideOfStorage = true;
    	if(order.x<0){
    		sideOfStorage = false;
    		order.x = order.x -(2*order.x);
    	}
    	Delay.msDelay(1000);
        Sound.beepSequenceUp();
        Log.info("Sending robot to location");

    	Motor.B.forward();
    	Motor.C.forward();

        Button.LEDPattern(0);
    	Delay.msDelay(10000);

        Motor.B.stop();
        Motor.C.stop();
        Sound.beepSequence();
        Log.info("Robot at location.");
    }

    private void PickupItem() {
        Log.info("Picking up item");
    	//move forward

	   	Motor.A.rotate(-180);
	   	Delay.msDelay(1000);
	   	Motor.A.rotate(180);
	   	Delay.msDelay(1000);
        Motor.A.forward();
        Delay.msDelay(1000);

    	//move backward
        Button.LEDPattern(2);
        Log.info("Item picked up");
    }

    private void DropItem() {
        Log.info("Dropping..:");
    	//move forward

	   	Motor.A.backward();
	    Delay.msDelay(1000);
	    Motor.A.rotate(-180);
	    Motor.B.setSpeed(720);// 2 RPM 720
	    Motor.C.setSpeed(720);
	    Motor.B.backward();
	    Motor.C.backward();

    	//move backward
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
    private static void ForwardTest(){
    	System.out.print("Press Back, when you want to stop!");
    	Motor.B.forward();
    	Motor.C.forward();
    	while(true){
        	if (Button.waitForAnyPress() == Button.ID_ESCAPE) {
            	Motor.B.stop();
            	Motor.C.stop();
        		break;
        	}
    	}
    }
    private static void Align(){
    	Motor.A.backward();
    	Delay.msDelay(1000);
    	Motor.A.stop();
    }
    public static void Forward(int i){
    	Motor.B.setSpeed(i);// 2 RPM 720
        Motor.C.setSpeed(i);
        Motor.B.forward();
        Motor.C.forward();
    }
    public static void Backward(int i){
    	Motor.B.setSpeed(i);// 2 RPM 720
        Motor.C.setSpeed(i);
        Motor.B.backward();
        Motor.C.backward();
    }
    public static void Stop(){
        Motor.B.stop();
        Motor.C.stop();
    }
}
