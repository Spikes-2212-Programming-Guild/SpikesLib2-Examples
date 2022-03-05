package frc.robot.subsystems.gripper;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A subsystem which represents a gripper. The gripper consists of two motors, that both rotate either inwards or
 * outwards. If they both rotate inwards, the gripper intakes a cargo. If they both rotate outwards, the gripper
 * dispenses the cargo.
 */
public class Gripper extends MotoredGenericSubsystem {

    public static final double INTAKE_SPEED = 0.5;
    public static final double DISPENSE_SPEED = -0.5;

    /**
     * With only a private constructor and a single private instance with a public getter, the singleton design pattern
     * ensures that there is only one instance of a specific class, while providing public access to this instance.
     */
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
        super(DISPENSE_SPEED, INTAKE_SPEED, "gripper", motorController1, motorController2);
        motorController2.setInverted(true);
    }

    /**
     * Adds any commands or data from this subsystem to the {@link SmartDashboard}.
     */
    @Override
    public void configureDashboard() {
        rootNamespace.putData("intake", new MoveGenericSubsystem(this, INTAKE_SPEED));
        rootNamespace.putData("dispense", new MoveGenericSubsystem(this, DISPENSE_SPEED));
    }
}
