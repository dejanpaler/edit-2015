package com.ev3.brick;

import javax.websocket.MessageHandler;

public class BrickMessages implements MessageHandler.Whole<String>{

    private final BrickCommandsEndpoint commandsEndpoint;

    public BrickMessages(BrickCommandsEndpoint commandsEndpoint){
        this.commandsEndpoint = commandsEndpoint;
    }

    @Override
    public void onMessage(String message) {
        commandsEndpoint.sendMessage(message);
    }
}
