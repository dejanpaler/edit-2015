package com.ev3;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.websocket;
import io.undertow.Undertow;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;

import lejos.hardware.Button;
import lejos.hardware.motor.*;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;

public class BrickServer {

	 private static Port colorSensorPort = SensorPort.S2;
	    private static EV3ColorSensor colorSensor;
	    private static SampleProvider sampleProvider;
	    private static int sampleSize;
	
    public static void main(String[] args) {
        HelloWorld();
        //startWebSocketServer();
        find_path(1,1);
        //MotorForward();
    }

    private static float[] getSample() {
        // Initializes the array for holding samples
        float[] sample = new float[sampleSize];

        // Gets the sample an returns it
        sampleProvider.fetchSample(sample, 0);
        return sample;
    }
    
    private static void find_path(int x, int y)
    {
    	colorSensor = new EV3ColorSensor(colorSensorPort);
        sampleProvider = colorSensor.getRedMode();
        sampleSize = sampleProvider.sampleSize();

        // Takes some samples and prints them
        for (int i = 0; i < 4; i++) {
            float[] sample = getSample();
            System.out.println("N=" + i + " Sample=" + Arrays.toString(sample));
        }
    }
    
    
    private static void startWebSocketServer() {
        WebSocketCallback callback = new WebSocketCallback();

        String ip = "0.0.0.0";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    ip = addr.getHostAddress();
                    Log.info(iface.getDisplayName() + " " + ip);
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        Log.info("Starting server...");
        final String host = "0.0.0.0";
        Undertow server = Undertow.builder().addHttpListener(8081, host)
                .setHandler(path().addPrefixPath("/ev3", websocket(callback)))
                .build();
        server.start();
        Log.info("Server started.");
        Log.info("Listening on " + ip + ":8081/ev3");
        
        Thread thread = new Thread(){
            public void run(){
            	System.out.print("Thread started");
            	if (Button.waitForAnyPress() == Button.ID_ESCAPE) {
            		System.exit(0);
            	}
            }
        };
        thread.start();    
    }

    private static void HelloWorld(){
    	System.out.print("Hello World!");
    }
    private static void MotorForward(){
    	Motor.A.forward();
    }
}
