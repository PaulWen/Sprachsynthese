package wenzel.paul.speechsynthesis.model;

import wenzel.paul.speechsynthesis.model.dataobjects.WavFileDataObject;

public interface WavFilePlayerModel {

	/**
	 * Die Methode gibt das {@link WavFileDataObject} aus, welches die aktuelle WAV-Datei repräsentiert.
	 * 
	 * @return die aktuelle WAV-Datei als {@link WavFileDataObject}
	 */
	public WavFileDataObject getWavFile();
	
	/**
	 * Die Methode gibt 2 Indizes der Sample aus, zwischen welchen Samples die Wiedergabe erfolgen soll. 
	 * 
	 * @return das Start- und End-Sample für die Wiedergabe
	 */
	public int[] getIndexOfStartAndEndSample();
}
