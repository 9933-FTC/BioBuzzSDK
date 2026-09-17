package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp
public class BasicTeleOp extends TurtleOpMode {

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loop() {
        super.loop();
        //drive.fieldCentricDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        elbow.setMotorPower(gamepad2.right_stick_y);
        shoulder.setMotorPower(gamepad2.left_stick_y);
        arm.setMotorPower(gamepad2.right_stick_x);

        if(gamepad1.start){
            //drive.seedPose(0,0,0);
        }

//        if(gamepad1.a){
//            elbow.setMotorPower(0.5);
//
//        }
//
//        if(gamepad1.b){
//            shoulder.setMotorPower(0.5);
//
//        }
//
//        if(gamepad1.y){
//            arm.setMotorPower(0.5);
//
//        }
    }
}
