package wenzel.paul.speechsynthesis.model;

import java.awt.Color;

import wenzel.paul.speechsynthesis.model.dataobjects.WavFileDataObject;

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
	
	private WavFileDataObject wavFile;
	private long wavFileDuration;
	
	private Color backgroundColor;
	private Color lineColor;
	private Color pointColor;

	private long currentPoint;
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link Model}. 
	 */
	public  Model(WavFileDataObject wavFile, int width, int height, int pointDiameter, Color backgroundColor, Color lineColor, Color pointColor) {
		//Datenfelder initialisieren
		this.minWidth = width;
		this.minHeight = height;
		this.pointDiameter = pointDiameter;
		this.backgroundColor = backgroundColor;
		this.lineColor = lineColor;
		this.pointColor = pointColor;
		this.wavFile = wavFile;
		
		wavFileDuration = 0;
		
		currentPoint = 0;
	}
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	public void setMinWidth(int width) {
		this.minWidth = width;
	}
	
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

	public WavFileDataObject getWavFile() {
		return wavFile;
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
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	
	
	
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}