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
	 * Die Methode setzt ein bestimmtes Sample als markiert.
	 * 
	 * @param indexOfClickedSample der Index vom Sample, welches markiert werden soll.
	 */
	public void addMarkedSample(int indexOfClickedSample);
}