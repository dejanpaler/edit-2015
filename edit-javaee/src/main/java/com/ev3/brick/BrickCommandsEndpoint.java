package com.ev3.brick;

import com.ev3.brick.device.BrickClientEndpoint;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Singleton
@ServerEndpoint("/commands")
public class BrickCommandsEndpoint {

    private Session webSession;

    private final Set<Session> webSessions = Collections.synchronizedSet(new HashSet<>());

    @Inject
    BrickClientEndpoint brickEndpoint;

    @PostConstruct
    public void addBrickMessageHandler(){
        brickEndpoint.addMessageHandler(new BrickMessages(this));
    }

    @OnOpen
    public void init(Session session) {
        this.webSession = session;
        webSessions.add(session);
    }

    @OnClose
    public void close(Session session) {
        webSessions.remove(session);
    }

    @OnMessage
    public void execute(String command) throws IOException {
        final JsonObject jsonCommand = Json.createReader(new StringReader(command))
                                          .readObject();
        final String action = jsonCommand.getString("action");

        // Send command to the device
        brickEndpoint.sendCommand(jsonCommand.toString());

        // Send response to the web client
        this.webSession.getBasicRemote()
                    .sendText("[ev3.javaee] Message received with action " + action + ".");
    }

    public void sendMessage(String message){
        webSessions.stream().forEach(session -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Schedule(second = "*/15", minute = "*", hour = "*")
    public void sendAlive() throws IOException {
        sendMessage("[ev3.javaee] Server is alive");
    }

}
