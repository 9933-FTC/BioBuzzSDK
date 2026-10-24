package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IntakeSubsystem {
    DcMotor Intake;
    CRServo Leftservo;

    CRServo Rightservo;



    public IntakeSubsystem(HardwareMap hardwareMap){
        Intake = hardwareMap.get(DcMotor.class,"Intake");
        Leftservo = hardwareMap.get(CRServo.class,"Leftservo");
        Rightservo = hardwareMap.get(CRServo.class,"Rightservo");


    }

    public void startIntake(){
        Intake.setPower(1);
        Leftservo.setPower(1);
        Rightservo.setPower(1);
    }

    public void stopIntake(){
        Intake.setPower(0);
        Leftservo.setPower(0);
        Rightservo.setPower(0);

    }
}
