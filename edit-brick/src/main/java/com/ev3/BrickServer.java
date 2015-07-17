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
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

public class BrickServer {

    private static Port colorSensorPort = SensorPort.S2;
    private static EV3ColorSensor colorSensor;
    private static SampleProvider sampleProvider;
    private static int sampleSize;
    private static RegulatedMotor lm = new EV3LargeRegulatedMotor(MotorPort.B);
    private static RegulatedMotor rm = new EV3LargeRegulatedMotor(MotorPort.C);

    public static void main(String[] args) {

        // wait for exit
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.print("Thread started");
                if (Button.waitForAnyPress() == Button.ID_ESCAPE) {
                    System.exit(0);
                }
            }
        };
        thread.start();

        HelloWorld();
        // startWebSocketServer();
        find_path(-1, 1, true, true);
        System.exit(0);
        // MotorForward();
    }

    private static float[] getSample() {
        // Initializes the array for holding samples
        float[] sample = new float[sampleSize];

        // Gets the sample an returns it
        sampleProvider.fetchSample(sample, 0);
        return sample;
    }

    private static void find_path(int x, int y, boolean go_left, boolean take_from) {
        colorSensor = new EV3ColorSensor(colorSensorPort);
        sampleProvider = colorSensor.getRedMode();
        sampleSize = sampleProvider.sampleSize();

        lm.setSpeed(420);
        rm.setSpeed(420);

        int current_x = 0;
        int current_y = 0;

        int i = 0;
        int colorID = 7;
        float threshold = (float) 0.25;
        while (true) {
            // float[] sample = getSample();
            colorID = colorSensor.getColorID();

            // System.out.println("N=" + i + " Sample=" +
            // Arrays.toString(sample));
            if (colorID == 6) // need to find the line again
            {
                // turn left and try to find the line again
                System.out.println("find line left");
                lm.stop();
                rm.stop();
                boolean found = false;
                rm.rotate(-100, true);
                lm.rotate(100, true);

                while (rm.isMoving() && lm.isMoving()) {
                    // sample = getSample();
                    colorID = colorSensor.getColorID();
                    if (colorID == 7) {
                        rm.stop();
                        lm.stop();
                        found = true;
                        break;
                    }
                }

                // if searching towards left didn't succeed, search towards
                // right
                if (!found) {
                    System.out.println("find line right");
                    lm.rotate(-200, true);
                    rm.rotate(200, true);

                    while (rm.isMoving() && lm.isMoving()) {
                        // sample = getSample();
                        colorID = colorSensor.getColorID();
                        if (colorID == 7) {
                            lm.stop();
                            rm.stop();
                            found = true;
                            break;
                        }
                    }
                }
            } else if (colorID == 5 || colorID == 0) // if we've found an intersection
                   // (red color)
            {
                System.out.println("found an intersection!");
                if ((current_y - Math.abs(current_x) < y - 1) && current_x != x)
                // then march on!
                {
                    System.out.println("goin fwd");
                    current_y++;
                    ForwardIntersection();
                } else if (current_x == x && go_left) // then make the final
                // turn
                {
                    System.out.println("final left turn");
                    rotateL();
                } else if (current_x == x && !go_left) // then make the final
                // turn
                {
                    System.out.println("final right turn");
                    rotateR();
                } else if (x < 0) // then turn left
                {
                    System.out.println("goin left");
                    current_x--;
                    rotateL();
                } else if (x > 0) // then turn right
                {
                    System.out.println("goin right");
                    current_x++;
                    rotateR();
                }

            }
            else // if we're on the line
            {
                System.out.println("fwd");
                lm.backward();
                rm.backward();
            }
            if (i == 100000)
                System.exit(0);
            i++;
        }

        // sample spam and motor work
        /*
         * int i = 0; float threshold = (float)0.25; while(true) { float[]
         * sample = getSample(); System.out.println("N=" + i + " Sample=" +
         * Arrays.toString(sample)); if (sample[0] < threshold) {
         * System.out.println("grem levo"); lm.setSpeed(540); lm.backward();
         * rm.setSpeed(0); rm.stop(); } else { System.out.println("grem desno");
         * lm.setSpeed(0); lm.stop(); rm.setSpeed(540); rm.backward(); } if (i
         * == 100000) System.exit(0); i++; }
         */
    }

    private static void rotateL() {
        lm.stop(true);
        rm.stop(true);
        rm.rotate(120, true);
        lm.rotate(-620);// rotiraj 90° levo
    }

    private static void rotateR() {
        lm.stop(true);
        rm.stop(true);
        lm.rotate(120, true);
        rm.rotate(-620);// rotiraj 90° Desno
    }

    private static void ForwardIntersection() {
        lm.stop();
        rm.stop();
        // Motor.B.rotateTo(-840);
        // Motor.C.rotateTo(-840);
        lm.rotate(-800);
        rm.rotate(-800);
        // Delay.msDelay(800);
        lm.stop();
        rm.stop();

    }

    private static void startWebSocketServer() {
        WebSocketCallback callback = new WebSocketCallback();

        String ip = "0.0.0.0";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
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
                .setHandler(path().addPrefixPath("/ev3", websocket(callback))).build();
        server.start();
        Log.info("Server started.");
        Log.info("Listening on " + ip + ":8081/ev3");

        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.print("Thread started");
                if (Button.waitForAnyPress() == Button.ID_ESCAPE) {
                    System.exit(0);
                }
            }
        };
        thread.start();
    }

    private static void HelloWorld() {
        System.out.print("Hello World!");
    }

    private static void MotorForward() {
        Motor.A.forward();
    }
}
