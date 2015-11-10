package wenzel.paul.speechsynthesis.view.panels.controll;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import wenzel.paul.speechsynthesis.controller.listener.AnalysisControllPanelListener;

/**
 * Die Klasse {@link AnalysisControllPanel} [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public class AnalysisControllPanel extends JPanel {
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	private JButton durationBetweenPeeksButton;
	private JButton searchSoundPatternButton;
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link AnalysisControllPanel}. 
	 */
	public AnalysisControllPanel(final AnalysisControllPanelListener listener) {
		//Datenfelder initialisieren
			//Buttons Konfigurieren
		durationBetweenPeeksButton = new JButton("Abstand zwischen Peeks");
		durationBetweenPeeksButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				listener.analyseDurationBetweenPeeks();
			}
		});

		searchSoundPatternButton = new JButton("Sound-Muster suchen");
		searchSoundPatternButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				listener.searchSoundPattern();
			}
		});
		
			//JPanel Konfigurieren
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
			//Komponenten Hinzufügen
		add(durationBetweenPeeksButton);
		add(searchSoundPatternButton);
	}
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	
	
	
	
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////

	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	
	
	
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}
