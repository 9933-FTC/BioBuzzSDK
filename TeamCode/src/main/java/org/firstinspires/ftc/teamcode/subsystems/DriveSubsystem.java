package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class DriveSubsystem {
    Telemetry telemetry;
    DcMotorEx frontLeft, frontRight, rearLeft, rearRight;
    IMU gyro ;
    boolean isRobotAtTarget = true;
    double autoMoveKP = 0.1,    autoTurnKP = 0.1;
    double autoMoveTolerance = 1, autoTurnTolerance = 1;
    double forwardOffset = 0, rightOffset = 0, turnOffset = 0;
    double forwardPodOffset = 5, rightPodOffset = 2;

    /**
     * Assumes Drive Motors are configured as leftFront, rightFront, leftRear, and rightRear
     * </p>
     * Assumes Pinpoint is configured as pinpoint
     */
    public DriveSubsystem(Telemetry telemetry, HardwareMap hardwareMap) {
        this.telemetry = telemetry;

        frontLeft = hardwareMap.get(DcMotorEx.class, "leftFront");
        frontRight = hardwareMap.get(DcMotorEx.class, "rightFront");
        rearLeft = hardwareMap.get(DcMotorEx.class, "leftRear");
        rearRight = hardwareMap.get(DcMotorEx.class, "rightRear");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        rearRight.setDirection(DcMotorSimple.Direction.FORWARD);
        rearLeft.setDirection(DcMotorSimple.Direction.REVERSE);

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
        telemetry.addData("Forward Pose", frontLeft.getCurrentPosition());
        telemetry.addData("Right Pose", rearRight.getCurrentPosition());
        telemetry.addData("Current Angle", gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
    }

    public void zeroGyro() {
        gyro.resetYaw();
    }

    public double getForwardPose() {
        double returnDistance = frontLeft.getCurrentPosition();
        returnDistance /= 1; //TODO: Determine Ticks Per Inch
        returnDistance -= gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) * forwardPodOffset;
        returnDistance -= forwardOffset;

        return returnDistance;
    }

    public double getRightPose() {
        double returnDistance = rearRight.getCurrentPosition();
        returnDistance /= 1; //TODO: Determine Ticks Per Inch
        returnDistance -= gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) * rightPodOffset;
        returnDistance -= rightOffset;

        return returnDistance;
    }

    public void autoMove(double distanceForward, double distanceRight, double endAngle) {
        //Translational Math
        //Calculate Error
        double forwardError = distanceForward - getForwardPose();
        double rightError = distanceRight - getRightPose();

        //Calculate Power
        double forwardPower = forwardError * autoMoveKP;
        double rightPower = rightError * autoMoveKP;



        //Rotational Math
        //Calculate Error

        double turnError, currentAngle;

        //TODO Training Wheels Protocol (Spider-Man Reference!!!)
        if (true) {
            currentAngle = AngleUnit.normalizeDegrees(gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) + turnOffset);
        } else {
            currentAngle = gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        }

        turnError = AngleUnit.normalizeDegrees(endAngle - currentAngle);

        //Calculate Power
        double turnPower = turnError * autoTurnKP;



        //Figure out completion status
        double totalDistanceError = Math.sqrt(Math.pow(forwardError, 2) + Math.pow(rightError, 2));
        if (totalDistanceError < autoMoveTolerance && turnError < autoTurnTolerance){
            isRobotAtTarget = true;
        } else if (isRobotAtTarget) {
            //TODO Training Wheels Protocol (Spider-Man Reference!!!)
            forwardOffset = getForwardPose() + forwardOffset;
            rightOffset = getRightPose() + rightOffset;
            turnOffset = -gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            isRobotAtTarget = false;
            return;
        }

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
        frontLeft.setPower(maxSpeed * (frontLeftPower / maxPower));
        frontRight.setPower(maxSpeed * (frontRightPower / maxPower));
        rearLeft.setPower(maxSpeed * (backLeftPower / maxPower));
        rearRight.setPower(maxSpeed * (backRightPower / maxPower));
    }

    ///////////////////////// System Test Utils ////////////////////////////////
    public void setFrontLeftSpeed(double speed) {
        frontLeft.setPower(speed);
    }

    public void setFrontRightSpeed(double speed) {
        frontRight.setPower(speed);
    }

    public void setRearLeftSpeed(double speed) {
        rearLeft.setPower(speed);
    }

    public void setRearRightSpeed(double speed) {
        rearRight.setPower(speed);
    }

}
