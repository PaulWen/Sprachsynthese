package wenzel.paul.speechsynthesis.util.wav;

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
	
	private WavFileDataObject wavFileDataObject;
	private Thread playbackThread;
	
	private int indexOfPlaybackPosition;
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link WavFilePlayer}. 
	 */
	public WavFilePlayer(WavFileDataObject wavFileDataObject) {
		//Datenfelder initialisieren
		this.wavFileDataObject = wavFileDataObject;
		
		indexOfPlaybackPosition = 0;
		
		state = WavFilePlayer.PAUSED;
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
	
	public void play() {
		if (state != WavFilePlayer.PLAYS) {
			
			playbackThread = new Thread(new Runnable() {
				
				public void run() {
					while (!Thread.currentThread().isInterrupted()) {
						
						// berechnen wie viele Samples geladen werden sollen
						int numberOfSamplesToConvert;
						if (wavFileDataObject.getNumberOfFrames() - (indexOfPlaybackPosition + 1) < MAX_SAMPLES_TO_CONVERT) {
							numberOfSamplesToConvert = wavFileDataObject.getNumberOfFrames() - (indexOfPlaybackPosition + 1);
						} else {
							numberOfSamplesToConvert = MAX_SAMPLES_TO_CONVERT;
						}
						
						// die nächsten Bytes laden
						byte[] values = doubleValuesToByte(wavFileDataObject.getWavFileValues(),
								wavFileDataObject.getValidBits(), (wavFileDataObject.getValidBits() + 7) / 8,
								indexOfPlaybackPosition, numberOfSamplesToConvert);
							
						int i = 0;
						while (!Thread.currentThread().isInterrupted()) {
							// bytes wiedergeben
							System.out.println(values[i]);
							
							i++;
							indexOfPlaybackPosition++;
						}
					}
				}
			});
			
			playbackThread.start();
			
			state = WavFilePlayer.PLAYS;
		}
	}
	
	public void pause() {
		if (state != WavFilePlayer.PAUSED) {
			playbackThread.interrupt();
	
			state = WavFilePlayer.PAUSED;
		}
	}
	
	private byte[] doubleValuesToByte(	double[][] wavFileValues, int validBits, int bytesPerSample,
										int indexOfFirstFrame, int numberOfSamplesToConvert) {
										
		// Local buffer used for IO
		byte[] buffer = new byte[numberOfSamplesToConvert * bytesPerSample * wavFileValues.length];
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
		for (int frameNumber = indexOfFirstFrame; frameNumber < numberOfSamplesToConvert
				+ numberOfSamplesToConvert; frameNumber++) {
			// für alle Channels
			for (int channelNumber = 0; channelNumber < wavFileValues.length; channelNumber++) {
				
				long sample = (long) (floatScale * (floatOffset + wavFileValues[channelNumber][frameNumber]));
				
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