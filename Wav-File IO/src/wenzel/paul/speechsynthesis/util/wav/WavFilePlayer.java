package wenzel.paul.speechsynthesis.util.wav;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import wenzel.paul.speechsynthesis.model.dataobjects.WavFileDataObject;

/**
 * Die Klasse {@link WavFilePlayer} [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public class WavFilePlayer {

//////////////////////////////////////////////////Konstanten//////////////////////////////////////////////////

	private final int MAX_SAMPLES_TO_CONVERT = 100;
	
	/** der Player spielt das WAV-File ab */
	public static final int PLAYS = 0;
	
	/** der Player ist pausiert */
	public static final int PAUSED = 1;
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	/** der Status vom Player (siehe Konstanten der Klasse {@link WavFilePlayer}) */
	private int state;
	
	/** das {@link WavFileDataObject}, welches die wiederzugebende WAV-Datei repräsentiert */
	private WavFileDataObject wavFileDataObject;
	
	/** der Thread, in welchem die Wiedergabe statt finden soll */
	private Thread loadDataThread;
	
	/** 
	 * Der Thread wartet, bis die Wiedergabe gestoppt wird und führt danach alle nötigen Schritte aus,
	 * um alles für die nächste Wiedergabe vorzubereiten.
	 * 
	 * (Für diese Aufgabe muss leider ein eigener Thread fungieren, da der LineListener leider nicht wie 
	 * versprochen bescheid sagt, wenn die Wiedergabe zu Ende ist.)
	 */
	private Thread watchPlayback;
	
	/** der Index des als nächstes wiederzugebenden Frames */
	private int indexOfPlaybackPosition;
	
	/** hier wird der Sound ausgegeben */
	private SourceDataLine sourceLine;
	
	private AudioFormat audioFormat;
	private DataLine.Info info;
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link WavFilePlayer}. 
	 */
	public WavFilePlayer(WavFileDataObject wavFileDataObject) {
		//Datenfelder initialisieren
		this.wavFileDataObject = wavFileDataObject;
		
		indexOfPlaybackPosition = 0;
		state = WavFilePlayer.PAUSED;
		
		// Soundwiedergabe vorbereiten
		try {
			File soundFile = wavFileDataObject.getFile();
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
			audioFormat = audioStream.getFormat();
			info = new DataLine.Info(SourceDataLine.class, audioFormat);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		preparePlayback();
	}
	
	
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	/**
	 * Die Methode gibt den Status (siehe Konstanten der Klasse {@link WavFilePlayer}) vom Player aus.
	 * 
	 * @return Status vom Player (siehe Konstanten der Klasse {@link WavFilePlayer})
	 */
	public int getState() {
		return state;
	}
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////
	

	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	/**
	 * Die Methode bereitet alle Threads etc. vor, damit eine Wiedergabe gestartet werden kann.
	 */
	private void preparePlayback() {
		// neue Source Line aufbauen
		if (sourceLine != null) {
			sourceLine.flush();
			sourceLine.close();
			sourceLine = null;
		}
		try {
			sourceLine = null;
			sourceLine = (SourceDataLine) AudioSystem.getLine(info);
			sourceLine.open(audioFormat);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		// Daten lade Thread aufbauen
		if (loadDataThread != null) {
			loadDataThread = null;
		}
		loadDataThread = new Thread(new Runnable() {
			public void run() {
				loadFramesIntoSourceLine(indexOfPlaybackPosition, wavFileDataObject.getNumberOfFrames() - indexOfPlaybackPosition);
			}
		});
		
		// Watch Thread aufbauen
		if (watchPlayback != null) {
			watchPlayback = null;
		}
		watchPlayback = new Thread(new Runnable() {
			public void run() {
				
				// diese Pause ist nötig, da ansonsten die Wiedergabe noch nicht gestartet wurde und somit die drain-methode
				// (siehe wieter unten) direkt returnen würde und somit die Wiedergabe direkt wieder pausiert werden würde
				try {
					Thread.currentThread().sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				// die Methdoe returned erst, wenn alle im Buffer vorhandenen Daten komplett ausgegeben wurden
				sourceLine.drain();

				// die Pause ist nötig, da wenn mitten in der Wiedergabe pasusiert wird zwei mal pausiert wird
				// 1. durch den Nutzer (über den Button)
				// 2. vom Wiedergabe-Überwachungs-Thread (diesem hier), da er sieht, das die WIedergaeb beendet wurde
				try {
					Thread.currentThread().sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				// die Wiedergabe pausieren
				pause();
			}
		});
		
	}
	
	/**
	 * Die Methode sorgt dafür, dass gewünschte Frames für die Wiedergabe in die SourceLine geladen werden.
	 * 
	 * @param indexOfNextFrameToLoad der Index vom Start-Frame
	 * @param numberOfFramesToLoad wie viele Frames, vom Start-Frame ausgehend, geladen werden sollen
	 */
	private void loadFramesIntoSourceLine(int indexOfNextFrameToLoad, int numberOfFramesToLoad) {
		// Anzahl der Frames, welche übersprungen werden sollen
		int offset = indexOfNextFrameToLoad;
		
		// solange der Thread nicht unterbrochen wurde und noch nicht alle Frames geladen wurden
		// weiter die nächsten Frames laden
		while (!Thread.currentThread().isInterrupted()  && numberOfFramesToLoad - (indexOfNextFrameToLoad - offset) > 0) {
		//+++++berechnen wie viele Samples geladen werden sollen+++++//
			
			// wenn der Buffer für die Soundwiedergabe noch nicht voll ist und noch nicht alle Frames geladen wurden,
			// dann weitere Frames laden
			if (sourceLine.available() > 0) {
				int numberOfFramesToConvert = 0;
				
					
				// wenn weniger als die maximale Anzahl an auf einmal zu konvertierenden Frames noch vorhanden sind
				if (numberOfFramesToLoad - (indexOfNextFrameToLoad - offset) < MAX_SAMPLES_TO_CONVERT) {
					numberOfFramesToConvert = numberOfFramesToLoad - (indexOfNextFrameToLoad - offset);
					
				// wenn noch mehr als die maximale Anzahl an auf einmal zu konvertierenden Frames noch vorhanden sind
				} else {
					numberOfFramesToConvert = MAX_SAMPLES_TO_CONVERT;
				}
				
				// prüfen ob für soviele Frames auch noch Platz im Buffer der Audioline ist
				if (sourceLine.available() < numberOfFramesToConvert) {
					numberOfFramesToConvert = sourceLine.available();
				}
				
				//+++++Bytes laden+++++//
				// die nächsten Bytes laden
				byte[] values = doubleValuesToByte(wavFileDataObject.getWavFileValues(), wavFileDataObject.getNumberOfChannels(),
						wavFileDataObject.getValidBits(), wavFileDataObject.getBytesPerSample(),
						indexOfNextFrameToLoad, numberOfFramesToConvert);
				
				//+++++Bytes in die AudioLine laden+++++//
				sourceLine.write(values, 0, values.length);
				indexOfNextFrameToLoad += numberOfFramesToConvert;
			}
		}
	}
	
	/**
	 * Die Methode stoppt die Wiedergabe und legt als aktuelle Wiedergabeposition den Anfang der Datei fest.
	 */
	public void stop() {
		pause();
		indexOfPlaybackPosition = 0;
	}
	
	/**
	 * Die Methode setzt die Wiedergabe bei der aktuellen Position fort.
	 */
	public void play() {
		if (state != WavFilePlayer.PLAYS) {
			// Status updaten
			state = WavFilePlayer.PLAYS;

			// die Wiedergabe starten
			sourceLine.start();
			
			// Daten laden (starten)
			loadDataThread.start();
			
			// Überwachen wann die Wiedergabe zuende ist (starten)
			watchPlayback.start();
		}
	}
	
	/**
	 * Die Methode pausiert die Wiedergabe an der aktuellen Position.
	 */
	public void pause() {
		if (state != WavFilePlayer.PAUSED) {
			// Status updaten
			state = WavFilePlayer.PAUSED;

			// Wiedergabe pausieren
			sourceLine.stop();
			
			//Daten lade Thread stoppen
			loadDataThread.interrupt();

			// wenn die Wiedergabe am Ende angelangt ist, sie auf Anfang zurücksetzen
			if (indexOfPlaybackPosition + sourceLine.getFramePosition() >= wavFileDataObject.getNumberOfFrames()) {
				indexOfPlaybackPosition = 0;
			} else {
				indexOfPlaybackPosition += sourceLine.getFramePosition();
			}
			
			// alles für die nächste Wiedergabe vorbereiten
			preparePlayback();
		}
	}
	
	private byte[] doubleValuesToByte(	double[][] wavFileValues, int numberOfChannels, int validBits, int bytesPerSample,
										int indexOfFirstFrame, int numberOfSamplesToConvert) {
										
		// Local buffer used for IO
		byte[] buffer = new byte[numberOfSamplesToConvert * bytesPerSample * numberOfChannels];
		int bufferPointer = 0; // Points to the current position in local buffer
		
		double floatOffset;
		double floatScale;
		
		// Calculate the scaling factor for converting to a normalised
		// double
		if (validBits > 8) {
			// If more than 8 validBits, data is signed
			// Conversion required multiplying by magnitude of max positive
			// value
			floatOffset = 0;
			floatScale = Long.MAX_VALUE >> (64 - validBits);
		} else {
			// Else if 8 or less validBits, data is unsigned
			// Conversion required dividing by max positive value
			floatOffset = 1;
			floatScale = 0.5 * ((1 << validBits) - 1);
		}
		
		// für die gewünschten Frames
		for (int frameNumber = indexOfFirstFrame; frameNumber < indexOfFirstFrame
				+ numberOfSamplesToConvert; frameNumber++) {
			// für alle Channels
			for (int channelNumber = 0; channelNumber < numberOfChannels; channelNumber++) {
				
				long sample = Math.round( (floatScale * (floatOffset + wavFileValues[channelNumber][frameNumber])));
				
				// Sample in Bytes umwandeln und in die Datei schreiben
				for (int b = 0; b < bytesPerSample; b++) {
					buffer[bufferPointer] = (byte) (sample & 0xFF);
					sample >>= 8;
					bufferPointer++;
				}
				
			}
		}
		
		return buffer;
	}
	
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}