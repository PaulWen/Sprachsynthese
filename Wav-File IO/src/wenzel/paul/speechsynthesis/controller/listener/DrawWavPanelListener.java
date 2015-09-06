package wenzel.paul.speechsynthesis.controller.listener;

/**
 * Das Interface {@link DrawWavPanelListener} [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public interface DrawWavPanelListener {
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	
	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	/**
	 * Wenn ein Punkt im Panel angeklickt wird, welcher ein Sample repräsentiert, so wird diese Methode aufgerufen.
	 * 
	 * @param indexOfClickedSample der Index vom Sample, welches durch den angeklickten Punkt repräsentiert wird
	 */
	public void sampleClicked(int indexOfClickedSample);
}