package com.ev3.brick;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.ev3.brick.device.BrickClientEndpoint;

@Singleton
@ServerEndpoint("/commands")
public class BrickCommandsEndpoint {

    private Session webSession;

    private final Set<Session> webSessions = Collections
            .synchronizedSet(new HashSet<>());

    @Inject
    BrickClientEndpoint brickEndpoint;

    @PostConstruct
    public void addBrickMessageHandler() {
        // brickEndpoint.addMessageHandler(new BrickMessages(this));
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
    public void execute(String json) {

        JsonArray jsonValues = Json.createReader(new StringReader(json))
                .readArray();

        List<JsonObject> jsonObjects = jsonValues.getValuesAs(JsonObject.class);
        jsonObjects.stream().forEach(
                jsonObject -> {
                    jsonObject.getString("id");

                    CommandWrapper command = new CommandWrapper(
                            "MoveToLocation", "X,Y");

                    JsonObject jsonCommand = Json.createObjectBuilder()
                            .add("command", command.getCommand())
                            .add("data", command.getData()).build();

                    try {
                        brickEndpoint.sendCommand(jsonCommand.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void sendMessage(String message) {
        webSessions.stream().forEach(session -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
