package wenzel.paul.speechsynthesis.controller;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import wenzel.paul.speechsynthesis.controller.listener.ViewListener;
import wenzel.paul.speechsynthesis.model.Model;
import wenzel.paul.speechsynthesis.model.dataobjects.WavFileDataObject;
import wenzel.paul.speechsynthesis.quellen.WavFileException;
import wenzel.paul.speechsynthesis.util.wav.ReadWavFile;
import wenzel.paul.speechsynthesis.util.wav.WriteWavFile;
import wenzel.paul.speechsynthesis.view.View;

/**
 * Die Klasse "KlassenVorlage" [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public class Main implements ViewListener {
	
/////////////////////////////////////////////////Konstanten/////////////////////////////////////////////////
	
	/** die Anzahl an Pixel pro Frame einer WAV-Datei, welche das Fenster zu beginn breit sein soll */
	public static final float WINDOW_WIDTH_PER_FRAME = 1.0f;

	/** der Pfad, mit welchem der File-Chooser gestartet wird */
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
	
	public void saveWavFile() {
		WriteWavFile.writeWavFile(model.getWavFile(), startFileChooser(true));
	}
	
	public void zoom(float zoomValue) {
		model.setMinWidth((int)Math.ceil(model.getWavFile().getNumberOfFrames() * zoomValue));
		view.repaint();
	}
	
	
	public void startPausePlayback() {
			
	}
	
	public void stopPlayback() {
			
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
		view.repaint();
	}
	
	/**
	 * Die Methode öffnet einen FileChooser über welchen eine .WAV-Datei ausgewählt werden kann.
	 * Die Ausgewählte .WAV-Datei wird anschließend in ein {@link Model} geladen.
	 * 
	 * @return	das {@link Model} mit den Daten der gewünschten .WAV-Datei <br>
	 * 		  	falls null zurückgegeben wird, so gab es einen Fehler!
	 */
	private Model initNewModel() {
		Model model = null;
		
		// WAV-Datei öffnen
		try {
			WavFileDataObject wavFile = ReadWavFile.openWavFile(startFileChooser(false));
			
			// neues Model anlegen
			model = new Model(wavFile, (int)Math.ceil(wavFile.getNumberOfFrames() * WINDOW_WIDTH_PER_FRAME), 500, 4, Color.white, Color.green, Color.black);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WavFileException e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	public static void main(String[] args) {
		new Main();
	}
	
	/**
	 * Die Methode öffnet einen FileChosser um entweder einen Speicherort für eine Datei wählen zu lassen,
	 * oder eine Datei auszuwählen welche geöffnet werden soll.
	 * 
	 * @param save true = es soll ein Speicherort gewählt werden <br>
	 * 				false = es soll eine zu öffnende Datei ausgewählt werden
	 * 
	 * @return die gewünschte Datei oder der gewünschte Speicherort
	 */
	private File startFileChooser(boolean save) {
		String selectedFilePath = null;
		
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
		
		int status;
		// gucken ob ein Öffne- oder Speicher-Dialog gestartet werden soll
		if (save) {
			status = fileChooser.showSaveDialog(view);
		} else {
			status = fileChooser.showOpenDialog(view);
		}
		
		//wenn eine Datei ausgewählt wurde und es kein Problem gab
		if (status == JFileChooser.APPROVE_OPTION) {
			//wenn die Datei auf .wav oder .WAV endet (=gültig)
			if (fileChooser.getSelectedFile().getName().endsWith(".wav") ||
					fileChooser.getSelectedFile().getName().endsWith(".WAV")) {
				
				selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
		
			} else { // wenn die Datei keine WAV-Datei ist (=ungültig)
				//Fehlermeldung
				JOptionPane.showMessageDialog(view, "Die Datei muss eine WAV-Datei sein!", "Falscher Dateityp!",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		
		
		// ausgewählte Datei zurückgeben oder null, falls ein Fehler aufgetreten ist
		if (selectedFilePath != null) {
			return new File(selectedFilePath);
		} else {
			return null;
		}
	}
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
}