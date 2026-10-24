package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DeliverySubsystem {
    CRServo mailmanServo;
    public DeliverySubsystem (HardwareMap hardwareMap) {
        mailmanServo = hardwareMap.get(CRServo.class, "randomValue");
    }

    public void startDelivery (){
        mailmanServo.setPower(1);
    }
    public void stopDelivery (){
        mailmanServo.setPower(0);
    }
}
