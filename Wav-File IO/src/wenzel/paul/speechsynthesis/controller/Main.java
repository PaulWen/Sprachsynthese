package wenzel.paul.speechsynthesis.controller;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import wenzel.paul.speechsynthesis.controller.listener.ViewListener;
import wenzel.paul.speechsynthesis.model.Model;
import wenzel.paul.speechsynthesis.model.dataobjects.WavFileDataObject;
import wenzel.paul.speechsynthesis.quellen.WavFileException;
import wenzel.paul.speechsynthesis.util.wav.ReadWavFile;
import wenzel.paul.speechsynthesis.util.wav.WavFilePlayer;
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
	public static final float START_WINDOW_WIDTH_PER_FRAME = 1.0f;
	/** die minimale Anzahl an Pixel pro Frame einer WAV-Datei, welche das Fenster haben muss */
	public static final float MIN_WINDOW_WIDTH_PER_FRAME = 0.1f;
	/** die maximale Anzahl an Pixel pro Frame einer WAV-Datei, welche das Fenster haben darf */
	public static final float MAX_WINDOW_WIDTH_PER_FRAME = 10.0f;

	/** der Pfad, mit welchem der File-Chooser gestartet wird */
	private String WAV_FILE_PATH = "C:/Users/Wenze/Desktop/Java Workspace/Sprachsynthese/Wav-File IO/res";

