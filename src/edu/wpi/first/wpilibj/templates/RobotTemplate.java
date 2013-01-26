/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.templates.Camera.Direction;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class RobotTemplate extends IterativeRobot {

	int sleepForMili = 2;
	Camera cam;
	boolean bool = false;
	Bundle leftSide = new Bundle(1, 2);
	Bundle rightSide = new Bundle(3, 4);
	DriverStationLCD station;
	int[] RGBThreshold = {202, 255, 86, 207, 0, 255};
	ParticleAnalysisReport[] particles;
	Direction direction;

	Joystick joystick = new Joystick(1);
	boolean hasTakenPic = false;

	public void robotInit() {
		cam = new Camera();
		station = DriverStationLCD.getInstance();
	}

	/**
	 * This function is called periodically during autonomous
	 */

	public void testInit()
	{
		cam.relay.set(Relay.Value.kOn);
	}

	public void testPeriodic() {
		if (joystick.getTrigger())
		{
			particles = cam.getLargestParticle(RGBThreshold);
			hasTakenPic = true;
			System.out.println("PICTURE");
		}
		if (joystick.getRawButton(2))
		{
			 direction = cam.leftOrRight(particles[0]);
			 if (direction == Direction.left) {
				station.println(DriverStationLCD.Line.kUser1, 1 , "left ");
				/*
				rightSide.set(0.2);
				try {
					Thread.sleep(sleepForMili);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				rightSide.set(0);
				*/

			} else if (direction == Direction.right) {
				station.println(DriverStationLCD.Line.kUser1, 1 , "right");
				/*
				leftSide.set(0.2);
				try {
					Thread.sleep(sleepForMili);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				leftSide.set(0);
				 */
			} else if (direction == Direction.center) {
				station.println(DriverStationLCD.Line.kUser1, 1 , "center'");
				/*
				System.out.println("YEEEEEE");
				leftSide.set(0);
				rightSide.set(0);
				*/
			}

		}

		while(hasTakenPic){
			System.out.println("Array Length: " + particles.length);
			for (int i = 0; i < particles.length; i ++){
				System.out.println("Particle number " + i + " center x mass: " + particles[i].center_mass_x);
				System.out.println("Particle number " + i + " center y mass: " + particles[i].center_mass_y);
				System.out.println(" ");
			}
			hasTakenPic = false;
		}

		station.updateLCD();
	}

	/**
	 * This function is called periodically during operator control
	 */

	public void teleopPeriodic() {

		ParticleAnalysisReport[] orderedParticles;

		orderedParticles = cam.getLargestParticle(RGBThreshold);

		if (orderedParticles.length > 0) {
			System.out.println("Amount of particles:" + orderedParticles.length);
			System.out.println("The largest particle's center x mass:" + orderedParticles[0].center_mass_x);
			System.out.println("The largest particle's center y mass:" + orderedParticles[0].center_mass_y);


			Direction nextDirection = cam.leftOrRight(orderedParticles[0]);

			if (nextDirection == Direction.left) {
				rightSide.set(0.2);	
				try {
					Thread.sleep(sleepForMili);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				rightSide.set(0);

			} else if (nextDirection == Direction.right) {
				leftSide.set(0.2);
				try {
					Thread.sleep(sleepForMili);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				leftSide.set(0);
				
			} else if (nextDirection == Direction.center) {
				leftSide.set(0);
				rightSide.set(0);
				
			}
			station.updateLCD();
		}

		orderedParticles = null;
	}

	public void disabledPeriodic() {
		cam.relay.set(Relay.Value.kOff);
	}

	/**
	 * This function is called periodically during test mode
	 */


}
