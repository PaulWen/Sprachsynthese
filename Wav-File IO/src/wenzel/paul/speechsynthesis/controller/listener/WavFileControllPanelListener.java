package wenzel.paul.speechsynthesis.controller.listener;

/**
 * Das Interface {@link WavFileControllPanelListener} [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public interface WavFileControllPanelListener {
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	
	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	/**
	 * Die Methode sorgt dafür, dass eine neue WAV-Datei ausgewählt werden kann und diese dann geöffnet wird.
	 */
	public void openWavFile();
	
	/**
	 * Die Methode sorgt dafür, dass die geöffnete WAV-Datei an einem zu wählenden Speicherort gespeichert wird.
	 */
	public void saveWavFile();
	
	/**
	 * Die Metode dient dem anfügen einer WAV-Datei an die bereits geöffnete WAV-Datei. 
	 */
	public void attachWavFile();
	
	/**
	 * Die Methode löscht alle derzeit markierten Samples aus der Datei.
	 */
	public void deleteMarkedSamples();
	
	/**
	 * Die Methode passt die Präsentation der WAV-Datei an das gewünschte Zoom-Level an.
	 * 
	 * Zoomlevel =  die Anzahl an Pixel pro Frame einer Datei, welche das Fenster breit sein soll
	 * 
	 * @param zoomValue das Zoomlevel
	 */
	public void zoom(float zoomValue);
	
}