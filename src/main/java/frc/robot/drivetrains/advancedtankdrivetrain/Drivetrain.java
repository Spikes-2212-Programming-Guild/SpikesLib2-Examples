package frc.robot.drivetrains.advancedtankdrivetrain;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.command.drivetrains.commands.DriveTankWithPID;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.BustedMotorControllerGroup;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import java.util.function.Supplier;

/**
 * This class represents a type of Drivetrain that its left and right sides are controlled independently, allowing it to
 * move by giving each side a speed value separately.
 * It can move forward/backward by giving both its sides an equal speed or turn by giving the sides different speeds.
 * In addition, this drivetrain includes encoders, a gyro sensor, settings for PID and FeedForward controllers, and communication with
 * the dashboard.
 */
public class Drivetrain extends TankDrivetrain {

    /**
     * <p> A namespace is an object that holds values in a {@link NetworkTable}. </p>
     * This is a namespace used for configuring the {@code LEFT_CORRECTION} and the {@code RIGHT_CORRECTION}.
     */
    private static final RootNamespace corrections = new RootNamespace("corrections");

    /**
     * Constants that are used for fixing the drivetrain's deviation. More info in {@link BustedMotorControllerGroup}.
     * Since they were made using {@code addConstantDouble}, they are constants relative to the code itself, but are
     * still able to be changed via the shuffleboard.
     */
    private static final Supplier<Double> LEFT_CORRECTION =
            corrections.addConstantDouble("left correction", 1);
    private static final Supplier<Double> RIGHT_CORRECTION =
            corrections.addConstantDouble("right correction", 1);

    /**
     * The distance the robot moves every encoder pulse.
     * Our wheel moves 15.24 * PI (its perimeter) each 360 ticks (in meters).
     */
    private static final double DISTANCE_PER_PULSE = 15.24 * Math.PI / 360.0 / 100;

    private final Encoder leftEncoder, rightEncoder;
    private final ADXRS450_Gyro gyro;

    private final Supplier<Double> DRIVE_SPEED = rootNamespace.addConstantDouble("drive speed", 0.5);
    private final Supplier<Double> METERS_TO_DRIVE = rootNamespace.addConstantDouble("meters to drive", 2);

    /**
     * A {@link Namespace} is an object which holds values on a {@link NetworkTable}. <br>
     * This is a child, or a sub-namespace, of the subsystem's {@link RootNamespace}.
     */
    private final Namespace pidNamespace = rootNamespace.addChild("pid");

    /**
     * Places the PID constants on the {@link Shuffleboard}.
     */
    private final Supplier<Double> kP = pidNamespace.addConstantDouble("kP", 1);
    private final Supplier<Double> kI = pidNamespace.addConstantDouble("kI", 0);
    private final Supplier<Double> kD = pidNamespace.addConstantDouble("kD", 0);
    private final Supplier<Double> TOLERANCE = pidNamespace.addConstantDouble("tolerance", 0);
    private final Supplier<Double> WAIT_TIME = pidNamespace.addConstantDouble("wait time", 1);

    private final PIDSettings pidSettings = new PIDSettings(kP, kI, kD, TOLERANCE, WAIT_TIME);

    private final Namespace ffNamespace = rootNamespace.addChild("feed forward");

    /**
     * Places the FeedForward constants on the {@link Shuffleboard}.
     */
    private final Supplier<Double> kS = ffNamespace.addConstantDouble("kS", 0);
    private final Supplier<Double> kV = ffNamespace.addConstantDouble("kV", 0);
    private final Supplier<Double> kA = ffNamespace.addConstantDouble("kA", 0);

    private final FeedForwardSettings ffSettings = new FeedForwardSettings(kS, kV, kA);

    /**
     * The Drivetrain class is a singleton, which means it has only one instance. Since the constructor is private, a new
     * instance cannot be instantiated, and you can only access the existing instance via the {@link #getInstance}
     * function.
     * Since the robot itself has only one drivetrain, we want a single instance of the class.
     */
    private static Drivetrain instance;

    public static Drivetrain getInstance() {
        if (instance == null)
            instance = new Drivetrain();
        return instance;
    }

    private Drivetrain() {
        super(
                "drivetrain",
                new BustedMotorControllerGroup(
                        LEFT_CORRECTION,
                        new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_LEFT_TALON_1),
                        new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_LEFT_TALON_2)
                ),
                new BustedMotorControllerGroup(
                        RIGHT_CORRECTION,
                        new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_RIGHT_TALON_1),
                        new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_RIGHT_TALON_2)
                )
        );
        this.leftEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_A, RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_B);
        this.rightEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_A, RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_B);
        leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        this.gyro = new ADXRS450_Gyro();
    }

    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public void resetGyro() {
        gyro.reset();
    }

    /**
     * Sets the values which appear on the shuffleboard using a {@link Namespace}.
     */
    public void configureDashboard() {
        rootNamespace.putNumber("left distance", this::getLeftDistance);
        rootNamespace.putNumber("right distance", this::getRightDistance);
        rootNamespace.putNumber("gyro angle", this::getAngle);
        rootNamespace.putNumber("gyro modified angle", this::getModifiedAngle);
        rootNamespace.putData("reset encoders", new InstantCommand(this::resetEncoders));
        rootNamespace.putData("reset gyro", new InstantCommand(this::resetGyro));
        rootNamespace.putData("drive forward", new DriveArcade(this, DRIVE_SPEED, () -> 0.0));
        rootNamespace.putData("drive backward", new DriveArcade(this, () -> -DRIVE_SPEED.get(), () -> 0.0));
        rootNamespace.putData("drive meters", new DriveTankWithPID(this, pidSettings, pidSettings,
                METERS_TO_DRIVE, METERS_TO_DRIVE, this::getLeftDistance, this::getRightDistance, ffSettings, ffSettings));
    }

    /**
     * @return the robot's current angle in the range (-infinity, infinity)
     */
    public double getAngle() {
        return gyro.getAngle();
    }

    /**
     * @return the robot's current angle in the range (-180, 180]
     */
    public double getModifiedAngle() {
        double angle = gyro.getAngle() % 360;
        if (angle > 180)
            angle -= 360;
        if (angle <= -180)
            angle += 360;
        return angle;
    }

    public double getLeftDistance() {
        return leftEncoder.getDistance();
    }

    public double getRightDistance() {
        return rightEncoder.getDistance();
    }

    public PIDSettings getPIDSettings() {
        return pidSettings;
    }

    public FeedForwardSettings getFFSettings() {
        return ffSettings;
    }
}
