package wenzel.paul.speechsynthesis.view.panels.controll;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import wenzel.paul.speechsynthesis.controller.listener.PlaybackControllPanelListener;

/**
 * Die Klasse {@link PlaybackControllPanel} [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public class PlaybackControllPanel extends JPanel {
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	private JButton startPauseButton;
	private JButton stopButton;
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link PlaybackControllPanel}. 
	 */
	public PlaybackControllPanel(final PlaybackControllPanelListener listener) {
		//Datenfelder initialisieren
	
		
			//Buttons Konfigurieren
		startPauseButton = new JButton("Start/Pause");
		startPauseButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				listener.startPausePlayback();
			}
		});
		
		stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				listener.stopPlayback();
			}
		});
		
		
			//JPanel Konfigurieren
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
			//Komponenten Hinzufügen
		add(startPauseButton);
		add(stopButton);
	}
		
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	
	
	
	
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////
	

	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	
	
	
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}
