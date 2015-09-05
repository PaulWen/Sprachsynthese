package wenzel.paul.speechsynthesis.model.dataobjects;

import java.io.File;

/**
 * Die Klasse {@link WavFileDataObject} [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public class WavFileDataObject {
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////

	// Wav Header
	/** die Sample Rate */
	private long sampleRate;				
	
	/** 
	 * Blockausrichtung:
	 * 
	 * Anzahl der Bytes pro Block
	 * 
	 * ein Block = Anzahl der Bits pro Sample (auf ein Byte aufgerundet) * Anzahl der Kanäle
	 * 
	 */
	private int blockAlign;					
	
	/** Anzahl der Bits pro Sample */
	private int validBits;				

	// WAV Data
	/** die einzelnen Frame-Werte */
	private double[][] wavFileValues;	
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link WavFileDataObject}.
	 * 
	 * @param wavFile
	 * @param numberOfChannels
	 * @param sampleRate
	 * @param blockAlign
	 * @param validBits
	 * @param wavFileValues
	 */
	public WavFileDataObject(long sampleRate, int blockAlign, int validBits,
			double[][] wavFileValues)
	{
		this.sampleRate = sampleRate;
		this.blockAlign = blockAlign;
		this.validBits = validBits;
		this.wavFileValues = wavFileValues;
	}
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	public double[][] getWavFileValues() {
		return wavFileValues;
	}
	
	public int getValidBits() {
		return validBits;
	}
	
	public long getSampleRate() {
		return sampleRate;
	}
	
	public int getBlockAlign() {
		return blockAlign;
	}
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////
	
	
	
	
	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	public int getNumberOfFrames() {
		return wavFileValues[0].length;
	}
	
	/** die Anzahl an Channels */
	public int getNumberOfChannels() {
		return wavFileValues.length;
	}
	
	public int getBytesPerSample() {
		return (validBits + 7) / 8;
	}

	public File getFile() {
		return new File("C:/Users/Wenze/Desktop/Java Workspace/Sprachsynthese/Wav-File IO/res/wav_examples/welcome.wav");
	}
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}
