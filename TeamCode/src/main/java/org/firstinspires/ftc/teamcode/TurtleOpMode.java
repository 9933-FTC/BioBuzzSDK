package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.subsystems.DeliverySubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;


public class TurtleOpMode extends OpMode {

    DriveSubsystem drive;
    IntakeSubsystem intake;
    DeliverySubsystem delivery;


    @Override
    public void init() {
        drive = new DriveSubsystem(telemetry, hardwareMap);
        intake = new IntakeSubsystem(hardwareMap);
        delivery = new DeliverySubsystem(hardwareMap);
    }

    @Override
    public void loop() {
        drive.populateTelemetry();
    }

}
