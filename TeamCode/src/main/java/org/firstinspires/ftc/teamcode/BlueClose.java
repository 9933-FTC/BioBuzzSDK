package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class BlueClose extends TurtleOpMode {

    int step = 0;

    @Override
    public void init() {

        super.init();
        //drive.seedPose(0,0,0);  <- Null Pointer Exception?
    }

    @Override
    public void loop() {
        super.loop();
        telemetry.addLine("Step: " + step);

        if(step == 0) {

            drive.seedPose(0, 0, 0);
            step = 10;
        } else if (step == 10) {


            drive.driveToPose(12, 12, 90);
            if (drive.isRobotAtTarget()) {
                step = 20;
            }
        } else if (step == 20) {
            drive.driveToPose(24, 24, 60);
            if (drive.isRobotAtTarget()) {
                step = 30;
            }
        }



    }

}
