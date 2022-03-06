// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.transfer;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {

    public static final double TRANSFER_SPEED = 0.5;

    /**
     * A namespace is an object that holds values in a {@link NetworkTable}.
     */
    private RootNamespace robotNamespace;

    /*
     * A subsystem which transfers cargos. It consists of two MotorControllers that move a timing strap. The strap
     * puts pressure on a cargo, and thus the cargo moves along with the strap.
     */
    MotoredGenericSubsystem transfer;


    @Override
    public void robotInit() {
        robotNamespace = new RootNamespace("robot");

        transfer = new MotoredGenericSubsystem("transfer",
                new WPI_VictorSPX(RobotMap.CAN.TRANSFER_VICTOR_1), new WPI_VictorSPX(RobotMap.CAN.TRANSFER_VICTOR_2));

        // Places a command which moves the transfer subsystem on the SmartDashboard.
        robotNamespace.putData("move transfer", new MoveGenericSubsystem(transfer, TRANSFER_SPEED));
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void simulationInit() {
    }

    @Override
    public void simulationPeriodic() {
    }
}
