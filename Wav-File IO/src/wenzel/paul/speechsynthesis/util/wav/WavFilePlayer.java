package wenzel.paul.speechsynthesis.util.wav;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.Control.Type;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
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
public class WavFilePlayer implements LineListener {

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
//			 sourceLine = (SourceDataLine) AudioSystem.getLine(info);
//			 sourceLine.addLineListener(this);
//			 sourceLine.open(audioFormat);
			
			
			
			final SourceDataLine testSourceLine =((SourceDataLine) AudioSystem.getLine(info));
			 sourceLine = new SourceDataLine() {
				
				public void removeLineListener(LineListener listener) {
						testSourceLine.removeLineListener(listener);
					
					System.out.println("removeLineListener");
					
				}
				
				public void open() throws LineUnavailableException {
					try {
						testSourceLine.open();
					} catch (LineUnavailableException e) {
						e.printStackTrace();
					}
					System.out.println("open");
					
				}
				
				public boolean isOpen() {
					System.out.println("isOpen");
						return	testSourceLine.isOpen();
				}
				
				public boolean isControlSupported(Type control) {
					System.out.println("isControlSupported");
						return testSourceLine.isControlSupported(control);
				}
				
				public Line.Info getLineInfo() {
						System.out.println("getLineInfo");
						return	testSourceLine.getLineInfo();
				}
				
				public Control[] getControls() {
					System.out.println("getControls");
						return	testSourceLine.getControls();
				}
				
				public Control getControl(Type control) {
					System.out.println("getControl");
					return testSourceLine.getControl(control);
				}
				
				public void close() {
					System.out.println("close");
						testSourceLine.close();
				}
				
				public void addLineListener(LineListener listener) {
					System.out.println("addLineListener");
						testSourceLine.addLineListener(listener);
				}
				
				public void stop() {
					System.out.println("stop");
						testSourceLine.stop();
				}
				
				public void start() {
					System.out.println("start");
						testSourceLine.start();
				}
				
				public boolean isRunning() {
					System.out.println("isRunning");
						return testSourceLine.isRunning();
				}
				
				public boolean isActive() {
					System.out.println("isActive");
						return testSourceLine.isActive();
				}
				
				public long getMicrosecondPosition() {
					System.out.println("getMicrosecondPosition");
						return testSourceLine.getMicrosecondPosition();
				}
				
				public long getLongFramePosition() {
					System.out.println("getLongFramePosition");
						return testSourceLine.getLongFramePosition();
				}
				
				public float getLevel() {
					System.out.println("getLevel");
						return testSourceLine.getLevel();
				}
				
				public int getFramePosition() {
					System.out.println("getFramePosition");
					return testSourceLine.getFramePosition();
				}
				
				public AudioFormat getFormat() {
					System.out.println("getFormat");
						return testSourceLine.getFormat();
				}
				
				public int getBufferSize() {
					System.out.println("getBufferSize");
						return testSourceLine.getBufferSize();
				}
				
				public void flush() {
					System.out.println("flush");
						testSourceLine.flush();
				}
				
				public void drain() {
					System.out.println("drain");
						testSourceLine.drain();
				}
				
				public int available() {
					System.out.println("available");
						return testSourceLine.available();
				}
				
				public int write(byte[] b, int off, int len) {
					System.out.println("write");
						return testSourceLine.write(b, off, len);
				}
				
				public void open(AudioFormat format, int bufferSize) throws LineUnavailableException {
					System.out.println("open(AudioFormat format, int bufferSize)");
						testSourceLine.open(format, bufferSize);
				}
				
				public void open(AudioFormat format) throws LineUnavailableException {
					System.out.println("open(AudioFormat format)");
						testSourceLine.open(format);
				}
			};
			sourceLine.addLineListener(this);
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
	
	public void update(LineEvent event) {
		System.out.println("Event: " + event);
	}
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	private void loadFramesIntoAudioLine(int indexOfNextFrameToLoad, int numberOfFramesToLoad) {
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
		sourceLine.drain();
		System.out.println("zuende");
	}
	
	/**
	 * Die Methode setzt die Wiedergabe bei der aktuellen Position fort.
	 */
	public void play() {
		if (state != WavFilePlayer.PLAYS) {
			System.out.println("play: " + indexOfPlaybackPosition);
			// Daten lade Thread starten
			loadDataThread = new Thread(new Runnable() {
				public void run() {
					loadFramesIntoAudioLine(indexOfPlaybackPosition, wavFileDataObject.getNumberOfFrames() - indexOfPlaybackPosition);
				}
			});
			loadDataThread.start();
			
			// die Wiedergabe starten
			sourceLine.start();
			state = WavFilePlayer.PLAYS;
		}
	}
	
	/**
	 * Die Methdoe pasusiert die Wiedergabe an der aktuellen Position.
	 */
	public void pause() {
		if (state != WavFilePlayer.PAUSED) {
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
			System.out.println("pause: " + indexOfPlaybackPosition);

			// für den nächsten Start alles vorbereiten
			sourceLine.flush();
			
			try {
				sourceLine = null;
				sourceLine = (SourceDataLine) AudioSystem.getLine(info);
				sourceLine.addLineListener(this);
			 	sourceLine.open(audioFormat);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
			
			// Status updaten
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