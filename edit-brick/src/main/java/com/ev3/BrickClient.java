package com.ev3;

import java.io.IOException;
import java.util.Queue;

import javax.websocket.ClientEndpoint;
import javax.websocket.MessageHandler;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import lejos.hardware.Button;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
/*
class Order
{
    public int x, y, side, getItem;
}
*/
@ClientEndpoint
public class BrickClient {

    //connect to brick bluetooth host
    //private static final String uri = "ws://10.0.1.0:8081/brick";

    //private static final String uri = "ws://10.80.49.2:8081/brick";

    private Session session;
    private Queue<String> commandQueue;

    /*
    @PostConstruct
    public void connectBrickEndpoint() {
        final WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
        try {
            webSocketContainer.connectToServer(this, URI.create(uri));
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        }
    }
    */

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }
    public void addMessageHandler(MessageHandler.Whole<String> handler) {
        this.session.addMessageHandler(handler);
    }
    public void sendCommand(String command) throws IOException {
        this.session.getBasicRemote().sendText(command);
    }
    @OnMessage
    public void onMessage(String json, Session session) {
        this.session = session;
        try {
            final JsonObject jsonCommand = Json.createReader(new StringReader(json)).readObject();
            System.out.println("Parsed: " + jsonCommand.toString());
            final String command = jsonCommand.getString("command");
            //WebSockets.sendText("[ev3.brick] Received command " + command, channel, null);
            if (command.isEmpty())
                Log.info("No command given.");
            else if (command.equals("get")) {
                final String data = jsonCommand.getString("data");
                Order order = ParseCommand(data);
                order.getItem = 1;
                //MoveToLocation(order);
            } else if (command.equals("put")) {
                final String data = jsonCommand.getString("data");
                Order order = ParseCommand(data);
                order.getItem = 0;
                //MoveToLocation(order);
            } else if (command.equals("terminate")) {
                Log.info("Press escape to exit.");
                if (Button.waitForAnyPress() == Button.ID_ESCAPE) {
                    System.exit(0);
                }
            } else if (command.equals("control")) {
                //directControl = true;
            }
            /*else if(directControl){
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
            }*/
            else {
                Log.info("Command " + command + " doesn't exist.");
            }
        } catch (JsonException je) {
            System.out.print(json);
        }
    }

    private Order ParseCommand(String message) {
        int i = message.indexOf(";");
        int j = message.indexOf(";", i+1);
        Order order = new Order();
        order.x = Integer.parseInt(message.substring(0, i));
        order.y = Integer.parseInt(message.substring(i+1, j));
        order.side = Integer.parseInt(message.substring(j+1));
        return order;
    }
}


