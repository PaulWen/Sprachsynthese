package wenzel.paul.speechsynthesis.model.dataobjects;

import java.util.ArrayList;

/**
 * Die Klasse {@link PatternInWavFileDataObject2} dient dem Festhalten, wo ein bestimmtes Muster (Original Pattern)
 * innerhalb einer WAV-Datei überall gefunden wurde.
 * 
 * 
 * @author Paul Wenzel
 *
 */
public class PatternInWavFileDataObject2 {
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	/** die Länge vom Pattern */
	private int patternLength;
	/** der Index vom ersten Sample des eigentlichen Patterns */
	private int originalPatternFirstSampleIndex;
	/** der Index vom letzten Sample des eigentlichen Patterns */
	private int originalPatternLastSampleIndex;
	
	// wo dieses Pattern noch gefunden wurde
	private ArrayList<Double> precisions = new ArrayList<Double>();
	private ArrayList<Integer> firstSampleIndices = new ArrayList<Integer>();
	private ArrayList<Integer> lastSampleIndices = new ArrayList<Integer>();
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link PatternInWavFileDataObject2}.
	 * 
	 * @param firstSampleIndex der Index vom Sample, bei welchem das Muster beginnt (inklusive)
	 * @param lastSampleIndex der Index vom Sample, bei welchem das Muster endet (inklusive)
	 */
	public PatternInWavFileDataObject2(int firstSampleIndex, int lastSampleIndex) {
		//Datenfelder initialisieren
		originalPatternFirstSampleIndex = firstSampleIndex;
		originalPatternLastSampleIndex = lastSampleIndex;
		patternLength = lastSampleIndex - firstSampleIndex + 1;
	}
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	public int getOriginalPatternFirstSampleIndex() {
		return originalPatternFirstSampleIndex;
	}
	
	public int getOriginalPatternLastSampleIndex() {
		return originalPatternLastSampleIndex;
	}
	
	public int getPatternLength() {
		return patternLength;
	}
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////
	
	@Override
	public String toString() {
		StringBuffer string = new StringBuffer();
		string.append("#########################################################################");
		string.append("aller Muster ungefiltert:" + numberOfOccurrences() + "\n");
		string.append("Quoten:" + "\n");
		for (int i = 0; i < numberOfOccurrences(); i++) {
			string.append(getPrecision(i) + ";\t");
			string.append(getFirstSampleIndex(i) + " bis ");
			string.append(getLastSampleIndex(i) + "\n");
		}
		
		return string.toString();
	}
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	/**
	 * Die Methode dient dem Hinzufügen eines Vorkommens.
	 * 
	 * @param firstSampleIndex
	 * @param lastSampleIndex
	 * @param precision
	 */
	public void addOccurrence(int firstSampleIndex, int lastSampleIndex, double precision) {
		precisions.add(precision);
		firstSampleIndices.add(firstSampleIndex);
		lastSampleIndices.add(lastSampleIndex);
	}
	
	/**
	 * Die Methode dient dem Löschen eines bereits eingefügtem Vorkommen.
	 * 
	 * @param index
	 */
	public void removeOccurrence(int index) {
		precisions.remove(index);
		firstSampleIndices.remove(index);
		lastSampleIndices.remove(index);
	}
	
	public int numberOfOccurrences() {
		return firstSampleIndices.size();
	}
	
	public double getPrecision(int index) {
		return precisions.get(index);
	}
	
	public int getFirstSampleIndex(int index) {
		return firstSampleIndices.get(index);
	}
	
	public int getLastSampleIndex(int index) {
		return lastSampleIndices.get(index);
	}
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}