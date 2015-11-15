package wenzel.paul.speechsynthesis.controller.listener;

/**
 * Das Interface {@link AnalysisControllPanelListener} [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public interface AnalysisControllPanelListener {
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	
	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	/**
	 * Die Methode analysiert die zeitlichen Abstände zwischen Peeks in Millisekunden.
	 */
	public void analyseDurationBetweenPeeks();

	/**
	 * Die Methode öffnet einen FileChooser, welcher es ermöglicht eine WAV-Datei auszuwählen.
	 * Anschließend wird geguckt, wie häufig das Muster der der auswählten WAV-Datei in der aktuellen WAV-Datei vorkommt. 
	 */
	public void searchSoundPattern();

	/**
	 * Die Methode sorgt dafür, dass automatisiert SoundPatterns gesucht werden.
	 */
	public void findSoundPatterns();
	
}