// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.drivetrains.tankDrivetrain;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {

    /**
     * <p> A namespace is an object that holds values on a {@link NetworkTable}.</p>
     * This is the main namespace which should host the main values and commands which don't belong to a single subsystem
     * or command.
     */
    private final RootNamespace robotNamespace = new RootNamespace("robot");

    private final Drivetrain drivetrain = Drivetrain.getInstance();

    private OI oi;

    @Override
    public void robotInit() {
        oi = new OI();
        drivetrain.configureDashboard();
    }

    @Override
    public void robotPeriodic() {
        drivetrain.periodic();
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
        DriveArcade driveArcade = new DriveArcade(drivetrain, oi::getLeftX, oi::getRightY);
        drivetrain.setDefaultCommand(driveArcade);

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
