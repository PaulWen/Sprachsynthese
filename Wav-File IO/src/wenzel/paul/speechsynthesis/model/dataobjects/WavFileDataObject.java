package wenzel.paul.speechsynthesis.model.dataobjects;

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
	/** die Anzahl an Channels */
	private int numberOfChannels;				
	
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
	public WavFileDataObject(int numberOfChannels, long sampleRate, int blockAlign, int validBits,
			double[][] wavFileValues)
	{
		this.numberOfChannels = numberOfChannels;
		this.sampleRate = sampleRate;
		this.blockAlign = blockAlign;
		this.validBits = validBits;
		this.wavFileValues = wavFileValues;
	}
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	public double[][] getWavFileValues() {
		return wavFileValues;
	}
	
	public int getNumberOfChannels() {
		return numberOfChannels;
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
	
	public long getNumberOfFrames() {
		return wavFileValues[0].length;
	}
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}
