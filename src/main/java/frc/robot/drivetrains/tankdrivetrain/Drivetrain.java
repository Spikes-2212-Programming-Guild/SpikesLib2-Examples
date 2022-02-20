package frc.robot.drivetrains.tankdrivetrain;

import com.spikes2212.command.drivetrains.TankDrivetrain;
import edu.wpi.first.wpilibj.Encoder;

import java.util.function.Supplier;

public class Drivetrain extends TankDrivetrain {

    private Encoder leftEncoder, rightEncoder;

    private final Supplier<Double> kP = rootNamespace.addConstantDouble("kP", 0);
    private final Supplier<Double> kI = rootNamespace.addConstantDouble("kP", 0);
    private final Supplier<Double> kD = rootNamespace.addConstantDouble("kP", 0);
    private final Supplier<Double> TOLERANCE = rootNamespace.addConstantDouble("kP", 0);
    private final Supplier<Double>  = rootNamespace.addConstantDouble("kP", 0);


}