/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	private Model model;
	private View view;
	
	private WavFilePlayer player;
	
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
	
	public void attachWavFile() {
		// FileChooser öffnen und eine WAV-Datei einlesen
		WavFileDataObject newWavFile = null;
		try {
			newWavFile = ReadWavFile.openWavFile(startFileChooser(false));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WavFileException e) {
			e.printStackTrace();
		}
		
		// neue Samples anhängen an die aktuelle Datei
		double[][] oldWavFileFalues = model.getWavFile().getWavFileValues();
		double[][] newWavFileFalues = new double[Math.max(oldWavFileFalues.length, newWavFile.getWavFileValues().length)][oldWavFileFalues[0].length + newWavFile.getWavFileValues()[0].length];
		
		// alte Werte in den neuen Array schreiben
		for (int channelNumber = 0; channelNumber < oldWavFileFalues.length; channelNumber++) {
			for (int sampleNumber = 0; sampleNumber < oldWavFileFalues[0].length; sampleNumber++) {
				newWavFileFalues[channelNumber][sampleNumber] = oldWavFileFalues[channelNumber][sampleNumber];
			}
		}

		// neue Werte in den neuen Array schreiben
		int sampleNumberOffset = oldWavFileFalues[0].length;
		for (int channelNumber = 0; channelNumber < newWavFile.getWavFileValues().length; channelNumber++) {
			for (int sampleNumber = 0; sampleNumber < newWavFile.getWavFileValues()[0].length; sampleNumber++) {
				newWavFileFalues[channelNumber][sampleNumberOffset + sampleNumber] = newWavFile.getWavFileValues()[channelNumber][sampleNumber];
			}
		}
		
		model.getWavFile().setWavFileValues(newWavFileFalues);
		
		// View neu zeichnen
		view.repaint();
	}
	
	public void deleteMarkedSamples() {
		double[][] oldWavFileValues = model.getWavFile().getWavFileValues();
		double[][] newWavFileValues = new double[oldWavFileValues.length][oldWavFileValues[0].length - model.getIndexOfSamplesToHilight().size()];
		
		for (int channelNumber = 0; channelNumber < oldWavFileValues.length; channelNumber++) {
			int newWavFileValuesIndex = 0;
			for (int oldWavFileValuesIndex = 0; oldWavFileValuesIndex < oldWavFileValues[0].length; oldWavFileValuesIndex++) {
				// wenn das Sample nicht markiert ist es in die neue Liste mit aufnehmen
				if (!model.getIndexOfSamplesToHilight().contains(oldWavFileValuesIndex)) {
					newWavFileValues[channelNumber][newWavFileValuesIndex++] = oldWavFileValues[channelNumber][oldWavFileValuesIndex];
				}
			}
		}
		
		// die markierten Samples zurücksetzen
		model.setIndexOfSamplesToHilight(new HashSet<Integer>()); 
		// die neuen Sample-Werte übergeben 
		model.getWavFile().setWavFileValues(newWavFileValues);
		
		// die View neu zeichnen
		view.repaint();
	}
	
	public void zoom(float zoomValue) {
		model.setMinWidth((int)Math.ceil(model.getWavFile().getNumberOfFrames() * zoomValue));
		model.setCurrentZoomLevel(zoomValue);
		view.repaint();
	}
	
	
	public void startPausePlayback() {
		if (player.getState() == WavFilePlayer.PLAYS) {
			player.pause();
		} else if (player.getState() == WavFilePlayer.PAUSED) {
			player.play();
		}
	}
	
	public void stopPlayback() {
		player.stop();
	}
	
	public void addMarkedSample(int indexOfClickedSample) {
		if (!model.getIndexOfSamplesToHilight().contains(indexOfClickedSample)) {
			model.getIndexOfSamplesToHilight().add(indexOfClickedSample);
		} else {
			model.getIndexOfSamplesToHilight().remove(indexOfClickedSample);
		}
		view.repaint();
	}

	public void resetSelectedSamples() {
		model.setIndexOfSamplesToHilight(new HashSet<Integer>()); 
		view.repaint();
	}
	
	public void loopPlayback(boolean loopPlayback) {
		model.setLoopPlayback(loopPlayback);
	}
	
	public void analyseDurationBetweenPeeks() {
		// die Variable gibt an, ob die derzeit betrachteten Punkte eine steigende Linie bilden oder eine fallende
		boolean rising = true;
		// der Index vom letzten Sample, welcher eine Spitze darstellt
		int sampleNumberOfLastPeek = 0;
		
		for (int sampleNumber = 1; sampleNumber < model.getWavFile().getWavFileValues()[0].length; sampleNumber++) {
			if (rising) {
				// gucken, ob mittlerweile ein fallender Punkt erreicht wurde
				if (model.getWavFile().getWavFileValues()[0][sampleNumber] < model.getWavFile().getWavFileValues()[0][sampleNumber - 1]) {
					// den zeitlichen Abstand zwischen dem und dem letzten Peek bestimmen
					System.out.println("Rise: " + (double)((sampleNumber - 1) - sampleNumberOfLastPeek) / model.getWavFile().getSampleRate() * 1000);
					
					// Variablen updaten
					rising = false;
					sampleNumberOfLastPeek = sampleNumber - 1;
				}
			} else {
				// gucken, ob mittlerweile ein steigender Punkt erreicht wurde
				if (model.getWavFile().getWavFileValues()[0][sampleNumber] > model.getWavFile().getWavFileValues()[0][sampleNumber - 1]) {
					// den zeitlichen Abstand zwischen dem und dem letzten Peek bestimmen
					System.out.println("Fall: " + (double)((sampleNumber - 1) - sampleNumberOfLastPeek) / model.getWavFile().getSampleRate() * 1000);
					
					// Variablen updaten
					rising = true;
					sampleNumberOfLastPeek = sampleNumber - 1;
				}
			}
		}
	}
	
	public void showWavFilePresentation(boolean show) {
		model.setShowWavFilePresentation(show);
		view.repaint();
	}
	
	public void showPeeksPresentation(boolean show) {
		model.setShowPeeksPresentation(show);
		view.repaint();
	}
	
	public void showPolygonsOfPeeksPresentation(boolean show) {
		model.setShowPolygonsOfPeeksPresentation(show);
		view.repaint();
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
		
		player = new WavFilePlayer(model);
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
			model = new Model(wavFile, (int)Math.ceil(wavFile.getNumberOfFrames() * Main.START_WINDOW_WIDTH_PER_FRAME), 500, 4, Color.white, new Color(255, 255, 255, 128), Color.green, Color.black, Color.red, Main.START_WINDOW_WIDTH_PER_FRAME);
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