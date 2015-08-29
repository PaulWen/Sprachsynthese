package wenzel.paul.speechsynthesis.model;

import java.awt.Color;

public class Model implements ViewModel {

	private int minWidth;
	private int minHeight;
	private int pointDiameter;
	
	private double[] wavFileValues;
	private long wavFileDuration;
	
	private Color backgroundColor;
	private Color lineColor;
	private Color pointColor;

	private long currentPoint;
	
	
	public Model(double[] wavFileValues, int width, int height, int pointDiameter, Color backgroundColor, Color lineColor, Color pointColor) {
		this.minWidth = width;
		this. minHeight = height;
		this.pointDiameter = pointDiameter;
		this.backgroundColor = backgroundColor;
		this.lineColor = lineColor;
		this.pointColor = pointColor;
		this.wavFileValues = wavFileValues;
		
		wavFileDuration = 0;
		
		currentPoint = 0;
		
		
		
//		/// ZUM TESTEN ///
//		
//		// WAV-Datei öffnen
//		try {
//			// Open the wav file specified as the first argument
//			WavFile wavFile = WavFile.openWavFile(new File("res/wav_examples/Test.wav"));
//
//			// Display information about the wav file
//			wavFile.display();
//
//			// Get the number of audio channels in the wav file
//			int numChannels = wavFile.getNumChannels();
//
//			// Create a buffer of 100 frames
//			double[] buffer = new double[100 * numChannels];
//
//			int framesRead;
//			double min = Double.MAX_VALUE;
//			double max = Double.MIN_VALUE;
//
//			wavFileValues = new double[(int)wavFile.getNumFrames()];
//			int i = 0;
//			do {
//				// Read frames into buffer
//				framesRead = wavFile.readFrames(buffer, 100);
//
//				// Loop through frames and look for minimum and maximum value
//				for (int s = 0; s < framesRead * numChannels ; s++) {
//					wavFileValues[i++] = buffer[s];
//					if (buffer[s] > max) max = buffer[s];
//					if (buffer[s] < min) min = buffer[s];
//				}
//			} while (framesRead != 0);
//
//			// Close the wavFile
//			wavFile.close();
//
//			// Output the minimum and maximum value
//			System.out.printf("Min: %f, Max: %f\n", min, max);
//		}
//		catch (Exception exception) {
//			exception.printStackTrace();
//		}
	}
	
	public void setMinWidth(int width) {
		this.minWidth = width;
	}

	public void setMinHeight(int height) {
		this.minHeight = height;
	}

	public void setPointDiameter(int pointDiameter) {
		this.pointDiameter = pointDiameter;
	}

	public void setWavFileValues(double[] wavFileValues) {
		this.wavFileValues = wavFileValues;
	}
	public void setWavFileDuration(long wavFileDuration) {
		this.wavFileDuration = wavFileDuration;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public void setPointColor(Color pointColor) {
		this.pointColor = pointColor;
	}
	
	public void setCurrentPoint(long currentPoint) {
		this.currentPoint = currentPoint;
	}
	
	
	
	public int getMinWidth() {
		return minWidth;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public int getPointDiameter() {
		return pointDiameter;
	}

	public double[] getWavFileValues() {
		return wavFileValues;
	}
	public long getWavFileDuration() {
		return wavFileDuration;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public Color getLineColor() {
		return lineColor;
	}

	public Color getPointColor() {
		return pointColor;
	}

	public long getCurrentPoint() {
		return currentPoint;
	}
}