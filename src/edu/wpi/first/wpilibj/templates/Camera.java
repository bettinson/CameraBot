/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 *
 * @author mattbettinson
 */

public class Camera {

	public static class Direction {
	
		public final int value;
		static final int left_val = 0;
		static final int right_val = 1;
		static final int center_val = 2;
		static final int error_val = -1;

		public static final Direction left = new Direction(left_val);
		public static final Direction right = new Direction(right_val);
		public static final Direction center = new Direction(center_val);
		public static final Direction error = new Direction(error_val);


		private Direction (int value) {
			this.value = value;
		}
	}
	
	AxisCamera camera;
	ParticleAnalysisReport[] orderedParticles;
	int firstsWidth, pixelCentre, close;
	AnalogChannel ultraSonic;
	ParticleAnalysisReport largestParticle;
	Relay relay;
	BinaryImage binaryImage;
	
	public Camera() {
		camera = AxisCamera.getInstance();
		camera.writeBrightness(50);
		relay = new Relay(Config.LIGHTS);
		relay.setDirection(Relay.Direction.kReverse);
	}

	public ParticleAnalysisReport[] getLargestParticle(int[] imageValues) {
		try {
			relay.set(Relay.Value.kOn);
			System.out.println("Okay.");
			ColorImage colorImage = camera.getImage(); 			

			binaryImage = colorImage.thresholdRGB(imageValues[0], imageValues[1], imageValues[2], imageValues[3], imageValues[4], imageValues[5]);
			colorImage.free();
			binaryImage = binaryImage.convexHull(false);
			binaryImage = binaryImage.removeSmallObjects(false, 1);
			orderedParticles = binaryImage.getOrderedParticleAnalysisReports();
			binaryImage.free();
		} catch (AxisCameraException ex) {
			ex.printStackTrace();
		} catch (NIVisionException ex) {
			System.out.println("ZOMG ERROR " + ex.getMessage());
		}

		return orderedParticles;
	}

	public Direction leftOrRight(ParticleAnalysisReport sender) {
		int camWidth = camera.getResolution().width;

		if (sender.center_mass_x < ((camWidth / 2) - 30)) {
			return Direction.left;
		} else if (sender.center_mass_x > ((camWidth / 2) + 30)) {
			return Direction.right;
		} else if (sender.center_mass_x >= ((camWidth / 2) - 30) && sender.center_mass_x <= ((camWidth / 2) + 30)) {
			return Direction.center;
		}else {
			return Direction.error;
		}
	}

	public void takePicture(int[] values) {
		try {
			ColorImage img = camera.getImage();
			BinaryImage bin = img.thresholdRGB(values[0], values[1], values[2], values[3], values[4], values[5]);
			bin = bin.removeSmallObjects(true, 1);
			bin= bin.convexHull(true);
			img.free();
			bin = bin.removeSmallObjects(true, 1);
			bin = bin.convexHull(true);
			bin.write("/testMattISSIRI.png");
			bin.free();

		} catch (NIVisionException ex) {
			ex.printStackTrace();
		} catch (AxisCameraException ex) {
			ex.printStackTrace();
		}
	}

	
	
	public void getNormalPicture() {
		try {
			ColorImage img = camera.getImage();
			img.write("/testMattIsGood.png");
			img.free();
		} catch (NIVisionException ex) {
			ex.printStackTrace();
		} catch (AxisCameraException ex) {
			ex.printStackTrace();
		}
	}
}