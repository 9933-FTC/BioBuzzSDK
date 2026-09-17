package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

    public class ArmSubsystem {


        DcMotorEx Armmotor;

        public ArmSubsystem(HardwareMap hardwareMap){
            Armmotor = hardwareMap.get(DcMotorEx.class, "arm");

            Armmotor.setDirection(DcMotorSimple.Direction.FORWARD);


        }
        public void setMotorPower(double setSpeed) {
            Armmotor.setPower(setSpeed);
        }
        public void setPosition(int setPose) {
            Armmotor.setTargetPosition(setPose);
        }
    }