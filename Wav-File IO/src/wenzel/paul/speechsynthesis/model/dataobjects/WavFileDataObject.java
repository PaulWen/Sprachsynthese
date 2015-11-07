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
		System.out.println("hey!");
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
		
		// die gefundenen Peeks bereinigen um, alle Peeks, welche nur sehr sehr klein sind
		// ("inicesOfPeeks.size() - 1" damit das letzte Sample der WAV-Datei auf jeden Fall erhalten bleibt!)
		for (int i = 1; i < inicesOfPeeks.size() - 1; i++) {
			// wenn der Abstand zwischen zwei Peeks kleiner als 0.1 ist, so wird dieses nicht als Peek gewertet und daher der zweite Peek gelöscht
			if (Math.abs(wavFileValues[0][inicesOfPeeks.get(i - 1)] - wavFileValues[0][inicesOfPeeks.get(i)]) < 0.01) {
				// zu kleinen Peek löschen
				inicesOfPeeks.remove(i);
				
				// Zählvariable i um 1 zurücksetzen
				i--;
			}
		}
		
		// Ergebnis in ein int[] packen
		int[] result = new int[inicesOfPeeks.size()];
		for (int i = 0; i < inicesOfPeeks.size(); i++) {
			result[i] = inicesOfPeeks.get(i);
		}
		
		return result;
	}
	
	/**
	 * Die Methode berechnet ein Array von Samples, welche genau auf den Verbindungslinien der Peeks liegen.
	 * 
	 * @return ein Array mit Samples, welche genau auf den Verbindungslinien der Peeks liegen
	 */
	private double[][] calculateSamplesBetweenPeeks() {
		double[][] samples = new double[1][wavFileValues[0].length];
		
		// alle Peeks in das Array laden
		for (int peekIndex : inicesOfPeeks) {
			samples[0][peekIndex] = wavFileValues[0][peekIndex];
		}
		
		// die fehlenden Sample Werte berechnen
		// Vorgehen:
		// 	1) zwei Peeks nehmen (x-Wert = Index und y-Wert = Sample-Wert)
		// 	2) Geraden-Funktion aufstellen zwischen den beiden Punkte
		//	3) fehlende y-Werte bestimmen
		for (int peekIndex = 1; peekIndex < inicesOfPeeks.length; peekIndex++) {
			// m = (y2 - y1) / (x1 - x2)
			double m = (wavFileValues[0][inicesOfPeeks[peekIndex]] - wavFileValues[0][inicesOfPeeks[peekIndex - 1]]) / (inicesOfPeeks[peekIndex] - inicesOfPeeks[peekIndex - 1]);
			// n = y - m * x
			double n = (wavFileValues[0][inicesOfPeeks[peekIndex]]) - m * inicesOfPeeks[peekIndex];
			
			for (int x = inicesOfPeeks[peekIndex - 1] + 1; x < inicesOfPeeks[peekIndex]; x++) {
				// f(x) = m * x + n
				samples[0][x] = m * x + n;
			}
		}
		
		setWavFileValues(samples);
		
		return samples;
	}

///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}
