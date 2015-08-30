package wenzel.paul.speechsynthesis.model;

import java.awt.Color;

/**
 * Die Klasse {@link Model} [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public class Model implements ViewModel {
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	private int minWidth;
	private int minHeight;
	private int pointDiameter;
	
	private double[] wavFileValues;
	private long wavFileDuration;
	
	private Color backgroundColor;
	private Color lineColor;
	private Color pointColor;

	private long currentPoint;
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link Model}. 
	 */
	public  Model(double[] wavFileValues, int width, int height, int pointDiameter, Color backgroundColor, Color lineColor, Color pointColor) {
		//Datenfelder initialisieren
		this.minWidth = width;
		this.minHeight = height;
		this.pointDiameter = pointDiameter;
		this.backgroundColor = backgroundColor;
		this.lineColor = lineColor;
		this.pointColor = pointColor;
		this.wavFileValues = wavFileValues;
		
		wavFileDuration = 0;
		
		currentPoint = 0;
	}
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////
	
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
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	
	
	
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}