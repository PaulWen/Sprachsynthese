package wenzel.paul.speechsynthesis.controller.listener;

/**
 * Das Interface {@link PlaybackControllPanelListener} [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public interface PlaybackControllPanelListener {
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	
	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	/**
	 * Die Methode startet die Wiedergabe, falls diese gerade nicht läuft.
	 * Falls die Wiedergabe bereits läuft, so stoppt die Methode die Wiedergabe.
	 */
	public void startPausePlayback();
	
	/**
	 * Die Methode beendet die Wiedergabe und geht zum Anfang der Datei. 
	 * Beim nächsten Startetn wird die Wiedergabe von anfang an begonnen.
	 */
	public void stopPlayback();
	
}