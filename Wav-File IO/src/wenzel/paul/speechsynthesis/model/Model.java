package wenzel.paul.speechsynthesis.model;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;

import wenzel.paul.speechsynthesis.model.dataobjects.WavFileDataObject;

/**
 * Die Klasse {@link Model} [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public class Model implements ViewModel, WavFileControllPanelModel, WavFilePlayerModel, PresentationLayersControllPanelModel {
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	private int minWidth;
	private int minHeight;
	private int pointDiameter;
	
	private WavFileDataObject wavFile;
	
	private Color backgroundColor;
	private Color transparentBackgroundColor;
	private Color lineColor;
	private Color pointColor;
	private Color hilightColor;
	private	HashSet<Integer> indexOfSamplesToHilight;
	
	private boolean showWavFilePresentation;
	private boolean showPeeksPresentation;
	private boolean showPolygonsOfPeeksPresentation;
	
	private boolean loopPlayback;
	
	private float currentZoomLevel;

/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link Model}. 
	 */
	public  Model(WavFileDataObject wavFile, int width, int height, int pointDiameter, Color backgroundColor, Color transparentBackgroundColor, Color lineColor, Color pointColor, Color hilightColor, float currentZoomLevel) {
		//Datenfelder initialisieren
		this.minWidth = width;
		this.minHeight = height;
		this.pointDiameter = pointDiameter;
		this.backgroundColor = backgroundColor;
		this.transparentBackgroundColor = transparentBackgroundColor;
		this.lineColor = lineColor;
		this.pointColor = pointColor;
		this.hilightColor = hilightColor;
		this.wavFile = wavFile;
		this.currentZoomLevel = currentZoomLevel;
		
		indexOfSamplesToHilight = new HashSet<Integer>();
		loopPlayback = false;
		showWavFilePresentation = true;
		showPeeksPresentation = false;
		showPolygonsOfPeeksPresentation = false;
	}
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	public void setMinWidth(int width) {
		this.minWidth = width;
	}
	
	public void setLoopPlayback(boolean loopPlayback) {
		this.loopPlayback = loopPlayback;
	}
	
	public void setIndexOfSamplesToHilight(HashSet<Integer> indexOfSamplesToHilight) {
		this.indexOfSamplesToHilight = indexOfSamplesToHilight;
	}
	
	public void setShowWavFilePresentation(boolean showWavFilePresentation) {
		this.showWavFilePresentation = showWavFilePresentation;
	}
	
	public void setShowPeeksPresentation(boolean showPeeksPresentation) {
		this.showPeeksPresentation = showPeeksPresentation;
	}
	
	public void setShowPolygonsOfPeeksPresentation(boolean showPolygonsOfPeeksPresentation) {
		this.showPolygonsOfPeeksPresentation = showPolygonsOfPeeksPresentation;
	}
	
	public void setCurrentZoomLevel(float currentZoomLevel) {
		this.currentZoomLevel = currentZoomLevel;
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
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public Color getTransparentBackgroundColor() {
		return transparentBackgroundColor;
	}

	public Color getLineColor() {
		return lineColor;
	}

	public Color getPointColor() {
		return pointColor;
	}
	
	public Color getHilightColor() {
		return hilightColor;
	}

	public HashSet<Integer> getIndexOfSamplesToHilight() {
		return indexOfSamplesToHilight;
	}
	
	public int[] getIndexOfStartAndEndSample() {
		int[] indexOfStartAndEndSample;
		
		if (indexOfSamplesToHilight.size() >= 2) {
			Integer[] array = new Integer[2];
			indexOfSamplesToHilight.toArray(array);
			indexOfStartAndEndSample = new int[]{array[0], array[1]};
		} else {
			indexOfStartAndEndSample = new int[]{0, wavFile.getNumberOfFrames()};
		}
		
		
		// das Array der Größe nach sortieren
		Arrays.sort(indexOfStartAndEndSample);
		return indexOfStartAndEndSample;
	}
	
	public boolean loopPlayBack() {
			return loopPlayback;
	}
	
	public boolean isShowPeeksPresentation() {
		return showPeeksPresentation;
	}

	public boolean isShowWavFilePresentation() {
		return showWavFilePresentation;
	}
	
	public boolean isShowPolygonsOfPeeksPresentation() {
		return showPolygonsOfPeeksPresentation;
	}
	
	public float getCurrentZoomLevel() {
		return currentZoomLevel;
	}
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	

	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}