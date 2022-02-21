package frc.robot.subsystems.feeder;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
import com.spikes2212.control.PIDSettings;
import com.spikes2212.dashboard.Namespace;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import java.util.function.Supplier;

public class Feeder extends MotoredGenericSubsystem {

    private static final double DISTANCE_PER_PULSE = 1.0/1024;

    private final Supplier<Double> SETPOINT = rootNamespace.addConstantDouble("setpoint", 2.2);

    private final Supplier<Double> SPEED = rootNamespace.addConstantDouble("speed", 0.7);

    private final Namespace pidNS = rootNamespace.addChild("pid");
    private final Supplier<Double> kP = pidNS.addConstantDouble("kP", 0);
    private final Supplier<Double> kI = pidNS.addConstantDouble("kI", 0);
    private final Supplier<Double> kD = pidNS.addConstantDouble("kD", 0);
    private final Supplier<Double> TOLERANCE = pidNS.addConstantDouble("tolerance", 0);
    private final Supplier<Double> WAIT_TIME = pidNS.addConstantDouble("wait time", 1);
    private final PIDSettings pidSettings = new PIDSettings(kP, kI, kD, TOLERANCE, WAIT_TIME);

    private final Encoder encoder;

    private boolean isOpen = true;

    private boolean enabled;

    private static Feeder instance;

    public static Feeder getInstance() {
        if (instance == null)
            instance = new Feeder();
        return instance;
    }

    private Feeder() {
        super("feeder", new WPI_VictorSPX(RobotMap.CAN.FEEDER_VICTOR));
        encoder = new Encoder(RobotMap.DIO.FEEDER_ENCODER_A, RobotMap.DIO.FEED_ENCODER_B);
        encoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        enabled = true;
    }

    @Override
    public boolean canMove(double speed) {
        return enabled;
    }

    public void resetEncoder() {
        encoder.reset();
    }

    public double getPosition() {
        return encoder.getDistance();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putNumber("encoder pos", encoder::getDistance);
        rootNamespace.putData("feed", new MoveGenericSubsystem(this, SPEED));
        rootNamespace.putData("move with pid", new MoveGenericSubsystemWithPID(this, SETPOINT,
                encoder::getDistance, pidSettings));
        rootNamespace.putData("reset encoder", new InstantCommand(encoder::reset));
    }
}
