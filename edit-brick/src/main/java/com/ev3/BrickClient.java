package com.ev3;

import java.io.IOException;
import java.io.StringReader;
import java.util.Queue;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.websocket.ClientEndpoint;
import javax.websocket.MessageHandler;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

class Order
{
    public int x, y, side, getItem;
}

@ClientEndpoint
public class BrickClient {

    public Boolean directControl = false;
    private static Port colorSensorPort = SensorPort.S2;
    private static EV3ColorSensor colorSensor = new EV3ColorSensor(colorSensorPort);
    private static RegulatedMotor lm = new EV3LargeRegulatedMotor(MotorPort.B);
    private static RegulatedMotor rm = new EV3LargeRegulatedMotor(MotorPort.C);
    private static RegulatedMotor gripper = new EV3MediumRegulatedMotor(MotorPort.A);
    private static EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
    private static EV3IRSensor irSensor = new EV3IRSensor(SensorPort.S3);
    private static SampleProvider rangeSampler = irSensor.getDistanceMode();
    public static int WHITE = 6;
    public static int BLACK = 7;
    public static int RED = 0;

    /*
     * === COLORS ===
     * 6 - WHITE
     * 7 - BLACK/VERY DARK SOMETHING
     * 0 - RED
     */
    private Session session;
    private Queue<String> commandQueue;


    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        Log.info("Connected to brick");
    }
    public void addMessageHandler(MessageHandler.Whole<String> handler) {
        this.session.addMessageHandler(handler);
    }
    public void sendCommand(String command) throws IOException {
        this.session.getBasicRemote().sendText(command);
    }
    public void InformServer(String message, int x, int y, int side) throws IOException {
        CommandWrapper command;
        if(x == 0 && y == 0){
            command = new CommandWrapper(message,"");
        }else{
            command = new CommandWrapper(message, x + ", " + y + ", " + side);
        }
        JsonObject jsonCommand = Json.createObjectBuilder()
                .add("command", command.getCommand())
                .add("data", command.getData()).build();
        sendCommand(jsonCommand.toString());

    }
    public void InformServer(String message) throws IOException {
        InformServer(message, 0, 0, 0);
    }

    @OnMessage
    public void onMessage(String json, Session session) throws IOException {
        this.session = session;
        try {
            final JsonObject jsonCommand = Json.createReader(new StringReader(json)).readObject();
            System.out.println("Parsed: " + jsonCommand.toString());
            final String command = jsonCommand.getString("command");
            if (command.isEmpty())
                Log.info("No command given.");
            else if (command.equals("get")) {
                final String data = jsonCommand.getString("data");
                Order order = ParseCommand(data);
                order.getItem = 1;
                InformServer("start");
                find_path(order.x, order.y, order.side != 0 ? true : false, order.getItem != 0 ? true : false); // go fetch the item
            } else if (command.equals("put")) {
                final String data = jsonCommand.getString("data");
                Order order = ParseCommand(data);
                order.getItem = 0;
                InformServer("start");
                find_path(order.x, order.y, order.side != 0 ? true : false, order.getItem != 0 ? true : false); // go place the item
            } else if (command.equals("terminate")) {
                Log.info("Press escape to exit.");
                if (Button.waitForAnyPress() == Button.ID_ESCAPE) {
                    System.exit(0);
                }
            } else {
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
    public void find_path(int x, int y, boolean go_up, boolean take_from) throws IOException
    {
        go_up = !go_up;
        Log.info("x: " + x);
        Log.info("y: " + y);
        Log.info("down: " + go_up);
        Log.info("take_from: " + take_from);

        if (take_from)
            setupHand();
        else
        {
            grabItem();
            InformServer("PickedUp");
        }


        lm.setSpeed(420);
        rm.setSpeed(420);

        int current_x = 1;
        int current_y = 0;
        int colorID;

        // GO TO OBJECT
        while (true)
        {
            colorID = colorSensor.getColorID(); // do a color reading

            if (colorID == WHITE) // need to find the line again
            {
                find_line_again(BLACK, RED);
            }
            else if (colorID == 5 || colorID == RED) // if we've found an intersection (red color)
            {
                System.out.println("found an intersection!");
                InformServer("location", current_x, current_y, go_up ? 1 : 0); // send data to server
                if (current_x != x) // if we still need to go up
                {
                    System.out.println("goin fwd");
                    current_x++;
                    ForwardIntersection();
                }
                else if(current_x == x && current_y == 0) // if we've found the intersection on which we need to turn
                {
                    if (y < 0)
                    {
                        System.out.println("goin left");
                        current_y--;
                        rotateL();
                    }
                    else
                    {
                        System.out.println("goin right");
                        current_y++;
                        rotateR();
                    }
                }
                else if(current_x == x && current_y == y) // if we're on the final intersection
                {
                    if (!go_up && y < 0 || go_up && y > 0)
                    {
                        System.out.println("final right turn");
                        rotateR();
                    }
                    else
                    {
                        System.out.println("final left turn");
                        rotateL();
                    }

                    // GRAB OR DROP ITEM
                    Sound.beepSequence();
                    if (take_from)
                    {
                        alignToObject();
                        objectGrabbingProcedure();
                        InformServer("PickedUp");
                    }
                    else
                    {
                        goBackwardsShort();
                        dropItem();
                        InformServer("Dropped");
                    }
                    return_to_line(y, go_up);
                    break;
                }
                else // or we're moving horizontally (fwd on the Y axis)
                {
                    System.out.println("goin fwd (sideways)");
                    ForwardIntersection();
                    if (y < 0)
                        current_y--;
                    else
                        current_y++;
                }
            }
            else // base case is, that we're on the line, so go forward - fwd
            {
                System.out.println("fwd");
                lm.backward();
                rm.backward();
            }
        }

        // GO BACK TO START POINT

        current_y = y < 0 ? y + 1 : y - 1;
        current_x = x;
        while (true)
        {
            colorID = colorSensor.getColorID(); // do a color reading

            if (colorID == WHITE) // need to find the line again
            {
                find_line_again(BLACK, RED);
            }
            else if (colorID == 5 || colorID == RED) // if we've found an intersection (red color)
            {

                System.out.println("found an intersection!");
                if (current_y != 0)
                {
                    System.out.println("goin fwd");
                    InformServer("location", current_x, current_y, go_up ? 1 : 0);
                    ForwardIntersection();
                    // update current_x position
                    if (y < 0)
                        current_y++;
                    else
                        current_y--;
                }
                else if(current_y == 0 && current_x == x) // if we've hit the only intersection on which we have to turn
                {
                    InformServer("location", current_x, current_y, go_up ? 1 : 0);
                    if (y < 0)
                        rotateR();
                    else
                        rotateL();
                    current_x--;
                }
                else if(current_x != 0)
                {
                    System.out.println("goin fwd");
                    InformServer("location", current_x, current_y, go_up ? 1 : 0);
                    ForwardIntersection();
                    current_x--;
                }
                else
                {
                    // DROP ITEM AND TURN AROUND
                    Sound.beepSequence();
                    rm.stop(true);
                    lm.stop();
                    rotate180();
                    dropItem();
                    InformServer("end");
                    //ForwardIntersection();
                    break;
                }
            }
            else // base case is, that we're on the line, so go forward - fwd
            {
                System.out.println("fwd");
                lm.backward();
                rm.backward();
            }
        }


    }

    private static void return_to_line(int y, boolean go_up)
    {
        int colorID;

        lm.stop(true);
        rm.stop();

        lm.backward();
        rm.backward();

        while(true)
        {
            colorID = colorSensor.getColorID();

            if (colorID == 0 || colorID == 7)
            {
                if (!go_up && y < 0 || go_up && y > 0)
                {
                    System.out.println("going back on line LEFT");
                    rotateR();
                }
                else
                {
                    System.out.println("going back on line RIGHT");
                    rotateL();
                }
                break;
            }
        }
    }

    private static void find_line_again(int lineColorID, int lineColorID_2)
    {
     // turn left and try to find the line again
        System.out.println("find line left");
        lm.stop(true);
        rm.stop();
        boolean found = false;
        rm.rotate(-100, true);
        lm.rotate(100, true);
        int colorID;

        while (rm.isMoving() && lm.isMoving()) {
            // sample = getSample();
            colorID = colorSensor.getColorID();
            if (colorID == lineColorID || colorID == lineColorID_2) // found it!
            {
                rm.stop(true);
                lm.stop();
                found = true;
                break;
            }
        }

        // if searching towards left didn't succeed, search towards right
        if (!found) {
            System.out.println("find line right");
            lm.rotate(-210, true);
            rm.rotate(210, true);

            while (rm.isMoving() && lm.isMoving()) {
                // sample = getSample();
                colorID = colorSensor.getColorID();
                if (colorID == lineColorID || colorID == lineColorID_2)  // found it!
                {
                    lm.stop(true);
                    rm.stop();
                    found = true;
                    break;
                }
            }
        }
    }

    private static void follow_red_line()
    {
        lm.backward();
        rm.backward();
        int colorID;

        while(true)
        {
            colorID = colorSensor.getColorID();
            if (colorID == WHITE) // if we went off-course
                find_line_again(RED, 255);
            else if(colorID == BLACK) // if we're past the intersection
                break;
        }

    }

    private static void rotateL()
    {
        lm.stop(true);
        rm.stop(true);
        rm.rotate(120, true);
        lm.rotate(-640);
    }

    private static void rotateR()
    {
        lm.stop(true);
        rm.stop(true);
        lm.rotate(120, true);
        rm.rotate(-640);
    }

    private static void rotateR_90()
    {
        lm.stop(true);
        rm.stop();
        lm.rotate(80, true);
        rm.rotate(-660);
    }
    private static void rotateL_90()
    {
        lm.stop(true);
        rm.stop();
        lm.rotate(80, true);
        rm.rotate(-660);
    }


    private static void rotate180()
    {
        lm.stop(true);
        rm.stop();
        while ((!lm.isMoving()) && (!rm.isMoving()))
        {
            // wait for motors
        }
        lm.backward();
        rm.forward();
        boolean found_white = false;
        while (true)
        {
            if (found_white && colorSensor.getColorID() == BLACK)
            {
                    lm.stop(true);
                    rm.stop();
                    rm.rotate(60);
                    break;
            }
            else if(colorSensor.getColorID() == WHITE)
                found_white = true;
        }
    }

    private static void ForwardIntersection()
    {
        lm.stop(true);
        rm.stop();
        lm.rotate(-200, true);
        rm.rotate(-200);
    }

    private static void goBackwardsShort()
    {
        lm.stop(true);
        rm.stop();
        while ((!lm.isMoving()) && (!rm.isMoving()))
        {
            // wait for motors
        }
            
        lm.forward();
        rm.forward();

        boolean found_line = false;
        int colorID;

        while (true)
        {
            colorID = colorSensor.getColorID();
            if (colorID == BLACK || colorID == RED)
                found_line = true;
            else if(colorID == WHITE && found_line)
            {
                lm.stop(true);
                rm.stop();
                break;
            }
        }
    }

    private static void objectGrabbingProcedure()
    {

        lm.stop(true);
        rm.stop();
        lm.setSpeed(100);
        rm.setSpeed(100);

        lm.forward();
        rm.forward();

        int sampleSize = touchSensor.sampleSize();
        float[] sample = new float[sampleSize];
        while (true)
        {
            System.out.println("trying to grab object, fwd");
            touchSensor.fetchSample(sample, 0);
            if(sample[0] == 1)
            {
                lm.stop(true);
                rm.stop();
                grabItem();
                break;
            }
        }

        lm.stop(true);
        rm.stop();
        lm.setSpeed(420);
        rm.setSpeed(420);
    }

    public static void grabItem()
    {
        gripper.rotate(180);
        Delay.msDelay(1000);
        gripper.forward();
        Delay.msDelay(1000);
    }

    private static void dropItem() {
        Log.info("Dropping..:");
        gripper.backward();
        Delay.msDelay(1000);
        gripper.rotate(-180);
        Log.info("Item dropped.");
    }

    private static void setupHand()
    {
        gripper.rotate(-720,true);
        gripper.backward();
        while (true) {
            int sampleSize = touchSensor.sampleSize();

            float[] sample = new float[sampleSize];
            touchSensor.fetchSample(sample, 0);
            if(sample[0]==1){
                gripper.stop(true);
                gripper.rotate(-180);
                gripper.forward();
                Delay.msDelay(800);
                gripper.stop();
                break;
            }
        }
    }

    private static void alignToObject()
    {
        lm.stop(true);
        rm.stop();

        rm.rotate(-100, true);
        lm.rotate(100);

        lm.setSpeed(50);
        rm.setSpeed(50);


        boolean found = false;
        boolean isFurther = false;
        float[] sample = new float[rangeSampler.sampleSize()];
        irSensor.getDistanceMode().fetchSample(sample,0);

        System.out.println("TS: " + sample[0] + "cm");

        if(!Float.isFinite(sample[0]))
            sample[0]=255;

        float lastRange=sample[0];

        if(!found)
        {

            System.out.println("looking left");
            lm.stop(true);
            rm.stop();
            lm.rotate(-420, true);
            rm.rotate(420, true);

            while (true)
            {
                // sample = getSample();
                irSensor.getDistanceMode().fetchSample(sample,0);


                if(!Float.isFinite(sample[0]))
                    sample[0]=255;

                System.out.println("prev: " + lastRange + " cm");
                System.out.println("TS: " + sample[0] + " cm");


                if (sample[0] > lastRange && isFurther)
                {
                    lm.stop(true);
                    rm.stop();
                    lm.rotate(50);
                    rm.rotate(-50);
                    found = true;
                    break;
                }
                else if (sample[0] > lastRange)
                {
                    isFurther = true;
                    lastRange = sample[0];
                }
                else if (sample[0] < lastRange)
                {
                    isFurther = false;
                    lastRange = sample[0];
                }
                else
                    lastRange = sample[0];
            }
        }
        lm.stop(true);
        rm.stop();
    }
}
