package wenzel.paul.speechsynthesis.model;

import java.awt.Color;

public interface DrawWavPanelModel {

	public int getMinWidth();
	public int getMinHeight();
	public int getPointDiameter();
	
	/** die Werte die als Punkte eingezeichnet werden sollen (komplett verteilt auf die gesammte verfügbare Breite) */
	public double[] getWavFileValues();
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
