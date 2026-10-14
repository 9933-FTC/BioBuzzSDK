package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ElbowSubsystem {


DcMotorEx Elbowmotor;



//class method
public ElbowSubsystem(HardwareMap hardwareMap){
    Elbowmotor = hardwareMap.get(DcMotorEx.class, "elbow");
}
public void setMotorPower(double setSpeed) {
    Elbowmotor.setPower(setSpeed);
}
public void setPosition(int setPose) {
    Elbowmotor.setTargetPosition(setPose);
}

}