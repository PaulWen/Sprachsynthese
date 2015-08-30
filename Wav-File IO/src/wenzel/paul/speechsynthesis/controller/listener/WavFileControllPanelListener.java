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
	 * Die Methode passt die Präsentation der WAV-Datei an das gewünschte Zoom-Level an.
	 * 
	 * Zoomlevel =  die Anzahl an Pixel pro Frame einer Datei, welche das Fenster breit sein soll
	 * 
	 * @param zoomValue das Zoomlevel
	 */
	public void zoom(float zoomValue);
	
}