package com.ev3.brick;

import java.io.IOException;
import java.io.StringReader;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.ev3.item.Items;

@Singleton
@ServerEndpoint("/brick")
public class BrickConnection {

    private String currentId;
    @Inject
    AngularConnection AC;
    @Inject
    Items items;
    private Session session;

    public void setId(String id) {
        currentId = id;
    }

    /**
     * @OnOpen allows us to intercept the creation of a new session. The session
     *         class allows us to send data to the user. In the method onOpen,
     *         we'll let the user know that the handshake was successful.
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println(session.getId() + " has opened a brick connection");
        try {
            session.getBasicRemote().sendText("Connection Established");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendCommand(String command) throws IOException {
        try {
            session.getBasicRemote().sendText(command);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * When a user sends a message to the server, this method will intercept the
     * message and allow us to react to it. For now the message is read as a
     * String.
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message from " + session.getId() + ": " + message);
        try {
            final JsonObject jsonCommand = Json.createReader(new StringReader(message)).readObject();
            if (jsonCommand.getString("command").equals("end")) {
                AC.sendCommand(jsonCommand.toString());
                try {
                    items.RemoveItem(currentId);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                AC.sendCommand(jsonCommand.toString());
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    /**
     * The user closes the connection.
     *
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session) {
        System.out.println("Session " + session.getId() + " has ended");
    }

}
