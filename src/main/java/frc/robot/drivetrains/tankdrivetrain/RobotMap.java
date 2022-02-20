package frc.robot.drivetrains.tankdrivetrain;

/**
 * <p>
 * A class which holds the ports of the devices which are connected to the robot.
 * </p>
 * Each interface represents a different form of connection.
 *
 * TODO: remove documentation once there is a readme
 */
public class RobotMap {

    public interface CAN {

        int LEFT_TALON_1 = 0;
        int LEFT_TALON_2 = 1;
        int RIGHT_TALON_1 = 2;
        int RIGHT_TALON_2 = 3;
    }
    
    public interface DIO {
        int LEFT_ENCODER_A = 0;
        int LEFT_ENCODER_B = 1;
        int RIGHT_ENCODER_A = 2;
        int RIGHT_ENCODER_B = 3;
    }
    
    public interface PWM {

    }
    
    public interface AIN {
    
    }
}
