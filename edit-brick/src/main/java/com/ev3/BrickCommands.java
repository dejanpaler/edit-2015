package com.ev3;

import io.undertow.server.session.Session;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;

import lejos.hardware.Sound;
import lejos.hardware.Button;
import lejos.hardware.motor.*;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;

class Order
{
    public int x, y, side, getItem;
}

public class BrickCommands extends AbstractReceiveListener {

    private WebSocketChannel channel;
    public Boolean directControl = false;
    private static Port colorSensorPort = SensorPort.S2;
    private static EV3ColorSensor colorSensor = new EV3ColorSensor(colorSensorPort);
    private static RegulatedMotor lm = new EV3LargeRegulatedMotor(MotorPort.B);
    private static RegulatedMotor rm = new EV3LargeRegulatedMotor(MotorPort.C);
    private static RegulatedMotor gripper = new EV3MediumRegulatedMotor(MotorPort.A);
    private static EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);

    @Override
    protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {

        String json = message.getData();
        Log.info("Trying to parse JSON:" + json);

        try {
            final JsonObject jsonCommand = Json.createReader(new StringReader(json)).readObject();
            System.out.println("Parsed: " + jsonCommand.toString());
            final String command = jsonCommand.getString("command");
            //respond to client
            if (command.isEmpty())
                Log.info("No command given.");
            else if (command.equals("get")) {
                final String data = jsonCommand.getString("data");
                Order order = ParseCommand(data);
                find_path(order.x, order.y, order.side != 0 ? true : false, order.getItem != 0 ? true : false); // go fetch/place the item
            } else if (command.equals("terminate")) {
                Log.info("Press escape to exit.");
                if (Button.waitForAnyPress() == Button.ID_ESCAPE) {
                    System.exit(0);
                }
            } else if (command.equals("control")) {
                directControl = true;
            } else if(directControl){
                switch (command){
                case "grab":
                    PickupItem();
                    break;

                case "drop":
                    dropItem();
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
            } else {
                Log.info("Command " + command + " doesn't exist.");
            }
        } catch (JsonException je) {
            System.out.print("Couldn't parse JSON.");
            System.out.print(je.getMessage());
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
    
    public static void find_path(int x, int y, boolean go_left, boolean take_from) 
    {
        
        lm.setSpeed(420);
        rm.setSpeed(420);

        int current_x = 0;
        int current_y = 1;
        int colorID;

        int WHITE = 6;
        int BLACK = 7;
        int RED = 0;
        
        /*
         * === COLORS ===
         * 6 - WHITE
         * 7 - BLACK/VERY DARK SOMETHING
         * 0 - RED
         */
        
        // GO TO OBJECT
        while (true) 
        {
            colorID = colorSensor.getColorID(); // do a color reading

            if (colorID == WHITE) // need to find the line again
            {
                find_line_again();
            }
            else if (colorID == 5 || colorID == RED) // if we've found an intersection (red color)    
            {
                System.out.println("found an intersection!");
                if (current_y != y) // if we still need to go up
                {
                    System.out.println("goin fwd");
                    current_y++;
                    ForwardIntersection();
                }
                else if(current_y == y && current_x == 0) // if we've found the intersection on which we need to turn
                {
                    if (x < 0)
                    {
                        System.out.println("goin left");
                        current_x--;
                        rotateL();
                    }
                    else
                    {
                        System.out.println("goin right");
                        current_x++;
                        rotateR();
                    }
                }
                else if(current_y == y && current_x == x) // if we're on the final intersection
                {
                    if (go_left)
                    {
                        System.out.println("final left turn");
                        rotateR();
                    }
                    else
                    {
                        System.out.println("final right turn");
                        rotateL();
                    }
                    Sound.beepSequence();
                    objectGrabbingProcedure();
                    rotateBackOnLine(!go_left);
                    break;
                }
                else // or we're moving horizontally (fwd on the X axis)
                {
                    System.out.println("goin fwd (sideways)");
                    ForwardIntersection();
                    if (x < 0)
                        current_x--;
                    else
                        current_x++;
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
        
        current_x = x < 0 ? x + 1 : x - 1;
        current_y = y;
        while (true)
        {
            colorID = colorSensor.getColorID(); // do a color reading

            if (colorID == WHITE) // need to find the line again
            {
                find_line_again();
            }
            else if (colorID == 5 || colorID == RED) // if we've found an intersection (red color)    
            {
                
                System.out.println("found an intersection!");
                if (current_x != 0)
                {
                    System.out.println("goin fwd");
                    ForwardIntersection();
                    // update current_x position
                    if (x < 0)
                        current_x++;
                    else
                        current_x--;
                }
                else if(current_x == 0 && current_y == y) // if we've hit the only intersection on which we have to turn
                {
                    if (x < 0)
                        rotateR();
                    else
                        rotateL();
                    current_y--;
                }
                else if(current_y != 0)
                {
                    System.out.println("goin fwd");
                    ForwardIntersection();
                    current_y--;
                }
                else
                {
                    // DROP ITEM AND TURN AROUND
                    Sound.beepSequence();
                    rm.stop(true);
                    lm.stop();
                    dropItem();
                    rotate180();
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

    private static void find_line_again()
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
            if (colorID == 7) // found it!
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
            lm.rotate(-200, true);
            rm.rotate(200, true);

            while (rm.isMoving() && lm.isMoving()) {
                // sample = getSample();
                colorID = colorSensor.getColorID();
                if (colorID == 7)  // found it!
                {
                    lm.stop(true);
                    rm.stop();
                    found = true;
                    break;
                }
            }
        }
    }
    
    private static void rotateL() 
    {
        lm.stop(true);
        rm.stop(true);
        rm.rotate(120, true);
        lm.rotate(-620);
    }

    private static void rotateR() 
    {
        lm.stop(true);
        rm.stop(true);
        lm.rotate(120, true);
        rm.rotate(-620);
    }

    private static void rotate180()
    {
        lm.rotate(840, true);
        rm.rotate(840);
    }
    
    private static void ForwardIntersection() 
    {
        lm.stop(true);
        rm.stop();
        lm.rotate(-200, true);
        rm.rotate(-200);
    }

    private static void objectGrabbingProcedure()
    {
        lm.stop(true);
        rm.stop();
        lm.setSpeed(100);
        rm.setSpeed(100);
        
        lm.forward();
        rm.forward();
        
        while (true) 
        {
            int sampleSize = touchSensor.sampleSize();
            float[] sample = new float[sampleSize];
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
    
    
    private void PickupItem() 
    {
        Log.info("Picking up item");
        //move forward

        gripper.rotate(-180);
        Delay.msDelay(1000);
        gripper.rotate(180);
        Delay.msDelay(1000);
        gripper.forward();
        Delay.msDelay(1000);

        //move backward
        Button.LEDPattern(2);
        Log.info("Item picked up");
    }

    private static void dropItem() {
        Log.info("Dropping..:");
        gripper.backward();
        Delay.msDelay(1000);
        gripper.rotate(-180);
        Log.info("Item dropped.");
    }

    private static void rotateBackOnLine(boolean turn_left)
    {
        if(turn_left)
            lm.rotate(-840); //rotiraj 90° levo
        else
            rm.rotate(-840); //rotiraj 90° Desno 
    }

    private void Turn(String direction){
        if(direction.equals("right")){
            //turn right
        }
        else if (direction.equals("left")){
            //turn left
        }
        else {
            //turn around
        }

    }
    private static void ForwardTest(){
        System.out.print("Press Back, when you want to stop!");
        Motor.B.forward();
        Motor.C.forward();
        while(true)
        {
            if (Button.waitForAnyPress() == Button.ID_ESCAPE) {
                Motor.B.stop();
                Motor.C.stop();
                break;
            }
        }
    }
    private static void Align(){
        Motor.A.backward();
        Delay.msDelay(1000);
        Motor.A.stop();
    }
    public static void Forward(int i){
        Motor.B.setSpeed(i);// 2 RPM 720
        Motor.C.setSpeed(i);
        Motor.B.forward();
        Motor.C.forward();
    }
    public static void Backward(int i){
        Motor.B.setSpeed(i);// 2 RPM 720
        Motor.C.setSpeed(i);
        Motor.B.backward();
        Motor.C.backward();
    }
    public static void Stop(){
        Motor.B.stop();
        Motor.C.stop();
    }
}
