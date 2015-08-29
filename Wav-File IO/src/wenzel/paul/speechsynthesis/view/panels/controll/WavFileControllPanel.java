package wenzel.paul.speechsynthesis.view.panels.controll;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import wenzel.paul.speechsynthesis.controller.listener.WavFileControllPanelListener;

/**
 * Die Klasse {@link WavFileControllPanel} [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public class WavFileControllPanel extends JPanel {
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	private JButton loadWavFileButton;
	
	private JLabel zoomLabel;
	private JSpinner zoomSchalter;
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link WavFileControllPanel}. 
	 */
	public WavFileControllPanel(final WavFileControllPanelListener listener) {
		//Datenfelder initialisieren
	
		
			//Buttons Konfigurieren
		loadWavFileButton = new JButton("lade WAV-Datei");
		loadWavFileButton.setActionCommand("ladeLabyrinthButton");
		loadWavFileButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				listener.openWavFile();
			}
		});
		
			//Zoom Konfigurieren
//		JPanel zoomPanel = new JPanel(new FlowLayout());
//		zoomLabel = new JLabel("Zoom von 1 - 1000 Pixel^2 pro Rasterquadrat: ");
//		zoomPanel.add(zoomLabel);
//		zoomSchalter = new JSpinner(new SpinnerNumberModel(7, 1, 1000, 1));
//		zoomSchalter.addChangeListener(this);
//		zoomPanel.add(zoomSchalter);
//		settingsPanel.add(zoomPanel);
		
			//JPanel Konfigurieren
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
			//Komponenten Hinzufügen
		add(loadWavFileButton);
	}
	
	
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	
	
	
	
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////
	
//	public void stateChanged(ChangeEvent e) {
//	//wenn sich der Wert des ZoomSchalter geändert hat
//	if (e.getSource() == zoomSchalter) {
//		Dimension aktuelleFramegroesse = getSize();
//		labyrinth.zoom((Integer)zoomSchalter.getValue(), (Integer)zoomSchalter.getValue()); // Die Rasterquadratgröße im Labyrinth
//																							// entsprechend der Nutzereingabe setzen.
//		frameNeuZeichnen(aktuelleFramegroesse);
//	}
//	
//}
	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	
	
	
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}
