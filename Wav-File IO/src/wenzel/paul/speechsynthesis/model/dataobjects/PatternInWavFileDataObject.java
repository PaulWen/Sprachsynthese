package wenzel.paul.speechsynthesis.model.dataobjects;

import java.util.ArrayList;

/**
 * Die Klasse {@link PatternInWavFileDataObject} dient dem Festhalten 
 * 
 * 
 * @author Paul Wenzel
 *
 */
public class PatternInWavFileDataObject {
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	private ArrayList<Double> precisions = new ArrayList<Double>();
	private ArrayList<Integer> firstSampleIndices = new ArrayList<Integer>();
	private ArrayList<Integer> lastSampleIndices = new ArrayList<Integer>();
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link PatternInWavFileDataObject}.
	 */
	public PatternInWavFileDataObject() {
		//Datenfelder initialisieren
	}
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	
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