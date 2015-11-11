package wenzel.paul.speechsynthesis.controller.listener;

/**
 * Das Interface {@link PresentationLayersControllPanelListener} [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public interface PresentationLayersControllPanelListener {
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	
	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	/**
	 * Die Methode ermöglicht es die komplette WAV-Datei in die View einzuzeichnen bzw. auszublenden.
	 * 
	 * @param show true = die komplette WAV-Datei sollen eingezeichnet werden
	 * 				false = die komplette WAV-Datei sollen nicht eingezeichnet werden
	 */
	public void showWavFilePresentation(boolean show);
	
	/**
	 * Die Methode ermöglicht es die Peeks in die View einzuzeichnen bzw. auszublenden.
	 * 
	 * @param show true = die Peeks sollen hervorgehoben/eingezeichnet werden
	 * 				false = die Peeks sollen nicht hervorgehoben/eingezeichnet werden
	 */
	public void showPeeksPresentation(boolean show);

	/**
	 * Die Methode ermöglicht es die Polygone der Peeks in die View einzuzeichnen bzw. auszublenden.
	 * 
	 * @param show true = die Polygone der Peeks sollen hervorgehoben/eingezeichnet werden
	 * 				false = die Polygone der Peeks sollen nicht hervorgehoben/eingezeichnet werden
	 */
	public void showPolygonsOfPeeksPresentation(boolean show);
	
	/**
	 * Die Methode ermöglicht es eine komplette zweite WAV-Datei in die View einzuzeichnen bzw. auszublenden.
	 * 
	 * @param show true = eine komplette zweite WAV-Datei soll eingezeichnet werden
	 * 				false = eine komplette zweite WAV-Datei soll ausgeblendet werden
	 */
	public void showSecondWavFilePresentation(boolean show);

	/**
	 * Die Methode öffnet einen File-Chooser, über welchen eine zweite WAV-Datei ausgesucht werden kann,
	 * welche angezeigt werden soll.
	 */
	public void openSecondWavFile();

	/**
	 * Die Methode sorgt dafür, dass die zweite WAV-Datei als erste WAV-Datei gilt und die erste WAV-Datei als zweite WAV-Datei gilt.
	 */
	public void swapFirstAndSecondWavFile();

	/**
	 * Die Methode passt die Präsentation der WAV-Datei an das gewünschte Zoom-Level an.
	 * 
	 * Zoomlevel =  die Anzahl an Pixel pro Frame einer Datei, welche das Fenster breit sein soll
	 * 
	 * @param zoomValue das Zoomlevel
	 */
	public void zoom(float zoomValue);
	
}