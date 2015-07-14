package wenzel.paul.speechsynthesis.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;

import vorlagen.Main.OpenWavFileListener;
import wenzel.paul.speechsynthesis.model.ViewModel;

public class View extends JFrame {
	
/////////////////////////////////////////////Datenfelder deklarieren////////////////////////////////////////////
	
	private ViewModel model;
	
	private JLabel zoomLabel;
	private JSpinner zoomSchalter;
	private JButton loadWavFileButton;
	private JFileChooser fileManager;
	
	private JScrollPane scrollPane;
	private DrawWavPanel drawPanel;

/////////////////////////////////////////////Konstruktor///////////////////////////////////////////////////////

	/**
	 * Konstruktor der Klasse View
	 */
	public View(ViewModel model, OpenWavFileListener openWavFileListener) {
		this.model = model;
		
		
		//Panels Konfigurieren
		drawPanel = new DrawWavPanel(model);
		scrollPane = new JScrollPane(drawPanel);
				
		JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));		
		
		//Zoom Konfigurieren
//		JPanel zoomPanel = new JPanel(new FlowLayout());
//		zoomLabel = new JLabel("Zoom von 1 - 1000 Pixel^2 pro Rasterquadrat: ");
//		zoomPanel.add(zoomLabel);
//		zoomSchalter = new JSpinner(new SpinnerNumberModel(7, 1, 1000, 1));
//		zoomSchalter.addChangeListener(this);
//		zoomPanel.add(zoomSchalter);
//		settingsPanel.add(zoomPanel);
		
		//Buttons Konfigurieren
		loadWavFileButton = new JButton("lade WAV-Datei");
		loadWavFileButton.setActionCommand("ladeLabyrinthButton");
		loadWavFileButton.addActionListener(openWavFileListener);
		settingsPanel.add(loadWavFileButton);
		
		
		//JFrame Konfigurieren
		setLayout(new BorderLayout());
		setTitle("WAV-File Analyser");
		setSize(500, 500);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
			//Komponenten Hinzufügen
		add(scrollPane, BorderLayout.CENTER);
		add(settingsPanel, BorderLayout.SOUTH);
						
		setVisible(true);
		repaint();
	}
	
/////////////////////////////////////////////geerbte Methoden/////////////////////////////////////////////////
	
//	public void stateChanged(ChangeEvent e) {
//		//wenn sich der Wert des ZoomSchalter geändert hat
//		if (e.getSource() == zoomSchalter) {
//			Dimension aktuelleFramegroesse = getSize();
//			labyrinth.zoom((Integer)zoomSchalter.getValue(), (Integer)zoomSchalter.getValue()); // Die Rasterquadratgröße im Labyrinth
//																								// entsprechend der Nutzereingabe setzen.
//			frameNeuZeichnen(aktuelleFramegroesse);
//		}
//		
//	}
	
/////////////////////////////////////////////Methoden////////////////////////////////////////////////////////

	
}
