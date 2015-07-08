package com.ev3;

import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.spi.WebSocketHttpExchange;

public class WebSocketCallback implements WebSocketConnectionCallback {

    public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
        channel.getReceiveSetter().set(new BrickCommands());
        channel.resumeReceives();
    }

}
