package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class DriveSubsystem {
    Telemetry telemetry;
    DcMotorEx leftFront, rightFront, leftRear, rightRear;
    IMU gyro ;
    boolean isRobotAtTarget = false;
    double autoMoveKP = 0.1,    autoTurnKP = 0.1;
    double autoMoveTolerance = 1, autoTurnTolerance = 1;

    /**
     * Assumes Drive Motors are configured as leftFront, rightFront, leftRear, and rightRear
     * </p>
     * Assumes Pinpoint is configured as pinpoint
     */
    public DriveSubsystem(Telemetry telemetry, HardwareMap hardwareMap) {
        this.telemetry = telemetry;

        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightRear.setDirection(DcMotorSimple.Direction.FORWARD);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);

        gyro = hardwareMap.get(IMU.class, "imu");
        // This needs to be changed to match the orientation on your robot
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

        RevHubOrientationOnRobot orientationOnRobot = new
                RevHubOrientationOnRobot(logoDirection, usbDirection);
        gyro.initialize(new IMU.Parameters(orientationOnRobot));
    }

    public void populateTelemetry() {
        telemetry.addData("Forward Pose", leftFront.getCurrentPosition());
        telemetry.addData("Right Pose", rightRear.getCurrentPosition());
        telemetry.addData("Current Angle", gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
    }

    public void zeroGyro() {
        gyro.resetYaw();
    }

    public void autoMove(double distanceForward, double distanceRight, double endAngle) {
        //Translational Math
        //Convert inches to ticks
        distanceForward *= 1; //TODO: Determine Ticks Per Inch
        distanceRight *= 1; //TODO: Determine Ticks Per Inch

        //Calculate Error
        double forwardError = distanceForward - leftFront.getCurrentPosition();
        double rightError = distanceRight - rightRear.getCurrentPosition();

        //Calculate Power
        double forwardPower = forwardError * autoMoveKP;
        double rightPower = rightError * autoMoveKP;



        //Rotational Math
        //Calculate Error
        double targetAngle = endAngle, currentAngle = gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        if (Math.abs(targetAngle - currentAngle) > 180) {
            double delta = (180 - Math.abs(targetAngle));

            if (currentAngle >= 0) {
                targetAngle = 180 + delta;
            } else {
                targetAngle = -180 - delta;
            }
        }
        double turnError = targetAngle - currentAngle;

        //Calculate Power
        double turnPower = turnError * autoTurnKP;



        //Figure out completion status
        double totalDistanceError = Math.sqrt(Math.pow(forwardError, 2) + Math.pow(rightError, 2));
        isRobotAtTarget = totalDistanceError < autoMoveTolerance && turnError < autoTurnTolerance;

        //Apply Correct Power
        if (isRobotAtTarget) {
            fieldCentricDrive(0, 0, 0);
        } else {
            fieldCentricDrive(forwardPower, rightPower, turnPower);
        }
    }

    public boolean isRobotAtTarget() {
        return isRobotAtTarget;
    }

     //This routine drives the robot field relative
    public void fieldCentricDrive(double forward, double right, double rotate) {
        // First, convert direction being asked to drive to polar coordinates
        double theta = Math.atan2(forward, right);
        double r = Math.hypot(right, forward);

        // Second, rotate angle by the angle the robot is pointing
        theta = AngleUnit.normalizeRadians(theta -
                gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        // Third, convert back to cartesian
        double newForward = r * Math.sin(theta);
        double newRight = r * Math.cos(theta);

        // Finally, call the drive method with robot relative forward and right amounts
        robotCentricDrive(newForward, newRight, rotate);
    }

    public void robotCentricDrive(double forward, double right, double rotate) {
        // This calculates the power needed for each wheel based on the amount of forward,
        // strafe right, and rotate
        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backRightPower = forward + right - rotate;
        double backLeftPower = forward - right + rotate;

        double maxPower = 1.0;
        double maxSpeed = 1.0;  // make this slower for outreaches

        // This is needed to make sure we don't pass > 1.0 to any wheel
        // It allows us to keep all of the motors in proportion to what they should
        // be and not get clipped
        maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));

        // We multiply by maxSpeed so that it can be set lower for outreaches
        // When a young child is driving the robot, we may not want to allow full
        // speed.
        leftFront.setPower(maxSpeed * (frontLeftPower / maxPower));
        rightFront.setPower(maxSpeed * (frontRightPower / maxPower));
        leftRear.setPower(maxSpeed * (backLeftPower / maxPower));
        rightRear.setPower(maxSpeed * (backRightPower / maxPower));
    }
}
