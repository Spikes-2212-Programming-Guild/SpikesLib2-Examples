package frc.robot.subsystems.gripper;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

import java.util.function.Supplier;

public class Gripper extends MotoredGenericSubsystem {

    public final Supplier<Double> INTAKE_SPEED = rootNamespace.addConstantDouble("intake speed", 0.5);
    public final Supplier<Double> DISPENSE_SPEED = rootNamespace.addConstantDouble("dispense speed", -0.5);

    private static Gripper instance;

    public static Gripper getInstance() {
        if (instance == null) {
            instance = new Gripper(
                    new WPI_VictorSPX(RobotMap.CAN.LEFT_GRIPPER_VICTOR),
                    new WPI_VictorSPX(RobotMap.CAN.RIGHT_GRIPPER_VICTOR));
        }
        return instance;
    }

    private Gripper(MotorController motorController1, MotorController motorController2) {
        super("gripper", motorController1, motorController2);
        motorController2.setInverted(true);
    }

    @Override
    public void configureDashboard() {
        rootNamespace.putData("intake", new MoveGenericSubsystem(this, INTAKE_SPEED));
        rootNamespace.putData("dispense", new MoveGenericSubsystem(this, DISPENSE_SPEED));
    }
}
