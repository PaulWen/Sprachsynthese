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
	private Thread playbackThread;
	
	/** der Index des als nächstes wiederzugebenden Frames */
	private int indexOfPlaybackPosition;

	/** hier wird der Sound ausgegeben */
	SourceDataLine sourceLine;
	
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
			 AudioFormat audioFormat = audioStream.getFormat();
			 DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
			 sourceLine = (SourceDataLine) AudioSystem.getLine(info);
			 sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
	}
	
	
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	/**
	 * Über die Methode kann der Startpunkt für die Wiedergabe festgelegt werden.
	 * 
	 * @param indexOfPosition der Index vom Frame, bei welchem die WIedergabe gestartet werden soll
	 */
	public void setIndexOfPosition(int indexOfPosition) {
		this.indexOfPlaybackPosition = indexOfPosition;
	}
	
	public int getState() {
		return state;
	}
	
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////
	
	
	
	
	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	
	private void playbackRoutine() {
		while (!Thread.currentThread().isInterrupted()) {
			
		//+++++berechnen wie viele Samples geladen werden sollen+++++//
			int numberOfSamplesToConvert = 0;
			
			// wenn die Datei bereits komplett wiedergegeben wurde
			if (wavFileDataObject.getNumberOfFrames() - indexOfPlaybackPosition == 0) {
				// die Playbackposition zurück setzen
				indexOfPlaybackPosition = 0;
				
				// die Wiedergabe beenden
				System.out.println("DRAIN");
				sourceLine.drain();
				indexOfPlaybackPosition = sourceLine.getFramePosition();
				System.out.println(indexOfPlaybackPosition);
				pause();
				return;
				
			// wenn weniger als die maximale Anzahl an auf einmal zu konvertierenden Frames noch vorhanden sind
			} else if (wavFileDataObject.getNumberOfFrames() - indexOfPlaybackPosition < MAX_SAMPLES_TO_CONVERT) {
				numberOfSamplesToConvert = wavFileDataObject.getNumberOfFrames() - indexOfPlaybackPosition;
			
			// wenn noch mehr als die maximale Anzahl an auf einmal zu konvertierenden Frames noch vorhanden sind
			} else {
				numberOfSamplesToConvert = MAX_SAMPLES_TO_CONVERT;
			}
			
		//+++++Bytes laden+++++//
			// die nächsten Bytes laden
			byte[] values = doubleValuesToByte(wavFileDataObject.getWavFileValues(), wavFileDataObject.getNumberOfChannels(),
					wavFileDataObject.getValidBits(), wavFileDataObject.getBytesPerSample(),
					indexOfPlaybackPosition, numberOfSamplesToConvert);
		
		//+++++Bytes wiedergeben+++++//
			int numberOfBytesPlayed = 0;
			while (!Thread.currentThread().isInterrupted() && numberOfBytesPlayed < values.length) {
				// Anzahl der auf einmal wiederzugebenden Bytes (= BytesPerSample * NumberOfChannels)
				// diese Anzahl an Bytes stellt genau ein Frame dar
				int numberOfBytesToPlayAtOnce = wavFileDataObject.getBytesPerSample() * wavFileDataObject.getNumberOfChannels();
				sourceLine.write(values, numberOfBytesPlayed, numberOfBytesToPlayAtOnce);
				sourceLine.getBufferSize();
				
				numberOfBytesPlayed += numberOfBytesToPlayAtOnce;
				indexOfPlaybackPosition++;
			}
		}
	}
	
	public void play() {
		if (state != WavFilePlayer.PLAYS) {
			// Wiedergabe Thread aufbauen
			playbackThread = new Thread(new Runnable() {
				public void run() {
					playbackRoutine();
				}
			});
			
			// die Wiedergabe wirklich starten
			sourceLine.start();
			playbackThread.start();
			state = WavFilePlayer.PLAYS;
		}
	}
	
	public void pause() {
		if (state != WavFilePlayer.PAUSED) {
			indexOfPlaybackPosition = sourceLine.getFramePosition();
			System.out.println(indexOfPlaybackPosition);

			// die Wiedergaeb pausieren
			sourceLine.flush();
			sourceLine.stop();
			playbackThread.interrupt();
			state = WavFilePlayer.PAUSED;
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