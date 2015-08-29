package wenzel.paul.speechsynthesis.controller;

import java.awt.Color;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import wenzel.paul.speechsynthesis.controller.listener.ViewListener;
import wenzel.paul.speechsynthesis.model.Model;
import wenzel.paul.speechsynthesis.view.View;
import wenzel.paul.speechsynthesis.wav.WavFile;

/**
 * Die Klasse "KlassenVorlage" [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public class Main implements ViewListener {
	
/////////////////////////////////////////////////Konstanten/////////////////////////////////////////////////
	/** die Anzahl an Pixel pro Frame einer Datei, welche das Fenster breit sein soll */
	private final float WINDOW_WIDTH_PER_FRAME = 1.0f;

	private String WAV_FILE_PATH = "C:/Users/Wenze/Desktop/Java Workspace/Sprachsynthese/Wav-File IO/res";

/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	private Model model;
	private View view;
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse "Main". 
	 */
	public Main() {
		init();
	}
	
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	
	
	
	
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////
	
	public void openWavFile() {
		init();
	}

//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	private void init() {
		// ein bisheriges Fenster schließen
		if (view != null) {
			view.dispose();
		}
		
		//Datenfelder initialisieren
		model = initNewModel();
		view = new View(model, this);
	}
	
	/**
	 * Die Methode �ffnet einen FileChooser �ber welchen eine .WAV-Datei ausgew�hlt werden kann.
	 * Die Ausgew�hlte .WAV-Datei wird anschlie�end in ein {@link Model} geladen.
	 * 
	 * @return	das {@link Model} mit den Daten der gew�nschten .WAV-Datei <br>
	 * 		  	falls null zur�ckgegeben wird, so gab es einen Fehler!
	 */
	private Model initNewModel() {
		Model model = null;
		
		//FileChooser Konfigurieren
		JFileChooser fileChooser = new JFileChooser(WAV_FILE_PATH);
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "*., *.wav, *.WAV";
			}
			@Override
			public boolean accept(File file) {
				String filename = file.getName();
				//Nur wenn es ein Ordner, eine .wav-Datei oder eine.WAV-Datei ist zeige sie an
				if (file.isDirectory() || filename.endsWith(".wav") || filename.endsWith(".WAV")) {
					return true;
				} else {
					return false;
				}
			}
		});
		
		int status = fileChooser.showOpenDialog(view);
		
		//wenn eine Datei ausgew�hlt wurde und es kein Problem gab
		if (status == JFileChooser.APPROVE_OPTION) {
			//wenn die Datei auf .wav oder .WAV endet (=g�ltig)
			if (fileChooser.getSelectedFile().getName().endsWith(".wav") ||
					fileChooser.getSelectedFile().getName().endsWith(".WAV")) {
				
				String wavFilePath = fileChooser.getSelectedFile().getAbsolutePath();
				
				// WAV-Datei �ffnen
				try {
					System.out.println("///////////////NEUE DATEI �FFNEN///////////////");
					// Open the wav file specified as the first argument
					WavFile wavFile = WavFile.openWavFile(new File(fileChooser.getSelectedFile().getAbsolutePath()));
					
					// Display information about the wav file
					wavFile.display();
					
					// Get the number of audio channels in the wav file
					int numChannels = wavFile.getNumChannels();
					
					// Create a buffer of 100 frames
					double[] buffer = new double[100 * numChannels];
					
					int framesRead;
					double min = Double.MAX_VALUE;
					double max = Double.MIN_VALUE;
					
					double[] wavFileValues = new double[(int)wavFile.getNumFrames()];
					int i = 0;
					do {
						// Read frames into buffer
						framesRead = wavFile.readFrames(buffer, 100);
						
						// Loop through frames and look for minimum and maximum value
						for (int s = 0; s < framesRead * numChannels ; s++) {
							wavFileValues[i++] = buffer[s];
							if (buffer[s] > max) max = buffer[s];
							if (buffer[s] < min) min = buffer[s];
						}
					} while (framesRead != 0);
					
					// Close the wavFile
					wavFile.close();
					
					// Output the minimum and maximum value
					System.out.printf("Min: %f, Max: %f\n", min, max);
					
					
					
					// neues Model anlegen
					model = new Model(wavFileValues, (int)Math.ceil(wavFile.getNumFrames() * WINDOW_WIDTH_PER_FRAME), 500, 4, Color.white, Color.green, Color.black);
					model.setWavFileValues(wavFileValues);
				}
				catch (Exception exception) {
					exception.printStackTrace();
				}
				
			} else { // wenn die Datei keine TXT-Datei ist (=ung�ltig)
				//Fehlermeldung
				JOptionPane.showMessageDialog(view, "Die Datei muss eine WAV-Datei sein!", "Falscher Dateityp!",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		
		return model;
	}
	
	public static void main(String[] args) {
		new Main();
	}
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
}