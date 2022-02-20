package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystemWithPIDForSpeed;
import com.spikes2212.control.FeedForwardSettings;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.util.TalonEncoder;

import java.util.function.Supplier;

public class Shooter extends MotoredGenericSubsystem {

    public static final double DISTANCE_PER_PULSE = 10 / 4096.0;
    public static final double MAX_SPEED = 0.6;
    public static final double MIN_SPEED = 0;

    /**
     * The PID constants' place on the shuffleboard.
     */
    private final Namespace PID = rootNamespace.addChild("PID");

    public Supplier<Double> shootSpeed = rootNamespace.addConstantDouble("speed", 0.4);

    private final Supplier<Double> kP = PID.addConstantDouble("kP", 0);
    private final Supplier<Double> kI = PID.addConstantDouble("kI", 0);
    private final Supplier<Double> kD = PID.addConstantDouble("kD", 0);
    private final Supplier<Double> kS = PID.addConstantDouble("kS", 0);
    private final Supplier<Double> kV = PID.addConstantDouble("kV", 0);
    private final Supplier<Double> tolerance = PID.addConstantDouble("tolerance", 0);
    private final Supplier<Double> waitTime = PID.addConstantDouble("wait time", 0);

    private final Supplier<Double> targetSpeed = PID.addConstantDouble("target speed", 0);

    public final PIDSettings pidSettings = new PIDSettings(kP, kI, kD, tolerance, waitTime);
    public final FeedForwardSettings ffSettings = new FeedForwardSettings(kS, kV, () -> 0.0);

    private static Shooter instance;

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
     * Add any commands or data from this subsystem to the shuffleboard.
     */
    @Override
    public void configureDashboard() {
        rootNamespace.putData("shoot", new MoveGenericSubsystem(this, shootSpeed));
        rootNamespace.putData("pid shoot", new MoveGenericSubsystemWithPIDForSpeed(this, targetSpeed,
                encoder::getVelocity, pidSettings, ffSettings));
    }
}
