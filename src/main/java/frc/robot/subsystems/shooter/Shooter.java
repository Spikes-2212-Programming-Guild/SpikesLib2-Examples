package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystemWithPIDForSpeed;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.TalonEncoder;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import java.util.function.Supplier;

/**
 * A shooter subsystem that is controlled with two {@link WPI_TalonSRX}.<br>
 * In addition, it has settings for PID and matching commands, which you can use and control through the {@link Shuffleboard}.
 */
public class Shooter extends MotoredGenericSubsystem {

    /**
     * Number of rotations the wheel does for each encoder's pulse.
     *
     * <p>To find the correct distance per pulse, you will need to find the number <br>
     * of pulses in one rotation (e.g. 4096). The distance per pulse will be 1 divided by this number.</p>
     */
    public static final double DISTANCE_PER_PULSE = 1 / 4096.0;

    public static final double MAX_SPEED = 0.6;
    public static final double MIN_SPEED = 0;

    /**
     * A {@link Namespace} is an object that holds values in a {@link NetworkTable}.<br>
     * This namespace holds the necessary constants for the PID loop used by this subsystem.
     * It is a child (a sub-namespace) of the {@link RootNamespace}.
     */
    private final Namespace pidNamespace = rootNamespace.addChild("PID");
    private final Namespace ffNamespace = rootNamespace.addChild("feed forward");

    public final Supplier<Double> SHOOTING_SPEED = rootNamespace.addConstantDouble("speed", 0.4);

    /**
     * Places the PID constants on the {@link Shuffleboard}.
     */
    private final Supplier<Double> kP = pidNamespace.addConstantDouble("kP", 1);
    private final Supplier<Double> kI = pidNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kD = pidNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> TOLERANCE = pidNamespace.addConstantDouble("tolerance", 0);
    private final Supplier<Double> WAIT_TIME = pidNamespace.addConstantDouble("wait time", 1);
    private final Supplier<Double> targetVelocity = pidNamespace.addConstantDouble("target speed", 60);

    /**
     * Places the feed forward constants on the {@link Shuffleboard}.
     */
    private final Supplier<Double> kS = ffNamespace.addConstantDouble("kS", 0);
    private final Supplier<Double> kV = ffNamespace.addConstantDouble("kV", 0);

    public final PIDSettings pidSettings = new PIDSettings(kP, kI, kD, TOLERANCE, WAIT_TIME);
    public final FeedForwardSettings ffSettings = new FeedForwardSettings(kS, kV, () -> 0.0);

    /**
     * The Shooter class is a singleton, which means it has only one instance. Since the constructor is private, a new
     * instance cannot be instantiated, and you can only access the existing instance via the {@link #getInstance()}
     * function.
     * Since the robot itself has only one shooter, we want only a single instance of the class.
     */
    private static Shooter instance;

    /**
     * An encoder that is connected to a {@link WPI_TalonSRX} as the PIDSource.
     */
    private final TalonEncoder encoder;

    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter(new WPI_TalonSRX(RobotMap.CAN.SHOOTER_TALON_ONE),
                    new WPI_TalonSRX(RobotMap.CAN.SHOOTER_TALON_TWO));
        }
        return instance;
    }

    private Shooter(WPI_TalonSRX talon1, WPI_TalonSRX talon2) {
        super(MIN_SPEED, MAX_SPEED, "shooter", talon1, talon2);
        this.encoder = new TalonEncoder(talon1, DISTANCE_PER_PULSE);
    }

    /**
     * Adds any commands or data from this subsystem to the {@link Shuffleboard}.
     */
    @Override
    public void configureDashboard() {
        rootNamespace.putData("shoot", new MoveGenericSubsystem(this, SHOOTING_SPEED));
        rootNamespace.putData("pid shoot", new MoveGenericSubsystemWithPIDForSpeed(this, targetVelocity,
                encoder::getVelocity, pidSettings, ffSettings));
    }
}
