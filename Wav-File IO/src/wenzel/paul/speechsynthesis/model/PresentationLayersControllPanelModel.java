package wenzel.paul.speechsynthesis.model;

public interface PresentationLayersControllPanelModel {

	/** Die Methode gibt aus, ob die komplette WAV-Datei in die View eingezeichnet bzw. ausgeblendet werden soll. */
	public boolean isShowWavFilePresentation();

	/** Die Methode gibt aus, ob die Peeks in die View eingezeichnet bzw. ausgeblendet werden soll. */
	public boolean isShowPeeksPresentation();

	/** Die Methode gibt aus, ob die Polygone von den Peeks in die View eingezeichnet bzw. ausgeblendet werden sollen. */
	public boolean isShowPolygonsOfPeeksPresentation();
	
	/** Die Methode gibt das aktuelle Zoom-Level als Float aus. */
	public float getCurrentZoomLevel();
	
}
