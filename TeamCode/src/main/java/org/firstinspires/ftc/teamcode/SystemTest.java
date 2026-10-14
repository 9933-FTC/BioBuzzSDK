package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Utility;

@Utility
public class SystemTest extends TurtleOpMode {

    @Override
    public void init() {

        super.init();
    }

    @Override
    public void loop() {
        super.loop();

        telemetry.addLine("X = Front Left");
        if (gamepad1.x) {
            drive.setFrontLeftSpeed(0.2);
        } else {
            drive.setFrontLeftSpeed(0);
        }

        telemetry.addLine("Y = Front Right");
        if (gamepad1.y) {
            drive.setFrontRightSpeed(0.2);
        } else {
            drive.setFrontRightSpeed(0);
        }

        telemetry.addLine("A = Rear Left");
        if (gamepad1.a) {
            drive.setRearLeftSpeed(0.2);
        } else {
            drive.setRearLeftSpeed(0);
        }

        telemetry.addLine("B = Front Left");
        if (gamepad1.x) {
            drive.setRearRightSpeed(0.2);
        } else {
            drive.setRearRightSpeed(0);
        }



    }

}
