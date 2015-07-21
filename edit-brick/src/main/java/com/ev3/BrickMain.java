package com.ev3;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import lejos.hardware.Button;

public class BrickMain {

    //connect to brick bluetooth host
    private static final String uri = "ws://10.0.1.11:8080/edit-javaee/brick";

    //private static final String uri = "ws://10.80.49.2:8081/edit-javaee/brick";

    //private Session session;

    public static void main(String... argv) {
        final WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
        try {
            webSocketContainer.connectToServer(new BrickClient(), URI.create(uri));
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        }
        Log.info("Connected to " + uri);

        BrickMain main = new BrickMain();
        main.onKeyTouchExit();
    }
    private void onKeyTouchExit() {
        if (Button.waitForAnyPress() == Button.ID_ESCAPE) {
            System.exit(0);
        }
        System.exit(0);
    }
}
