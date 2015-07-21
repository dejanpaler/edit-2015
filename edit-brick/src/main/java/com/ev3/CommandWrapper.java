package com.ev3;

public class CommandWrapper {

    private String command;

    private String data;

    public CommandWrapper(String c, String d) {
        this.command = c;
        this.data = d;
    }

    public String getCommand() {
        return command;
    }

    public String getData() {
        return data;
    }
}
