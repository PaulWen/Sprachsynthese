package wenzel.paul.speechsynthesis.model;

import java.awt.Color;

import wenzel.paul.speechsynthesis.model.dataobjects.WavFileDataObject;

public interface DrawWavPanelModel {

	public int getMinWidth();
	public int getMinHeight();
	public int getPointDiameter();
	
	/** die Werte die als Punkte eingezeichnet werden sollen (komplett verteilt auf die gesammte verf�gbare Breite) */
	public WavFileDataObject getWavFile();
	/** wie vielen Millisekunden die WAV Datei lang ist */
	public long getWavFileDuration();
	
	/** Farbe vom Hintergrund */
	public Color getBackgroundColor(); 
	/** Farbe in welcher die Linie gemalt wird */
	public Color getLineColor(); 
	/** Farbe in welcher die unkte gemalt werden */
	public Color getPointColor();
	
	/** der Index vom Wert, welcher gerade gespielt wird */ 
	public long getCurrentPoint();
}
