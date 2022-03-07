package frc.robot.drivetrains.advancedtankdrivetrain;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Handles the communication between the user and the robot.
 */
public class OI /*GEVALD*/ {

    private final Joystick leftJoystick = new Joystick(0);
    private final Joystick rightJoystick = new Joystick(1);

    public OI() {
    }

    public double getLeftX() {
        return leftJoystick.getX();
    }

    public double getRightY() {
        return rightJoystick.getY();
    }
}
