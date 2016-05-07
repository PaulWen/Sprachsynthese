package wenzel.paul.speechsynthesis.controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import wenzel.paul.speechsynthesis.controller.listener.ViewListener;
import wenzel.paul.speechsynthesis.model.Model;
import wenzel.paul.speechsynthesis.model.dataobjects.PatternInWavFileDataObject;
import wenzel.paul.speechsynthesis.model.dataobjects.PatternInWavFileDataObject2;
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
	private String WAV_FILE_PATH = "C:/Users/Wenze/Documents/Programmieren/Java/Java Workspace/Sprachsynthese/Wav-File IO/res";

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
	
	public void keepSamplesInInterval(int firstSample, int lastSample) {
		// nur die gewünschten Samples behalten
		keepSamplesInInterval(model.getWavFile(), firstSample, lastSample);
		
		// die markierten Samples zurücksetzen
		model.setIndexOfSamplesToHilight(new HashSet<Integer>()); 
		
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
	
	public void showSecondWavFilePresentation(boolean show) {
		model.setShowSecondWavFilePresentation(show);
		view.repaint();
	}
	
	public void openSecondWavFile() {
		// FileChooser öffnen und eine zweite WAV-Datei einzulesen
		WavFileDataObject secondWavFile = null;
		try {
			secondWavFile = ReadWavFile.openWavFile(startFileChooser(false));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WavFileException e) {
			e.printStackTrace();
		}
		
		// Model updaten
		model.setSecondWavFile(secondWavFile);
	}
	
	public void swapFirstAndSecondWavFile() {
		WavFileDataObject temp = model.getSecondWavFile();
		model.setSecondWavFile(model.getWavFile());
		model.setWavFile(temp);
		
		// View neu laden
		view.repaint();
	}
	
	public void searchSoundPattern() {
		// WAV-Datei, welche das gesuchte Muster ist, öffnen
		// FileChooser öffnen und eine WAV-Datei einlesen
		WavFileDataObject soundPatternWavFile = null;
		try {
			soundPatternWavFile = ReadWavFile.openWavFile(startFileChooser(false));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WavFileException e) {
			e.printStackTrace();
		}
		
		searchSoundPattern(model.getWavFile(), soundPatternWavFile, true);
	}
	
	public void findSoundPatterns() {
		// ob auf der Konsole Nachrichten ausgegeben werden sollen
		boolean messages = true;
		
		// ein Muster muss Mindestens 1 Sekunde / 100 lang sein
		int minPatternLength = (int)(model.getWavFile().getSampleRate() / 100);
		// ein Muster darf maximal 1 Sekunde * 5 lang sein
		int maxPatternLength = (int)(model.getWavFile().getSampleRate() * 5);
		
		// HashMap mit allen möglicherweise gefundenen Mustern
		// der Key ist: "<Index vom Start-Sample des gesuchten Musters>;<Länge vom gesuchten Muster>"
		HashMap<String, PatternInWavFileDataObject2> patternsFound = new HashMap<String, PatternInWavFileDataObject2>();
		
		/* Vorgehen:
		 * nach Mustern in der aktuellen WAV-Datei suchen, indem die größtmöglichen Muster gebildet werden und nach diesen gesucht wird
		 * bei der suche wird aber auch notiert, sobald ein kleineres Muster (mindestens so groß, wie die Mindestgröße) gefunden wurde
		 * auf diese Weise muss die gesamte Datei nur einmal abgesucht werden
		 */
		
		// Bedingungen: die Punkte werden normiert --> x * 100; y * 100 
		int normX = 100;
		int normY = 100;
		int tolerance = 10;
		
		
		
		// jedes Sample als Start-Sample eines Musters verwenden (wenn von dem Sample ausgehend ein Muster mit der Mindestlänge gebildet werden kann)
		// und nach dem Muster in der kompletten WAV-Datei suchen
		for (int patternStartSampleIndex = 0; patternStartSampleIndex < model.getWavFile().getNumberOfFrames() - minPatternLength; patternStartSampleIndex++) {
			System.out.println(patternStartSampleIndex + " von " + (model.getWavFile().getNumberOfFrames() - minPatternLength) + " durchsucht");
			
			
			// den Index vom letzten Sample des Musters bestimmen
			int patternEndSampleIndex;
				// wenn die maximale Musterlänge genommen werden kann, diese auch nehmen ...
			if (patternStartSampleIndex + maxPatternLength - 1 < model.getWavFile().getNumberOfFrames()) {
				patternEndSampleIndex = patternStartSampleIndex + maxPatternLength - 1;
			} else { // ... ansonsten als End-Sample das letzte Sample verwenden
				patternEndSampleIndex = model.getWavFile().getNumberOfFrames() - 1;
			}
			
			// das Pattern nach welchem gesucht werden soll in einem eigenen WavFileDataObject speichern
			WavFileDataObject soundPatternWavFile = new WavFileDataObject(model.getWavFile().getSampleRate(), model.getWavFile().getBlockAlign(), model.getWavFile().getValidBits(), model.getWavFile().getWavFileValues());			
			keepSamplesInInterval(soundPatternWavFile, patternStartSampleIndex, patternEndSampleIndex);

			
			
			// nach Mustern in der WAV-Datei suchen:
			
			// 1. Polygone vom Muster erstellen
			ArrayList<Polygon>	polygonsOfSoundPattern = new ArrayList<Polygon>();
			
			// die Polygone der Peeks erstellen
			for (int i = 0; i < soundPatternWavFile.getInicesOfPeeks().length - 1; i++) {
				int indexOfPeek = soundPatternWavFile.getInicesOfPeeks()[i];
				int indexOfNextPeek = soundPatternWavFile.getInicesOfPeeks()[i + 1];
				// Polygone um die Punkte berechnen
				polygonsOfSoundPattern.add(Main.calculatePolygonOfTwoPoints(indexOfPeek * normX, (int)(soundPatternWavFile.getWavFileValues()[0][indexOfPeek] * normY),
						indexOfNextPeek * normX, (int)(soundPatternWavFile.getWavFileValues()[0][indexOfNextPeek] * normY), tolerance));
			}
			
			//	// Polygone für alle Samples erstellen (= genauer aber schlechtere Performance als die obere Variante)
			//	for (int indexOfPeek = 0; indexOfPeek < soundPatternWavFile.getNumberOfFrames() - 1; indexOfPeek++) {
			//		// Polygone um die Punkte berechnen
			//		polygonsOfSoundPattern.add(Main.calculatePolygonOfTwoPoints(indexOfPeek * normX, (int)(soundPatternWavFile.getWavFileValues()[0][indexOfPeek] * normY),
			//				(indexOfPeek + 1) * normX, (int)(soundPatternWavFile.getWavFileValues()[0][indexOfPeek + 1] * normY), tolerance));
			//	}
			
			// 2. nach dem Muster in der aktuellen Datei suchen
			// von jedem Sample in der aktuellen WAV-Datei aus prüfen, ob die darauffolgenden Samples dem gesuchten Muster oder dem Anfang des gesuchten Musters entsprechen
			for (int i = 0; i < model.getWavFile().getNumberOfFrames() - soundPatternWavFile.getNumberOfFrames() + 1; i++) {
				double samplesInPolygonsCounter = 0;
				
				for (int j = 0; j < soundPatternWavFile.getNumberOfFrames(); j++) {
					boolean sampleInPolygons = false;
					
					// prüfen, ob es überhaupt noch möglich ist die gewünschte Quote (= Sample innerhalb der Polygone) zu erfüllen
					if ((samplesInPolygonsCounter + soundPatternWavFile.getNumberOfFrames() - j) / soundPatternWavFile.getNumberOfFrames() < 0.9) {
						// wenn die Quote nicht mehr erreicht werden kann, dann die Mustersuche an dieser Stelle beenden!
						break;
					}
					
					for (Polygon polygon : polygonsOfSoundPattern) {
						// prüfen, ob das Sample normiert in einem Polygon vorkommt
						if (polygon.contains((i + j) * normX, (int)(model.getWavFile().getWavFileValues()[0][i + j] * normY))) {
							sampleInPolygons = true;
							break;
						}
					}
					
					if (sampleInPolygons) {
						samplesInPolygonsCounter++;
					}
					
					// prüfen, ob ein Muster gefunden wurde
					// ein Muster gilt als gefunden, wenn die Anzahl der bisher betrachteten Samples mindestens der Mindestlänge eines Musters entspricht
					// UND mindestens 90% aller bisher betrachteten Samples innerhalb der Polygone liegen
					if (j + 1 >= minPatternLength && samplesInPolygonsCounter / (j + 1) >= 0.9) {
						// prüfen, ob es für das gefundene Muster bereits einen Container (= PatternInWavFileDataObject2) gibt und falls nicht einen anlegen
						if (!patternsFound.containsKey(patternStartSampleIndex + ";" + (j + 1))) {
							// Objekt anlegen, in welchem gespeichert wird, wo überall das Pattern zu finden ist
							patternsFound.put(patternStartSampleIndex + ";" + (j + 1), new PatternInWavFileDataObject2(patternStartSampleIndex, patternStartSampleIndex + j));
						}
						patternsFound.get(patternStartSampleIndex + ";" + (j + 1)).addOccurrence(i, i + j, samplesInPolygonsCounter / (j + 1));
					}
				}
				
				// die Polygone um einen Frame weiter verschieben
				for (Polygon polygon : polygonsOfSoundPattern) {
					for (int j = 0; j < polygon.xpoints.length; j++) {
						polygon.xpoints[j] += normX;
					}
					// dem Polygon sagen, dass sich seine Punkte verändert haben
					polygon.invalidate();
				}
				if (messages) {
					System.out.println((i + 1) + "/" + (model.getWavFile().getNumberOfFrames() - soundPatternWavFile.getNumberOfFrames() + 1));
				}
			}
			
		}
		
//		// 3. alle gefundenen Muster ausgeben, bevor sie gefiltert werden
//		if (messages) {
//			System.out.println(occurrences.toString());
//		}
//		
//		// 4. gleiche Funde mit nur unterschiedlichem Suchmuster rausfiltern
//		
//		// jedes Element aus "patternsFound" angucken und...
//			//... ein neues "PatternInWavFileDataObject", in welches nachfolgend Vorkommnisse drin gespeichert werden...
//			//... zunächst werden alle Vorkommnisse aus dem aktuellen "PatternInWavFileDataObject2" in das neue "PatternInWavFileDataObject" gespeichert
//			// ...anschließend wird für jedes im "PatternInWavFileDataObject" gespeicherte Vorkommnis nachfolgendes gemacht...
//				//...und für jedes Vorkommen ein Element in der "patternsFound"-HashMap raussuchen mit dem Schlüssel = "<Index vom Start-Sample vom Vorkommen>;<länge vom aktuellen Patter (= aktuelles "patternsFound"-Element)>" und mit diesem...
//					// ...und mit diesem alle Vorkommnisse aus dem "PatternInWavFileDataObject2" in das aktuelle "PatternInWavFileDataObject" rüber kopieren
//					// und das PatternInWavFileDataObject2 löschen
//					// auf diese Weise werden alle gleichen Musterfunde in einem "PatternInWavFileDataObject" gebündelt!
//		
//		// TODO: ggf. Schritt 6. besser vor Schritt 5 und Schritt 5 ist vielleicht gar nicht notwendig, da es ja eigentlich nur das Ziel ist die Anzahl an Muster innerhalb einer großen WAV-Datei in Erfahrung zu bringen und zu gucken, mit wie vielen eine gesamte beliebige Datei gebaut werden kann 
//		// TODO: Schritt 5 ggf. so optimieren, dass nicht mehr auf die Quote geachtet wird, sondern das Vorkommen behalten wird, was in der Vorkommen-Folge in der Mitte, gemäß dem Start-Sample-Index, liegt
//		// 5. mehrmals an praktisch der gleichen Stelle gefundene Muster rausfiltern
//		// = alle direkt aufeinander folgenden Frames von einzelnen Mustern werden zu einem Muster zusammengefasst
//		// zusammengefasst = das Muster behalten, welches die beste Quote hat und die anderen vergessen
//		for (int i = 0; i < occurrences.numberOfOccurrences(); i++) {
//			int firstPattern = i;
//			int lastPattern = i;
//			
//			// gucken wie viele aufeinander folgende Muster es gibt
//			while (lastPattern + 1 < occurrences.numberOfOccurrences() && occurrences.getFirstSampleIndex(lastPattern) + 1 == occurrences.getFirstSampleIndex(lastPattern + 1)) {
//				lastPattern++;
//			}
//			
//			// wenn mehrere direkt aufeinanderfolgende Muster gefunden wurden, diese auf eins reduzieren
//			if (firstPattern < lastPattern) {
//				// das Muster auswählen, welches behalten werden soll, da es die beste Quote aufweist
//				double maxQuote = 0;
//				int indexOfMaxQuote = firstPattern;
//				
//				for (int j = firstPattern; j < lastPattern + 1; j++) {
//					if (maxQuote < occurrences.getPrecision(j)) {
//						maxQuote = occurrences.getPrecision(j);
//						indexOfMaxQuote = j;
//					}
//				}
//				
//				// alle Muster, welche nicht behalten werden sollen löschen aus den ArrayLists
//				// die Liste von hinten durchgehen, da sich ansonsten die Indizes verschieben und dann nicht alle gewünschten Elemente gelöscht werden
//				for (int j = lastPattern; j >= firstPattern; j--) {
//					if (j != indexOfMaxQuote) {
//						occurrences.removeOccurrence(j);
//					}
//				}
//			}
//		}
//		
//		// 6. Muster mit dem gleichen Start-Sample aber unterschiedlicher Länge filtern, sodass am Ende nur die "PatternInWavFileDataObject"e verbleiben, am meisten Vorkommnisse haben und es zu jedem Start-Sample maximal ein Muster gibt
//			// für jedes "PatternInWavFileDataObject" jedes Vorkommnis in jedem anderen "PatternInWavFileDataObject" suchen
//				// falls in zwei "PatternInWavFileDataObject"'s welche Muster mit unterschiedlichen Längen verwalten, der gleiche Index vom Start-Sample verwendet wird, nur das "PatternInWavFileDataObject" behalten, welches mehr Vorkomnisse aufweist
//		
//		// 7. ausgeben, wie oft das gesuchte Muster gefunden wurde und wo jeweils
//		if (messages) {
//			System.out.println(occurrences.toString());
//		}
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
	
	/**
	 * Die Methode zeichnet ein Polygon um zwei beliebige Punkte.
	 * Dieses Polygon hat einen gewünschten Abstand von den beiden Punkten.
	 * Die Methode ist somit in der Lage ein Polygon um eine beliebige Linie zu zeichnen.
	 * 
	 * @param x1 x-Wert von Punkt 1
	 * @param y1 y-Wert von Punkt 1
	 * @param x2 x-Wert von Punkt 2
	 * @param y2 y-Wert von Punkt 2
	 * @param distance der Abstand vom Polygon zu den Punkten
	 * @return das Polygon, welches die beiden Punkte mit dem gewünschten Abstand umschließt
	 */
	public static Polygon calculatePolygonOfTwoPoints(double x1, double y1, double x2, double y2, double distance) {
		// sicherstellen, dass x1 < x2
		if (x1 > x2) {
			// x-Werte Tauschen
			double tempX = x1;
			x1 = x2;
			x2 = tempX;
			
			// y-Werte Tauschen
			double tempY = y1;
			y1 = y2;
			y2 = tempY;
		}
		
		
		int[] xpoints = new int[5];
		int[] ypoints = new int[5];
		
		double m;
		double m2;
		// wenn die Steigung nicht 0 ist und es somit die beiden Punkte nicht den gleichen x-Wert haben  
		if (x2 - x1 != 0) {
			m = (y2 - y1) / (x2 - x1); // Steigung der Linie 
			m2 = -1 / m; // orthogonale Steigung zur Linie
		// wenn die beiden Punkte den gleichen x-Wert haben und daher der Nenner 0 ergeben würde, dann ist die Steigung=unendlich
		} else {
			m = Double.POSITIVE_INFINITY; // Steigung der Linie
			m2 = 0; // orthogonale Steigung zur Linie
		}
		
		Point pOne = Main.calculatePointOnLineWithSpecificDistanceToAnotherPoint(x1, y1, m, distance, false);
		Point pTwo = Main.calculatePointOnLineWithSpecificDistanceToAnotherPoint(x2, y2, m, distance, true);
		// 1. Punkt
		Point p1 = Main.calculatePointOnLineWithSpecificDistanceToAnotherPoint(pOne.getX(), pOne.getY(), m2, distance, true);
		xpoints[0] = p1.x;
		ypoints[0] = p1.y;
		xpoints[4] = p1.x;
		ypoints[4] = p1.y;
		
		// 2. Punkt
		Point p2 = Main.calculatePointOnLineWithSpecificDistanceToAnotherPoint(pOne.getX(), pOne.getY(), m2, distance, false);
		xpoints[1] = p2.x;
		ypoints[1] = p2.y;

		// 3. Punkt
		Point p3 = Main.calculatePointOnLineWithSpecificDistanceToAnotherPoint(pTwo.getX(), pTwo.getY(), m2, distance, false);
		xpoints[2] = p3.x;
		ypoints[2] = p3.y;

		// 4. Punkt
		Point p4 = Main.calculatePointOnLineWithSpecificDistanceToAnotherPoint(pTwo.getX(), pTwo.getY(), m2, distance, true);
		xpoints[3] = p4.x;
		ypoints[3] = p4.y;
		
		return new Polygon(xpoints, ypoints, 5);
	}
	
	/**
	 * Die Methode bestimmt einen Punkt, welcher auf einer Gerade (mit der Steigung m und dem Punkt x, y) liegt und von dem Punkt x, y eine gewünschte Entfernung hat. 
	 * Außerdem kann bestimmt werden, ob der zu bestimmende Punkt auf der x-Achse links (followM = false) oder auf der x-Achse rechts (upwards = true) neben dem Punkt x, y liegen soll.
	 * Falls die Steigung m = unendlich, da die Gerade senkrecht verläuft, dann ist der zu bestimmende Punkt auf der y-Achse unter (followM = false) oder auf der y-Achse über (followM = true) dem Punkt x, y. 
	 * 
	 * @param x
	 * @param y
	 * @param m
	 * @param distance
	 * @param followM
	 * @return
	 */
	public static Point calculatePointOnLineWithSpecificDistanceToAnotherPoint(double x, double y, double m, double distance, boolean followM) {
		double n = y - m * x;
		
		double pointX;
		double pointY;
		
		// prüfen, ob die Steigung != unendlich ist
		if (m != Double.POSITIVE_INFINITY && m != Double.NEGATIVE_INFINITY) {
			if (followM) {
				pointX=(-(m*n)+x+m*y+(Math.sqrt(Math.pow(distance, 2)+Math.pow(distance, 2)*Math.pow(m, 2)-(Math.pow(n, 2))-(2*m*n*x)-(Math.pow(m, 2)*Math.pow(x, 2))+2*n*y+2*m*x*y-(Math.pow(y, 2)))))/(1+Math.pow(m, 2));
			} else {
				pointX=(-(m*n)+x+m*y-(Math.sqrt(Math.pow(distance, 2)+Math.pow(distance, 2)*Math.pow(m, 2)-(Math.pow(n, 2))-(2*m*n*x)-(Math.pow(m, 2)*Math.pow(x, 2))+2*n*y+2*m*x*y-(Math.pow(y, 2)))))/(1+Math.pow(m, 2));
			}
			
			pointY = m * pointX + n;
		// wenn die Steigung = unendlich ist
		} else {
			pointX = x;
			
			if (followM) {
				pointY = y + distance;
			} else {
				pointY = y - distance;
			}
		}

		return new Point((int)pointX, (int)pointY);
	}
	
	/**
	 * Die Methode durchsucht eine WAV-Datei nach dem vorkommen des Musters einer anderen WAV-Datei. 
	 * 
	 * @param wavFile die zu durchsuchende WAV-Datei
	 * @param pattern das Muster nachdem in der anderen WAV-Datei gesucht werden soll
	 * @param messages ob der Fortschritt und die Funde ausgegeben werden sollen auf der Konsole
	 * 
	 * @return gibt an wo das Muster in der WAV-Datei gefunden wurde 
	 */
	public PatternInWavFileDataObject searchSoundPattern(WavFileDataObject wavFile, WavFileDataObject pattern, boolean messages) {
		// das Muster in der aktuellen WAV-Datei suchen
			// Bedingungen: die Punkte werden normiert --> x * 100; y * 100 
		int normX = 100;
		int normY = 100;
		int tolerance = 10;
		
			// 1. Polygone vom Muster erstellen
		ArrayList<Polygon>	polygonsOfSoundPattern = new ArrayList<Polygon>();
		
		// die Polygone der Peeks erstellen
		for (int i = 0; i < pattern.getInicesOfPeeks().length - 1; i++) {
			int indexOfPeek = pattern.getInicesOfPeeks()[i];
			int indexOfNextPeek = pattern.getInicesOfPeeks()[i + 1];
			// Polygone um die Punkte berechnen
			polygonsOfSoundPattern.add(Main.calculatePolygonOfTwoPoints(indexOfPeek * normX, (int)(pattern.getWavFileValues()[0][indexOfPeek] * normY),
					indexOfNextPeek * normX, (int)(pattern.getWavFileValues()[0][indexOfNextPeek] * normY), tolerance));
		}
		
//		// Polygone für alle Samples erstellen (= genauer aber schlechtere Performance als die obere Variante)
//		for (int indexOfPeek = 0; indexOfPeek < soundPatternWavFile.getNumberOfFrames() - 1; indexOfPeek++) {
//			// Polygone um die Punkte berechnen
//			polygonsOfSoundPattern.add(Main.calculatePolygonOfTwoPoints(indexOfPeek * normX, (int)(soundPatternWavFile.getWavFileValues()[0][indexOfPeek] * normY),
//					(indexOfPeek + 1) * normX, (int)(soundPatternWavFile.getWavFileValues()[0][indexOfPeek + 1] * normY), tolerance));
//		}
		
			// 2. nach dem Muster in der aktuellen Datei suchen
		PatternInWavFileDataObject occurrences = new PatternInWavFileDataObject();
		
		// von jedem Sample in der aktuellen WAV-Datei aus prüfen, ob die darauffolgenden Samples dem gesuchten Muster entsprechen
		for (int i = 0; i < wavFile.getNumberOfFrames() - pattern.getNumberOfFrames() + 1; i++) {
			double samplesInPolygonsCounter = 0;
			
			for (int j = 0; j < pattern.getNumberOfFrames(); j++) {
				boolean sampleInPolygons = false;
				
				// prüfen, ob es überhaupt noch möglich ist die gewünschte Quote (= Sample innerhalb der Polygone) zu erfüllen
				if ((samplesInPolygonsCounter + pattern.getNumberOfFrames() - j) / pattern.getNumberOfFrames() < 0.9) {
					// wenn die Quote nicht mehr erreicht werden kann, dann die Mustersuche an dieser Stelle beenden!
					break;
				}
				
				for (Polygon polygon : polygonsOfSoundPattern) {
					// prüfen, ob das Sample normiert in einem Polygon vorkommt
					if (polygon.contains((i + j) * normX, (int)(wavFile.getWavFileValues()[0][i + j] * normY))) {
						sampleInPolygons = true;
						break;
					}
				}
				
				if (sampleInPolygons) {
					samplesInPolygonsCounter++;
				}
			}
			
			// ein Muster gilt als gefunden, wenn mindestens 90 % der betrachteten Samples innerhalb der Polygone liegen
			if (samplesInPolygonsCounter / pattern.getNumberOfFrames() >= 0.9) {
				occurrences.addOccurrence(i, i + pattern.getNumberOfFrames() - 1, samplesInPolygonsCounter / pattern.getNumberOfFrames());
			}
			
			// die Polygone um einen Frame weiter verschieben
			for (Polygon polygon : polygonsOfSoundPattern) {
				for (int j = 0; j < polygon.xpoints.length; j++) {
					polygon.xpoints[j] += normX;
				}
				// dem Polygon sagen, dass sich seine Punkte verändert haben
				polygon.invalidate();
			}
			if (messages) {
				System.out.println(i + "/" + wavFile.getNumberOfFrames());
			}
		}
		
			// 3. alle gefundenen Muster ausgeben, bevor sie gefiltert werden
		if (messages) {
			System.out.println(occurrences.toString());
		}
		
			// 4. mehrmals an praktisch der gleichen Stelle gefundene Muster rausfiltern
			// = alle direkt aufeinander folgenden Frames von einzelnen Mustern werden zu einem Muster zusammengefasst
			// zusammengefasst = das Muster behalten, welches die beste Quote hat und die anderen vergessen
		for (int i = 0; i < occurrences.numberOfOccurrences(); i++) {
			int firstPattern = i;
			int lastPattern = i;
			
			// gucken wie viele aufeinander folgende Muster es gibt
			while (lastPattern + 1 < occurrences.numberOfOccurrences() && occurrences.getFirstSampleIndex(lastPattern) + 1 == occurrences.getFirstSampleIndex(lastPattern + 1)) {
				lastPattern++;
			}
			
			// wenn mehrere direkt aufeinanderfolgende Muster gefunden wurden, diese auf eins reduzieren
			if (firstPattern < lastPattern) {
				// das Muster auswählen, welches behalten werden soll, da es die beste Quote aufweist
				double maxQuote = 0;
				int indexOfMaxQuote = firstPattern;
				
				for (int j = firstPattern; j < lastPattern + 1; j++) {
					if (maxQuote < occurrences.getPrecision(j)) {
						maxQuote = occurrences.getPrecision(j);
						indexOfMaxQuote = j;
					}
				}
				
				// alle Muster, welche nicht behalten werden sollen löschen aus den ArrayLists
				// die Liste von hinten durchgehen, da sich ansonsten die Indizes verschieben und dann nicht alle gewünschten Elemente gelöscht werden
				for (int j = lastPattern; j >= firstPattern; j--) {
					if (j != indexOfMaxQuote) {
						occurrences.removeOccurrence(j);
					}
				}
			}
		}
		
			// 5. ausgeben, wie oft das gesuchte Muster gefunden wurde und wo jeweils
		if (messages) {
			System.out.println(occurrences.toString());
		}
		
		return occurrences;
	}
	
	/**
	 * Die Methode ermöglicht es nur ein gewünschtes Intervall von Samples zu behalten und die restlichen Samples zu löschen.
	 * Hinweis: firstSample muss < lastSample sein!
	 * 
	 * @param wavFile die WAV-Datei, aus welcher Samples ausgeschnitten werden sollen
	 * @param firstSample Index vom ersten Sample, welches behalten werden soll (inklusive)
	 * @param lastSample Index vom letzten Sample, welches behalten werden soll (inklusive)
	 */
	public void keepSamplesInInterval(WavFileDataObject wavFile, int firstSample, int lastSample) {
		double[][] oldWavFileValues = wavFile.getWavFileValues();
		double[][] newWavFileValues = new double[oldWavFileValues.length][lastSample - firstSample + 1];
		
		// gewünschte Samples in das neue Array kopieren
		int offset = firstSample;
		for (int i = 0; offset + i <= lastSample; i++) {
			newWavFileValues[0][i] = oldWavFileValues[0][offset + i];
		}
		
		// die neuen Sample-Werte übergeben 
		wavFile.setWavFileValues(newWavFileValues);
	}
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
}