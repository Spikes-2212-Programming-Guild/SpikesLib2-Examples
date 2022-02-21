package frc.robot.drivetrains.tankdrivetrain;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.control.FeedForwardController;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.BustedMotorControllerGroup;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.networktables.NetworkTable;

import java.util.function.Supplier;

/**
 * This class represents a type of Drivetrain that its left and right sides are controlled independently, allowing it to
 * move by giving each side a speed value separately.
 * It can move forward/backward by giving both its sides an equal speed or turn by giving the sides different speeds.
 * In addition, this drivetrain includes encoders, settings for PID and FeedForward controllers, and communication with
 * the dashboard.
 */
public class Drivetrain extends TankDrivetrain {

    /**
     * <p>
     * A namespace is an object which holds value on a {@link NetworkTable}.
     * </p>
     * This is a namespace used for configuring the {@code LEFT_CORRECTION} and the {@code RIGHT_CORRECTION}.
     */
    private static final RootNamespace corrections = new RootNamespace("corrections");

    /**
     * Constants which are used for fixing the drivetrain's deviation. More info in {@link BustedMotorControllerGroup}.
     * Since they were made using {@code addConstantDouble}, they are constants relative to the code itself, but are
     * still able to be changed via the shuffleboard.
     */
    private static final Supplier<Double> LEFT_CORRECTIONS =
            corrections.addConstantDouble("left correction", 1);
    private static final Supplier<Double> RIGHT_CORRECTIONS =
            corrections.addConstantDouble("right correction", 1);

    /**
     * The wheel moves 15.24 * PI (its perimeter) each 360 ticks (in meters).
     */
    private static final double DISTANCE_PER_PULSE = 15.24 * Math.PI / 360.0 / 100;

    private final Encoder leftEncoder, rightEncoder;
    private final ADXRS450_Gyro gyro;

    /**
     * constants for a PID controller
     */
    private final Supplier<Double> kP = rootNamespace.addConstantDouble("kP", 0);
    private final Supplier<Double> kI = rootNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kD = rootNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> TOLERANCE = rootNamespace.addConstantDouble("TOLERANCE", 0);
    private final Supplier<Double> WAIT_TIME = rootNamespace.addConstantDouble("WAIT_TIME", 1);

    private final PIDSettings pidSettings = new PIDSettings(kP, kI, kD, TOLERANCE, WAIT_TIME);

    /**
     * Constants for a feed forward controller. More info in {@link FeedForwardController}.
     */
    private final Supplier<Double> kS = rootNamespace.addConstantDouble("kS", 0);
    private final Supplier<Double> kV = rootNamespace.addConstantDouble("kV", 0);
    private final Supplier<Double> kA = rootNamespace.addConstantDouble("kA", 0);
    private final Supplier<Double> kG = rootNamespace.addConstantDouble("kG", 0);

    private final FeedForwardSettings ffSettings = new FeedForwardSettings(kS, kV, kA, kG);

    private static Drivetrain instance;

    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain();
            instance.configureDashboard();
        }
        return instance;
    }

    private Drivetrain() {
        super(
                "drivetrain",
                new BustedMotorControllerGroup(
                        LEFT_CORRECTIONS,
                        new WPI_TalonSRX(RobotMap.CAN.LEFT_TALON_1),
                        new WPI_TalonSRX(RobotMap.CAN.LEFT_TALON_2)
                ),
                new BustedMotorControllerGroup(
                        RIGHT_CORRECTIONS,
                        new WPI_TalonSRX(RobotMap.CAN.RIGHT_TALON_1),
                        new WPI_TalonSRX(RobotMap.CAN.RIGHT_TALON_2)
                )
        );
        this.leftEncoder = new Encoder(RobotMap.DIO.LEFT_ENCODER_A, RobotMap.DIO.LEFT_ENCODER_B);
        this.rightEncoder = new Encoder(RobotMap.DIO.RIGHT_ENCODER_A, RobotMap.DIO.RIGHT_ENCODER_B);
        leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        this.gyro = new ADXRS450_Gyro();
    }

    public double getLeftDistance() {
        return leftEncoder.getDistance();
    }

    public double getRightDistance() {
        return rightEncoder.getDistance();
    }

    public double getAngle() {
        double angle = gyro.getAngle() % 360;
        if (angle > 180)
            angle -= 360;
        if (angle < -180)
            angle += 360;
        return angle;
    }

    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public void resetGyro() {
        gyro.reset();
    }

    /**
     * sets the values which appear on the shuffleboard using a {@link Namespace}.
     */
    public void configureDashboard() {
        rootNamespace.putNumber("left distance", this::getLeftDistance);
        rootNamespace.putNumber("right distance", this::getRightDistance);
        rootNamespace.putNumber("angle", this::getAngle);
        rootNamespace.putData("reset encoders", new InstantCommand(this::resetEncoders));
        rootNamespace.putData("reset gyro", new InstantCommand(this::resetGyro));
        rootNamespace.putData("drive forward", new DriveArcade(this, 0.5, 0));
        rootNamespace.putData("drive backward", new DriveArcade(this, -0.5, 0));

    }

    public PIDSettings getPIDSettings() {
        return pidSettings;
    }

    public FeedForwardSettings getFFSettings() {
        return ffSettings;
    }
}
