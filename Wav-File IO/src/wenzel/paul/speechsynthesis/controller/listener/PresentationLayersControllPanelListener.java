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
	
}