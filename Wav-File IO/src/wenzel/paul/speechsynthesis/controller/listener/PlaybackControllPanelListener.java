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
	 * Beim nächsten Starten wird die Wiedergabe von Anfang an begonnen.
	 */
	public void stopPlayback();
	
	/**
	 * Die Methode setzt die Auswahl an Samples zurück, so das keine Samples mehr ausgewählt sind.
	 */
	public void resetSelectedSamples();
	
	/**
	 * Über die Methode kann angegeben werden, ob die Wiedergabe in einer Dauerschleife immer wieder
	 * abgespielt werden soll  oder nach einem mal abspielen pausieren soll. 
	 * 
	 * @param loopPlayback true = die Wiedergabe soll geloopt werden <br>
	 * 						false = die Wiedergabe soll nicht geloopt werden
	 */
	public void loopPlayback(boolean loopPlayback);
	
}