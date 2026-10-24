package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ShooterSubsystem {

    DcMotor Shooter;


    public ShooterSubsystem(HardwareMap hardwareMap){
        Shooter = hardwareMap.get(DcMotor.class,"Shooter");


    }

    public void startShooter(){
        Shooter.setPower(1);

    }

    public void stopShooter(){
        Shooter.setPower(0);

    }


}

