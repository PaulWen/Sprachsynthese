package wenzel.paul.speechsynthesis.model.dataobjects;

import java.util.ArrayList;

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
	/** die Sample Rate --> Anzahl der zu spielenden Samples in der Sekunde */
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
	
	/** ein Array mit allen Indizes der Samples, welche Peeks sind */
	private int[] inicesOfPeeks;
	
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
		inicesOfPeeks = inicesOfPeeks();
	}
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	public double[][] getWavFileValues() {
		return wavFileValues;
	}
	
	public void setWavFileValues(double[][] wavFileValues) {
		this.wavFileValues = wavFileValues;
		inicesOfPeeks = inicesOfPeeks();
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
	
	/**
	 * Die Methode gibt ein Array mit allen Indizes der Samples, welche Peeks sind, aus.
	 * 
	 * @return ein Array mit allen Indizes der Samples, welche Peeks sind
	 */
	public int[] getInicesOfPeeks() {
		return inicesOfPeeks;
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
	
	/**
	 * Die Methode bestimmt die Indizes der Samples, welche Peeks sind.
	 * 
	 * @return ein Array mit allen Indizes der Samples, welche Peeks sind
	 */
	private int[] inicesOfPeeks() {
		ArrayList<Integer> inicesOfPeeks = new ArrayList<Integer>();
		
		// die Peeks bestimmen
			// die Variable gibt an, ob die derzeit betrachteten Punkte eine steigende Linie bilden oder eine fallende
		boolean rising = true;
		
		for (int sampleNumber = 1; sampleNumber < wavFileValues[0].length; sampleNumber++) {
			if (rising) {
				// gucken, ob mittlerweile ein fallender Punkt erreicht wurde
				if (wavFileValues[0][sampleNumber] < wavFileValues[0][sampleNumber - 1]) {
					// gefundenen Peek merken
					inicesOfPeeks.add(sampleNumber -1);
					
					// Variablen updaten
					rising = false;
				}
			} else {
				// gucken, ob mittlerweile ein steigender Punkt erreicht wurde
				if (wavFileValues[0][sampleNumber] > wavFileValues[0][sampleNumber - 1]) {
					// gefundenen Peek merken
					inicesOfPeeks.add(sampleNumber -1);
					
					// Variablen updaten
					rising = true;
				}
			}
		}
		
		// Ergebnis in ein int[] packen
		int[] result = new int[inicesOfPeeks.size()];
		for (int i = 0; i < inicesOfPeeks.size(); i++) {
			result[i] = inicesOfPeeks.get(i);
		}
		
		return result;
	}

///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}
