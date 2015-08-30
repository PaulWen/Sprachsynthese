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
	
//	private File wavFile;						// File that will be read from or written to
//	private int bytesPerSample;			// Number of bytes required to store a single sample
	private long numberOfFrames;					// Number of frames within the data section
//	private double floatScale;				// Scaling factor used for int <-> float conversion				
//	private double floatOffset;			// Offset factor used for int <-> float conversion				
//	private boolean wordAlignAdjust;		// Specify if an extra byte at the end of the data chunk is required for word alignment

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
		
		numberOfFrames = wavFileValues[0].length;
	}
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	

	
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////
	
	
	
	
	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	public double[][] getWavFileValues() {
		return wavFileValues;
	}
	
	public long getNumberOfFrames() {
		return numberOfFrames;
	}
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}
