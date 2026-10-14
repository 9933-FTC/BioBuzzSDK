package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.ArmSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElbowSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShoulderSubsystem;


public class TurtleOpMode extends OpMode {

    DriveSubsystem drive;
    ElbowSubsystem elbow;
    ArmSubsystem arm;
    ShoulderSubsystem shoulder;

    @Override
    public void init() {
        drive = new DriveSubsystem(telemetry, hardwareMap);
        elbow = new ElbowSubsystem(hardwareMap);
        arm = new ArmSubsystem(hardwareMap);
        shoulder = new ShoulderSubsystem(hardwareMap);
    }

    @Override
    public void loop() {
        drive.populateTelemetry();
    }

}
