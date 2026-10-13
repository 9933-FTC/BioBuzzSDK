package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class BlueClose extends TurtleOpMode {

    int step = 0;

    @Override
    public void init() {

        super.init();
    }

    @Override
    public void loop() {
        super.loop();
        telemetry.addLine("Step: " + step);

        if (step == 0) {
            drive.zeroGyro();
            step = 10;
        } else if (step == 10) {

            drive.autoMove(24, 48, 90);

            if (drive.isRobotAtTarget()) {
                step = 20;
            }
        }



    }

}


