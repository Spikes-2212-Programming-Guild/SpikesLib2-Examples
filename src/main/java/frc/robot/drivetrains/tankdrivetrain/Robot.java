// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.drivetrains.tankdrivetrain;

import com.spikes2212.command.drivetrains.commands.DriveArcade;
import com.spikes2212.dashboard.RootNamespace;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    /**
     * <p>
     * A namespace is an object which holds value on a {@link NetworkTable}.
     * </p>
     * <p>
     * This is the main namespace which should host the main values and commands which don't belong to a single subsystem
     * or command.
     */
    private final RootNamespace robotNamespace = new RootNamespace("robot");

    private final Drivetrain drivetrain = Drivetrain.getInstance();

    /**
     * constructs an instance of OI - a class for interfacing with the robot.
     */
    private final OI oi = new OI();

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        /*
        * DriveArcade is a command which allows the user to control the speed in which the robot moves forward (or
        * backwards) and the speed in which it rotates separately.
        * In this case, we give it the X value of the left joystick as its rotate speed, and the Y value of the right
        * joystick as its move speed.
        */
        DriveArcade driveArcade = new DriveArcade(drivetrain, oi::getLeftX, oi::getRightY);

        /*
        * Puts the driveArcade on the dashboard under the keyname "drive", so that the user can easily turn it on and
        * off using the shuffleboard.
        */
        robotNamespace.putData("drive", driveArcade);
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        /*
        * A function included in TankDrivetrain which is supposed to run constantly.
        */
        drivetrain.periodic();
        CommandScheduler.getInstance().run();
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     */
    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousInit() {

    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }

    /**
     * This function is called once when the robot is first started up.
     */
    @Override
    public void simulationInit() {
    }

    /**
     * This function is called periodically whilst in simulation.
     */
    @Override
    public void simulationPeriodic() {
    }
}
