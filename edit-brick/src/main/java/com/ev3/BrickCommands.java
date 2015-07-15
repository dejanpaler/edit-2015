package com.ev3;

import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import lejos.hardware.Button;

public class BrickCommands extends AbstractReceiveListener {

    private WebSocketChannel channel;

    @Override
    protected void onFullTextMessage(WebSocketChannel channel,
            BufferedTextMessage message) {
        String msg = message.getData();
        Log.info("Trying to parse msg:" + msg);
        this.channel = channel;
        try {
            System.out.println(msg);
        } catch ( Exception e) {
            System.out.print("Couldn't parse Txt.");
            System.out.print(e.getMessage());
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
