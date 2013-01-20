/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

	int sleepForMili = 500;
	Camera cam;
	boolean bool = false;
	Bundle leftSide = new Bundle(1, 2);
	Bundle rightSide = new Bundle(3, 4);

	public void robotInit() {
		cam = new Camera();

	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		int[] array = {202, 255, 86, 207, 0, 255};
		ParticleAnalysisReport[] orderedParticles;
		orderedParticles = cam.getLargestParticle(array);

		System.out.println("Amount of particles:" + orderedParticles.length);
		System.out.println("The largest particle's center x mass:" + orderedParticles[0].center_mass_x);
		System.out.println("The largest particle's center y mass:" + orderedParticles[0].center_mass_y);

		//cam.getNormalPicture();
		bool = true;

		String lr = cam.leftOrRight();

		if (lr == "left") {
			rightSide.set(0.2);
			try {
				Thread.sleep(sleepForMili);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			rightSide.set(0);
		} else if (lr == "right") {
			leftSide.set(0.2);
			try {
				Thread.sleep(sleepForMili);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			leftSide.set(0);
		} else if (lr == "center") {
			System.out.println("YEEEEEE");
			leftSide.set(0);
			rightSide.set(0);
		}


	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
	}
}
