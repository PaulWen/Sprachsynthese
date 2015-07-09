import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

public class View extends JFrame implements ActionListener, ChangeListener {
	
/////////////////////////////////////////////Datenfelder deklarieren////////////////////////////////////////////
	
	private Image icon;
	private Icon startBild;
	
	private double[] values;
	
	private JLabel zoomLabel;
	private JSpinner zoomSchalter;
	private JButton loadWavFileButton;
	private JFileChooser dateiManager;
	
	private JScrollPane scrollPane;
	private JPanel labyrinthPanel;
	
	private String labyrinthDateiPfad;
	
	private DrawPanel zeichenflaeche;
	
	
	
/////////////////////////////////////////////Konstruktor///////////////////////////////////////////////////////

	/**
	 * Konstruktor der Klasse View
	 */
	public View() {
		//Bilder Konfigurieren
		icon =  new ImageIcon("roboter.png").getImage();
		startBild = new ImageIcon("labyrinth.jpg");
		
		//Panels Konfigurieren
		labyrinthPanel = new JPanel(); // hier kommt das Labyrinth rein
		labyrinthPanel.setBackground(Color.GRAY);
		scrollPane = new JScrollPane(labyrinthPanel); // Hier kommt das "labyrinthPanel" rein,
													  // damit man es wenn nötig scrollen kann.
		JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 5));
		
		
		//Labyrinth Konfigurieren
		
		values = new double[500];
		int numValues = 0;
		try
		{
			// Open the wav file specified as the first argument
			WavFile wavFile = WavFile.openWavFile(new File("Test.wav"));

			// Display information about the wav file
			wavFile.display();

			// Get the number of audio channels in the wav file
			int numChannels = wavFile.getNumChannels();

			// Create a buffer of 100 frames
			double[] buffer = new double[100 * numChannels];

			int framesRead;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;

			do
			{
				// Read frames into buffer
				framesRead = wavFile.readFrames(buffer, 100);

				// Loop through frames and look for minimum and maximum value
				for (int s=0 ; s<framesRead * numChannels ; s += 2)
				{
					values[numValues++] = buffer[s];
					if (buffer[s] > max) max = buffer[s];
					if (buffer[s] < min) min = buffer[s];
				}
			}
			while (framesRead != 0);

			// Close the wavFile
			wavFile.close();

			// Output the minimum and maximum value
			System.out.printf("Min: %f, Max: %f\n", min, max);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("hi");
		}
		
		
		//Zoom Konfigurieren
		JPanel zoomPanel = new JPanel(new FlowLayout());
		zoomLabel = new JLabel("Zoom von 1 - 1000 Pixel^2 pro Rasterquadrat: ");
		zoomPanel.add(zoomLabel);
		zoomSchalter = new JSpinner(new SpinnerNumberModel(7, 1, 1000, 1));
		zoomSchalter.addChangeListener(this);
		zoomPanel.add(zoomSchalter);
		optionsPanel.add(zoomPanel);
		
		//Buttons Konfigurieren
		loadWavFileButton = new JButton("lade WAV-Datei");
		loadWavFileButton.setActionCommand("ladeLabyrinthButton");
		loadWavFileButton.addActionListener(this);
		optionsPanel.add(loadWavFileButton);
		
		//FileChooser Konfigurieren
		dateiManager = new JFileChooser();
		dateiManager.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "*., *.txt, *.TXT";
			}
			@Override
			public boolean accept(File file) {
				String filename = file.getName();
				//Nur wenn es ein Ordner, eine .txt-Datei oder eine.TXT-Datei ist zeige sie an
				if (file.isDirectory() || filename.endsWith(".txt") || filename.endsWith(".TXT")) {
					return true;
				} else {
					return false;
				}
			}
		});
		
		//Strings Konfigurieren
		labyrinthDateiPfad = "";
		
		zeichenflaeche = new DrawPanel(Color.green, labyrinthPanel.getSize().width, labyrinthPanel.getSize().height, 4, 4, values);
		
		labyrinthPanel.add(zeichenflaeche);
		
		//JFrame Konfigurieren
		setIconImage(icon);
		setLayout(new BorderLayout());
		setTitle("Turn90");
		setSize(625, 700);
		setMinimumSize(new Dimension(625, 500));
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
			//Komponenten Hinzufügen
		add(scrollPane, BorderLayout.CENTER);
		add(optionsPanel, BorderLayout.SOUTH);
						
		setVisible(true);
		
	}
	
/////////////////////////////////////////////geerbte Methoden/////////////////////////////////////////////////
	
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		
		if (actionCommand.equals("ladeLabyrinthButton")) {
			//Datei Manager öffnen und Statusmeldung abspeichern
			int status = dateiManager.showOpenDialog(this);
			//wenn eine Datei ausgewählt wurde und es kein Problem gab
			if (status == JFileChooser.APPROVE_OPTION) {
				//wenn die Datei auf .txt oder .TXT endet (=gültig)
				if (dateiManager.getSelectedFile().getName().endsWith(".txt") ||
					dateiManager.getSelectedFile().getName().endsWith(".TXT")) {
					
					labyrinthDateiPfad = dateiManager.getSelectedFile().getAbsolutePath();
					
//					//neue Labyrinth Datei Laden
//					Dimension aktuelleFramegroesse = getSize();
//					labyrinth.leseNeuesLabyrinthEin((Integer)zoomSchalter.getValue(),
//													(Integer)zoomSchalter.getValue(), labyrinthDateiPfad);
//					labyrinthPanel.removeAll();
//					labyrinthPanel.add(labyrinth);
//					frameNeuZeichnen(aktuelleFramegroesse);
					
				} else { // wenn die Datei keine TXT-Datei ist (=ungültig)
					//Fehlermeldung
					JOptionPane.showMessageDialog(this, "Die Datei muss eine TXT-Datei sein!", "Falscher Dateityp!",
												  JOptionPane.ERROR_MESSAGE);
				}
			}
		} 
	}

	
	public void stateChanged(ChangeEvent e) {
		//wenn sich der Wert des ZoomSchalter geändert hat
		if (e.getSource() == zoomSchalter) {
			Dimension aktuelleFramegroesse = getSize();
//			labyrinth.zoom((Integer)zoomSchalter.getValue(), (Integer)zoomSchalter.getValue()); // Die Rasterquadratgröße im Labyrinth
																								// entsprechend der Nutzereingabe setzen.
			frameNeuZeichnen(aktuelleFramegroesse);
		}
		
	}
	
/////////////////////////////////////////////Methoden////////////////////////////////////////////////////////

	/**
	 * Methode um das JFrame neu zu zeichnen, z.B. nötig wenn man ein neues Labyrinth eingelesen hat
	 * 
	 * @param aktuelleFramegroesse => die Größe, welche das JFrame bekommen soll
	 */
	private void frameNeuZeichnen(Dimension aktuelleFramegroesse) {
		remove(scrollPane);
		add(scrollPane, BorderLayout.CENTER);
		
		repaint();
		pack();
		setSize(aktuelleFramegroesse);
	}
	
	
	
	
	public static void main(String[] args) {
		new View(); // Zum Starten wird nur der Konstruktor der Klasse View aufgerufen,
						  // welcher dann ein Fenster(JFrame) erstellt und öffnet.
	}

}
