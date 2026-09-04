package org.firstinspires.ftc.teamcode.turtleUtils;

public class ProfiledPIDController extends PIDController{

    double maxVelo = 0;
    double maxAcc = 0;
    double prevPos = 0;


    public ProfiledPIDController (double p, double i, double d) {
        super(p, i, d);
    }

    public double calculate (double setpoint, double measurement, double velocity) {


        return super.calculate(setpoint, measurement);
    }
}
