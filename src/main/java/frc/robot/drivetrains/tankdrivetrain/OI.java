package frc.robot.drivetrains.tankdrivetrain;

import edu.wpi.first.wpilibj.Joystick;

/**
 * An object which represents an OI, which handles the active control over the robot from the user's side.
 */
public class OI /*GEVALD*/{
    private final Joystick leftJoystick;
    private final Joystick rightJoystick;

    public OI() {
        leftJoystick = new Joystick(0);
        rightJoystick = new Joystick(1);
    }

    public double getLeftX() {
        return leftJoystick.getX();
    }

    public double getLeftY() {
        return leftJoystick.getY();
    }

    public double getRightX() {
        return rightJoystick.getX();
    }

    public double getRightY() {
        return rightJoystick.getY();
    }
}
