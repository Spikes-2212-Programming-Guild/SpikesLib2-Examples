package frc.robot.drivetrains.tankdrivetrain;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Handles the active control over the robot from the user's side.
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
