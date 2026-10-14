 package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

    public class ShoulderSubsystem {


        DcMotorEx Shouldermotor;

        public ShoulderSubsystem(HardwareMap hardwareMap){
            Shouldermotor = hardwareMap.get(DcMotorEx.class, "shoulder");
        }
        public void setMotorPower(double setSpeed) {
            Shouldermotor.setPower(setSpeed);
        }


        public void setPosition(int setPose) {
            Shouldermotor.setTargetPosition(setPose);
        }

    }
















