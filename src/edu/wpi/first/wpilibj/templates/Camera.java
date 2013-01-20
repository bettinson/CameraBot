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

	AxisCamera camera;
	ParticleAnalysisReport[] orderedParticles;
	ParticleAnalysisReport first;
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
			ColorImage colorImage = camera.getImage();
			relay.set(Relay.Value.kOff);
			binaryImage = colorImage.thresholdRGB(imageValues[0], imageValues[1], imageValues[2], imageValues[3], imageValues[4], imageValues[5]);
			colorImage.free();
			binaryImage = binaryImage.removeSmallObjects(true, 1);
			binaryImage = binaryImage.convexHull(true);

			orderedParticles = binaryImage.getOrderedParticleAnalysisReports();
			binaryImage.free();
			largestParticle = orderedParticles[0];
			System.out.println(largestParticle.center_mass_x);
			System.out.println(largestParticle.center_mass_y);
		} catch (AxisCameraException ex) {
			ex.printStackTrace();
		} catch (NIVisionException ex) {
			ex.printStackTrace();
		}
		
		return orderedParticles;
	}

	public String leftOrRight() {
		if (largestParticle.center_mass_x < camera.getResolution().width / 2 + 10) {
			return "right";
		} else if (largestParticle.center_mass_x > camera.getResolution().width / 2 - 10) {
			return "left";
		} else if (largestParticle.center_mass_x >= camera.getResolution().width / 2 + 10 || largestParticle.center_mass_x <= camera.getResolution().width / 2 - 10) {
			return "centre";
		}
		return "nil, yo.";
	}

	public void takePicture(int[] values) {
		try {
			ColorImage img = camera.getImage();
			BinaryImage bin = img.thresholdRGB(values[0], values[1], values[2], values[3], values[4], values[5]);
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
