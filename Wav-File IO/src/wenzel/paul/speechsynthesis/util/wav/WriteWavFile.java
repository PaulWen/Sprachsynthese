package wenzel.paul.speechsynthesis.util.wav;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import wenzel.paul.speechsynthesis.model.dataobjects.WavFileDataObject;
import wenzel.paul.speechsynthesis.quellen.WavFileException;

/**
 * Die Klasse {@link WriteWavFile} [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public class WriteWavFile {
//////////////////////////////////////////////////Konstanten//////////////////////////////////////////////////
	
	private final static int BUFFER_SIZE = 4096;
	
	private final static int FMT_CHUNK_ID = 0x20746D66;
	private final static int DATA_CHUNK_ID = 0x61746164;
	private final static int RIFF_CHUNK_ID = 0x46464952;
	private final static int RIFF_TYPE_ID = 0x45564157;

/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	
	
	
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link WriteWavFile}. 
	 */
	public WriteWavFile() {
		//Datenfelder initialisieren
		
	}
	
	
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	
	
	
	
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////
	
	
	
	
	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	/**
	 * Die Methode speichert ein {@link WavFileDataObject} in einer belibigen Datei.
	 * 
	 * @param wavFile das {@link WavFileDataObject}, welches in einer Datei abgespeichert werden soll
	 * @param outputFile die Datei, in welcher das {@link WavFileDataObject} gespeichert werden soll
	 */
	public static void writeWavFile(WavFileDataObject wavFile, File outputFile) {
		try {
			
			byte[] buffer = new byte[BUFFER_SIZE];
			
			double floatScale;				// Scaling factor used for int <-> float conversion				
			double floatOffset;			// Offset factor used for int <-> float conversion				
			
			
			boolean wordAlignAdjust;
			
			
			// HEADER DER WAV-DATEI SCHREIBEN
			
			// Sanity check arguments
			if (wavFile.getNumberOfChannels() < 1 || wavFile.getNumberOfChannels() > 65535) throw new WavFileException("Illegal number of channels, valid range 1 to 65536");
			if (wavFile.getNumberOfFrames() < 0) throw new WavFileException("Number of frames must be positive");
			if (wavFile.getValidBits() < 2 || wavFile.getValidBits() > 65535) throw new WavFileException("Illegal number of valid bits, valid range 2 to 65536");
			if (wavFile.getSampleRate() < 0) throw new WavFileException("Sample rate must be positive");

			// Create output stream for writing data
			FileOutputStream wavFileOutputStream = new FileOutputStream(outputFile);

			// Calculate the chunk sizes
			long dataChunkSize = wavFile.getBlockAlign() * wavFile.getNumberOfFrames();
			long mainChunkSize =	4 +	// Riff Type
										8 +	// Format ID and size
										16 +	// Format data
										8 + 	// Data ID and size
										dataChunkSize;
			
			
			// Chunks must be word aligned, so if odd number of audio data bytes
			// adjust the main chunk size
			if (dataChunkSize % 2 == 1) {
				mainChunkSize += 1;
				wordAlignAdjust = true;
			}
			else {
				wordAlignAdjust = false;
			}

			// Set the main chunk size
			putLE(RIFF_CHUNK_ID,	buffer, 0, 4);
			putLE(mainChunkSize,	buffer, 4, 4);
			putLE(RIFF_TYPE_ID,	buffer, 8, 4);

			// Write out the header
			wavFileOutputStream.write(buffer, 0, 12);

			// Put format data in buffer
			long averageBytesPerSecond = wavFile.getSampleRate() * wavFile.getBlockAlign();

			putLE(FMT_CHUNK_ID, buffer, 0, 4); // Chunk ID
			putLE(16, buffer, 4, 4); // Chunk Data Size
			putLE(1, buffer, 8, 2); // Compression Code (Uncompressed)
			putLE(wavFile.getNumberOfChannels(), buffer, 10, 2); // Number of channels
			putLE(wavFile.getSampleRate(), buffer, 12, 4); // Sample Rate
			putLE(averageBytesPerSecond, buffer, 16, 4); // Average Bytes Per Second
			putLE(wavFile.getBlockAlign(), buffer, 20, 2); // Block Align
			putLE(wavFile.getValidBits(), buffer, 22, 2); // Valid Bits
			
			// Write Format Chunk
			wavFileOutputStream.write(buffer, 0, 24);

			// Start Data Chunk
			putLE(DATA_CHUNK_ID,				buffer, 0, 4);		// Chunk ID
			putLE(dataChunkSize,				buffer, 4, 4);		// Chunk Data Size

			// Write Format Chunk
			wavFileOutputStream.write(buffer, 0, 8);
			
			// Calculate the scaling factor for converting to a normalised
			// double
			if (wavFile.getValidBits() > 8) {
				// If more than 8 validBits, data is signed
				// Conversion required multiplying by magnitude of max positive
				// value
				floatOffset = 0;
				floatScale = Long.MAX_VALUE >> (64 - wavFile.getValidBits());
			} else {
				// Else if 8 or less validBits, data is unsigned
				// Conversion required dividing by max positive value
				floatOffset = 1;
				floatScale = 0.5 * ((1 << wavFile.getValidBits()) - 1);
			}

			
			
			// DATEN IN DIE WAV-DATEI SCHREIBEN
			
			// Frames schreiben
			writeFrames(wavFileOutputStream, wavFile.getWavFileValues(), floatScale, floatOffset, wavFile.getBytesPerSample());
			
			// Close the wavFile
				// If an extra byte is required for word alignment, add it to the end
			if (wordAlignAdjust) wavFileOutputStream.write(0);
			wavFileOutputStream.close();
			wavFileOutputStream = null;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
		}
	}
	
	/**
	 * Put little endian data from local buffer.
	 * 
	 * @param val
	 * @param buffer
	 * @param pos
	 * @param numBytes
	 */
	private static void putLE(long val, byte[] buffer, int pos, int numBytes) {
		for (int b = 0; b < numBytes; b++) {
			buffer[pos] = (byte) (val & 0xFF);
			val >>= 8;
			pos++;
		}
	}
	
	/**
	 * Die Methode schreibt alle WAV-File-Values in eine WAV-Datei.
	 * Der Header wird dabei nicht geschrieben, sondern nur die Werte!
	 * 
	 * @param wavFileOutputStream Der OutputStream in welchen die WAV-Datei geschrieben werden kann. <br>
	 * 								Es ist wichtig, dass die nächsten im OutputStream kommenden Bytes ausschließlich Frames beschreiben sollen!
	 * 								(Der Header soll somit bereits in die Datei geschrieben wurden sein!)
	 * @param wavFileValues Ein 2D-Double-Array mit allen Frames: [Channel][Frame]<br>
	 * 						Die Werte liegen zwichen 0 und 1!
	 * @param floatScale
	 * @param floatOffset
	 * @param bytesPerSample aus wie vielen Bytes ein Sample besteht
	 * 
	 * @throws IOException
	 */
	private static void writeFrames(FileOutputStream wavFileOutputStream, double[][] wavFileValues,
									double floatScale, double floatOffset, int bytesPerSample)
									throws IOException
	{
											
		byte[] buffer = new byte[BUFFER_SIZE]; // Local buffer used for IO
		int bufferPointer = 0; // Points to the current position in local buffer
		
		// für alle Frames
		for (int frameNumber = 0; frameNumber < wavFileValues[0].length; frameNumber++) {
			// für alle Channels
			for (int channelNumber = 0; channelNumber < wavFileValues.length; channelNumber++) {
				
				long sample = (long) (floatScale * (floatOffset + wavFileValues[channelNumber][frameNumber]));
				
				// Sample in Bytes umwandeln und in die Datei schreiben
				for (int b = 0; b < bytesPerSample; b++) {
					if (bufferPointer == BUFFER_SIZE) {
						wavFileOutputStream.write(buffer, 0, BUFFER_SIZE);
						bufferPointer = 0;
					}
					
					buffer[bufferPointer] = (byte) (sample & 0xFF);
					sample >>= 8;
					bufferPointer++;
				}
				
			}
		}
		
		// falls der Buffer noch nicht leer ist, die letzten bytes ebenfalls in die Datei laden
		if (bufferPointer > 0) wavFileOutputStream.write(buffer, 0, bufferPointer);
	}
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}
