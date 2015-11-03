package wenzel.paul.speechsynthesis.model;

public interface PresentationLayersControllPanelModel {

	/** Die Methode gibt aus, ob die komplette WAV-Datei in die View eingezeichnet bzw. ausgeblendet werden soll. */
	public boolean isShowWavFilePresentation();

	/** Die Methode gibt aus, ob die Peeks in die View eingezeichnet bzw. ausgeblendet werden soll. */
	public boolean isShowPeeksPresentation();
	
}