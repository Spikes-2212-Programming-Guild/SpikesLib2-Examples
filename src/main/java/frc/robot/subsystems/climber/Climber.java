package frc.robot.subsystems.climber;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import java.util.function.Supplier;

/**
 * A climber subsystem which consists of an arm that is controlled by a {@link WPI_VictorSPX}. The arm
 * can move up and grab a bar, then down to pull the robot up; thus making the robot climb. <br>
 * A game that this example subsystem can be used in is Rapid React 2022.
 */
public class Climber extends MotoredGenericSubsystem {

    /**
     * The speed in which the arms move up to catch the bar (in order to climb).
     */
    public static final double UP_SPEED = 0.3;

    /**
     * The speed in which the arms move down in order to take the robot up after they caught the bar.
     */
    public static final double DOWN_SPEED = -0.4;

    /**
     * The Climber class is a singleton, which means it has only one instance. Since the constructor is private, a new
     * instance cannot be instantiated, and you can only access the existing instance via the {@link #getInstance()}
     * function.
     * Since the robot itself only has one climber, we want only a single instance of the class.
     */
    private static Climber instance;

    public static Climber getInstance() {
        if (instance == null) {
            WPI_VictorSPX motor = new WPI_VictorSPX(RobotMap.CAN.CLIMBER_VICTOR);
            motor.setNeutralMode(NeutralMode.Brake);
            instance = new Climber(motor);
        }
        return instance;
    }

    private Climber(WPI_VictorSPX motor) {
        super(DOWN_SPEED, UP_SPEED, "climber", motor);
    }

    /**
     * Adds any commands or data from this subsystem to the {@link Shuffleboard}.
     */
    @Override
    public void configureDashboard() {
        rootNamespace.putData("move up", new MoveGenericSubsystem(this, UP_SPEED));
        rootNamespace.putData("move down", new MoveGenericSubsystem(this, DOWN_SPEED));
    }
}
