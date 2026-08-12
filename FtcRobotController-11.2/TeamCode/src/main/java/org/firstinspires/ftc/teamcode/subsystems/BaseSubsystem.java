package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class BaseSubsystem {



    DcMotorEx baseMotor;



    //class method
    public BaseSubsystem(HardwareMap hardwareMap){
        baseMotor = hardwareMap.get(DcMotorEx.class, "intake");
    }
    public void setMotorPower(double setSpeed) {
        baseMotor.setPower(setSpeed);
    }
}
