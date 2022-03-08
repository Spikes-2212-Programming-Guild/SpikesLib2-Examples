package frc.robot.subsystems.gripper;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A gripper subsystem which consists of two motors, that both rotate either inwards or
 * outwards. If they both rotate inwards, the gripper intakes a ball. If they both rotate outwards, the gripper
 * dispenses the ball.
 */
public class Gripper extends MotoredGenericSubsystem {

    public static final double INTAKE_SPEED = 0.5;
    public static final double DISPENSE_SPEED = -0.5;

    /**
     * The Gripper class is a singleton, which means it has only one instance. Since the constructor is private, a new
     * instance cannot be instantiated, and you can only access the existing instance via the {@link #getInstance()}
     * function. <br>
     * Since the robot itself only has one gripper, we want only a single instance of the class.
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
