package wenzel.paul.speechsynthesis.util.wav;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import wenzel.paul.speechsynthesis.model.dataobjects.WavFileDataObject;
import wenzel.paul.speechsynthesis.quellen.WavFileException;

/**
 * Die Klasse {@link ReadWavFile} dient dem Einlesen einer WAV-Datei in ein {@link WavFileDataObject}.
 * Die Klasse ist recht unordentlich, da sie aus vielen Codestücken aus einem anderen Quellcode betseht. 
 * 
 * Die meisten Teile dieser Klasse wurde genommen von:
 * 	A.Greensted
 * 	http://www.labbookpages.co.uk
 *
 * 	File format is based on the information from
 * 	http://www.sonicspot.com/guide/wavefiles.html
 * 	http://www.blitter.com/~russtopia/MIDI/~jglatt/tech/wave.htm
 *
 * 
 * @author Paul Wenzel
 *
 */
public class ReadWavFile {
	
//////////////////////////////////////////////////Konstanten//////////////////////////////////////////////////
	
	private final static int BUFFER_SIZE = 4096;

	private final static int FMT_CHUNK_ID = 0x20746D66;
	private final static int DATA_CHUNK_ID = 0x61746164;
	private final static int RIFF_CHUNK_ID = 0x46464952;
	private final static int RIFF_TYPE_ID = 0x45564157;
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	
	
	
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link ReadWavFile}. 
	 */
	private ReadWavFile() {
		//Datenfelder initialisieren
		
	}
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	
	
	
	
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////
	
	
	
	
	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	/**
	 * Die Methode liest eine WAV-Datei in ein {@link WavFileDataObject} ein.
	 * 
	 * @param wavFile die WAV-Datei, welche eingelesen werden soll
	 * 
	 * @return das {@link WavFileDataObject} zur gewünschten WAV-Datei
	 * 
	 * @throws IOException
	 * @throws WavFileException
	 */
	public static WavFileDataObject openWavFile(File wavFile) throws IOException, WavFileException {
		
		byte[] buffer = new byte[BUFFER_SIZE];
		
		int bytesPerSample = 0; // Number of bytes required to store a single
								// sample
		double floatScale; // Scaling factor used for int <-> float conversion
		double floatOffset; // Offset factor used for int <-> float conversion
		
		
		// gesuchte Informationen
		int numberOfChannels = 0;
		long sampleRate = 0;
		int blockAlign = 0;
		int validBits = 0;
		int numberOfFrames = 0;
		double[][] wavFileValues = null;
		
		// Create a new file input stream for reading file data
		FileInputStream wavFileInputStream = new FileInputStream(wavFile);
		
		// Read the first 12 bytes of the file which represent: Chunktyp,
		// Dateigröße minus acht, Formname
		int bytesRead = wavFileInputStream.read(buffer, 0, 12);
		if (bytesRead != 12)
			throw new WavFileException("Not enough wav file bytes for header");
			
		// Extract parts from the header
		int riffChunkID = getLE(buffer, 0, 4);
		int chunkSize = getLE(buffer, 4, 4);
		int riffTypeID = getLE(buffer, 8, 4);
		
		// Check the header bytes contains the correct signature
		if (riffChunkID != RIFF_CHUNK_ID)
			throw new WavFileException("Invalid Wav Header data, incorrect riff chunk ID");
		if (riffTypeID != RIFF_TYPE_ID)
			throw new WavFileException("Invalid Wav Header data, incorrect riff type ID");
			
		// Check that the file size matches the number of bytes listed in header
		if (wavFile.length() != chunkSize + 8) {
			throw new WavFileException("Header chunk size (" + chunkSize + ") does not match file size ("
					+ wavFile.length() + ")");
		}
		
		boolean foundFormat = false;
		boolean foundData = false;
		
		// Search for the Format and Data Chunks
		while (true) {
			// Read the first 8 bytes of the chunk (ID and chunk size) which
			// represent: Chunk-Typ, Format-Chunk
			bytesRead = wavFileInputStream.read(buffer, 0, 8);
			if (bytesRead == -1)
				throw new WavFileException("Reached end of file without finding format chunk");
			if (bytesRead != 8)
				throw new WavFileException("Could not read chunk header");
				
			// Extract the chunk ID and Size
			long chunkID = getLE(buffer, 0, 4);
			chunkSize = getLE(buffer, 4, 4);
			
			// Word align the chunk size
			// chunkSize specifies the number of bytes holding data. However,
			// the data should be word aligned (2 bytes) so we need to calculate
			// the actual number of bytes in the chunk
			long numChunkBytes = (chunkSize % 2 == 1) ? chunkSize + 1 : chunkSize;
			
			if (chunkID == FMT_CHUNK_ID) {
				// Flag that the format chunk has been found
				foundFormat = true;
				
				// Read the next 16 bytes, wich represent the the header info (=
				// Format-Chunk Daten/fmt-Chunk)
				bytesRead = wavFileInputStream.read(buffer, 0, 16);
				
				// Check this is uncompressed data
				int compressionCode = (int) getLE(buffer, 0, 2);
				if (compressionCode != 1)
					throw new WavFileException("Compression Code " + compressionCode + " not supported");
					
				// Extract the format information
				numberOfChannels = (int) getLE(buffer, 2, 2);
				sampleRate = getLE(buffer, 4, 4);
				blockAlign = (int) getLE(buffer, 12, 2);
				validBits = (int) getLE(buffer, 14, 2);
				
				if (numberOfChannels == 0)
					throw new WavFileException("Number of channels specified in header is equal to zero");
				if (blockAlign == 0)
					throw new WavFileException("Block Align specified in header is equal to zero");
				if (validBits < 2)
					throw new WavFileException("Valid Bits specified in header is less than 2");
				if (validBits > 64)
					throw new WavFileException(
							"Valid Bits specified in header is greater than 64, this is greater than a long can hold");
							
				// Calculate the number of bytes required to hold 1 sample
				bytesPerSample = (validBits + 7) / 8;
				if (bytesPerSample * numberOfChannels != blockAlign) {
					throw new WavFileException(
							"Block Align does not agree with bytes required for validBits and number of channels");
				}
				
				// Account for number of format bytes and then skip over
				// any extra format bytes
				numChunkBytes -= 16;
				if (numChunkBytes > 0)
					wavFileInputStream.skip(numChunkBytes);
					
			} else if (chunkID == DATA_CHUNK_ID) {
				// Check if we've found the format chunk,
				// If not, throw an exception as we need the format information
				// before we can read the data chunk
				if (foundFormat == false)
					throw new WavFileException("Data chunk found before Format chunk");
					
				// Check that the chunkSize (wav data length) is a multiple of
				// the
				// block align (bytes per frame)
				if (chunkSize % blockAlign != 0)
					throw new WavFileException("Data Chunk size is not multiple of Block Align");
					
				// Calculate the number of frames
				numberOfFrames = chunkSize / blockAlign;
				
				// Flag that we've found the wave data chunk
				foundData = true;
				
				break;
				
			} else {
				// If an unknown chunk ID is found, just skip over the chunk
				// data
				wavFileInputStream.skip(numChunkBytes);
			}
		}
		
		// Throw an exception if no data chunk has been found
		if (foundData == false) {
			throw new WavFileException("Did not find a data chunk");
		}
		
		// Calculate the scaling factor for converting to a normalised double
		if (validBits > 8) {
			// If more than 8 validBits, data is signed
			// Conversion required dividing by magnitude of max negative value
			floatOffset = 0;
			floatScale = 1 << (validBits - 1);
		} else {
			// Else if 8 or less validBits, data is unsigned
			// Conversion required dividing by max positive value
			floatOffset = -1;
			floatScale = 0.5 * ((1 << validBits) - 1);
		}
		
		
		// WAV-Daten laden
		try {
			wavFileValues = readFrames(wavFileInputStream, numberOfChannels, numberOfFrames, floatScale,
					floatOffset, bytesPerSample);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		// Close the wavFile
		wavFileInputStream.close();
		wavFileInputStream = null;
		
		// ein WavFileDataObject erstellen
		return new WavFileDataObject(sampleRate, blockAlign, validBits, wavFileValues);
	}
	
	/**
	 * Get little endian data from local buffer.
	 * 
	 * @param buffer
	 * @param pos
	 * @param numBytes
	 * @return
	 */
	private static int getLE(byte[] buffer, int pos, int numBytes) {
		numBytes--;
		pos += numBytes;
		
		int val = buffer[pos] & 0xFF;
		
		for (int b = 0; b < numBytes; b++) {
			val = (val << 8) + (buffer[--pos] & 0xFF);
		}
		
		return val;
	}
	
	/**
	 * Die Methode list alle Frames aus einer WAV-Datei aus und gibt sich nach Channels sortiert aus.
	 * Die Werte liegen zwichen 0 und 1!
	 * 
	 * @param wavFileInputStream Der InputStream über welchen die WAV-Datei ausgelesen werden kann. <br>
	 * 								Es ist wichtig, dass die nächsten im Inputstream kommenden Bytes ausschließlich Frames beschreiben!
	 * @param numberOfChannels die Anzahl an Channels, welche die WAV-Datei hat
	 * @param numberOfFrames die Anzahl an Frames pro Channel, welche die WAV-Datei hat
	 * @param floatScale 
	 * @param floatOffset
	 * @param bytesPerSample aus wie vielen Bytes ein Sample besteht
	 * 
	 * @return Ein 2D-Double-Array mit allen Frames: [Channel][Frame]<br>
	 * 			Die Werte liegen zwichen 0 und 1!
	 * 
	 * @throws IOException
	 */
	private static double[][] readFrames(	FileInputStream wavFileInputStream, int numberOfChannels,
											int numberOfFrames, double floatScale, double floatOffset,
											int bytesPerSample) throws IOException
	{
											
		double[][] values = new double[numberOfChannels][numberOfFrames];
		int offset = 0;
		
		// für alle Frames
		for (long frame : readSample(wavFileInputStream, bytesPerSample, numberOfFrames, numberOfChannels)) {
			// für alle Channels
			for (int channel = 0; channel < numberOfChannels; channel++) {
				values[channel][offset] = floatOffset
						+ (double) frame / floatScale;
			}
			offset++;
		}
		
		return values;
	}
	
	/**
	 * Die Methode liest alle Samples/Frames als Longs aus und sortiert dabei auch nicht nach Channels.
	 * 
	 * 
	 * @param wavFileInputStream Der InputSTream über welchen die WAV-Datei ausgelesen werden kann. <br>
	 * 								Es ist wichtig, dass die nächsten im Inputstream kommenden Bytes ausschließlich Frames beschreiben!
	 * @param bytesPerSample aus wie vielen Bytes ein Sample besteht
	 * @param numberOfChannels die Anzahl an Channels, welche die WAV-Datei hat
	 * @param numberOfFrames die Anzahl an Frames pro Channel, welche die WAV-Datei hat
	 * 
	 * @return in einem Long-Array alle Frames hintereinander aufgelistet
	 * 
	 * @throws IOException
	 */
	private static long[] readSample(FileInputStream wavFileInputStream, int bytesPerSample, int numberOfFrames, int numberOfChannels)
			throws IOException
	{
		long[] samples = new long[numberOfFrames * numberOfChannels];
		
		byte[] buffer = new byte[BUFFER_SIZE]; // Local buffer used for IO
		int sampleNumber = 0;
		// die komplette WAV-Datei-Daten auslesen
		int bytesRead= wavFileInputStream.read(buffer, 0, BUFFER_SIZE);
		while (bytesRead != -1) {
			// den kompleten Buffer abarbeiten
			for (int i = 0; i < bytesRead; i += bytesPerSample) {
				// ein komplettes Sample auslesen
				for (int j = 0; j < bytesPerSample; j++) {
					// lese den nächsten Frame aus dem Buffer aus
					int oneByte = buffer[i + j];
					if (j < bytesPerSample - 1 || bytesPerSample == 1) {
						oneByte &= 0xFF;
					}
					samples[sampleNumber] += oneByte << (j * 8);
				}
				sampleNumber++;
			}
			bytesRead= wavFileInputStream.read(buffer, 0, BUFFER_SIZE);
		}
		
		return samples;
	}
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
}
