package wenzel.paul.speechsynthesis.model;

import java.awt.Color;
import java.util.HashSet;

import wenzel.paul.speechsynthesis.model.dataobjects.WavFileDataObject;

public interface DrawWavPanelModel {

	public int getMinWidth();
	public int getMinHeight();
	public int getPointDiameter();
	
	/** die Werte die als Punkte eingezeichnet werden sollen (komplett verteilt auf die gesammte verfügbare Breite) */
	public WavFileDataObject getWavFile();
	
	/** Farbe vom Hintergrund */
	public Color getBackgroundColor(); 
	/** ein transparenter Hintergrund, welche mehrere gemalte Schichten trennen soll */
	public Color getTransparentBackgroundColor();
	/** Farbe in welcher die Linie gemalt wird */
	public Color getLineColor(); 
	/** Farbe in welcher die Punkte gemalt werden sollen */
	public Color getPointColor();
	/** die Farbe in welcher die hervorzuhebenden Punkte gemalt werden sollen */
	public Color getHilightColor();
	
	/**
	 * Die Methode gibt eine ArrayList mit den Indizes aller Samples aus, welche graphisch hervorgehoben werden sollen.
	 * 
	 * @return ArrayList mit den Indizes der Samples, welche grafisch hervorgehoben werden sollen.
	 */
	public HashSet<Integer> getIndexOfSamplesToHilight();
	
	/** Die Methode gibt aus, ob die komplette WAV-Datei in die View eingezeichnet bzw. ausgeblendet werden soll. */
	public boolean isShowWavFilePresentation();

	/** Die Methode gibt aus, ob die Peeks in die View eingezeichnet bzw. ausgeblendet werden soll. */
	public boolean isShowPeeksPresentation();
}
