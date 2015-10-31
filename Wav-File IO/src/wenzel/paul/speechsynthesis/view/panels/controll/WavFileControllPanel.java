package wenzel.paul.speechsynthesis.view.panels.controll;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import wenzel.paul.speechsynthesis.controller.Main;
import wenzel.paul.speechsynthesis.controller.listener.WavFileControllPanelListener;

/**
 * Die Klasse {@link WavFileControllPanel} [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public class WavFileControllPanel extends JPanel {
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	private JButton loadWavFileButton;
	private JButton saveWavFileButton;
	private JButton deleteSelectedSamplesButton;
	
	private JSpinner zoomSpinner;
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link WavFileControllPanel}. 
	 */
	public WavFileControllPanel(final WavFileControllPanelListener listener) {
		//Datenfelder initialisieren
			//Buttons Konfigurieren
		loadWavFileButton = new JButton("lade WAV-Datei");
		loadWavFileButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				listener.openWavFile();
			}
		});
		
		saveWavFileButton = new JButton("speichere WAV-Datei");
		saveWavFileButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				listener.saveWavFile();
			}
		});
		
		deleteSelectedSamplesButton = new JButton("lösche markierte Samples");
		deleteSelectedSamplesButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				listener.deleteMarkedSamples();
			}
		});
		
			//Zoom Konfigurieren
		zoomSpinner = new JSpinner(new SpinnerNumberModel(Main.WINDOW_WIDTH_PER_FRAME, 0, 10.0f, 0.1f));
		zoomSpinner.setToolTipText("Zoom-Level");
		JSpinner.NumberEditor editor = (JSpinner.NumberEditor)zoomSpinner.getEditor();
        DecimalFormat format = editor.getFormat();
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(1);
        editor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
		zoomSpinner.addChangeListener( new ChangeListener() {
			private float zoomValue = 1.0f;
			
			public void stateChanged(ChangeEvent e) {
				// nur wenn sich der Wert wirklich geändert hat eine Action melden
				if (((Double)((JSpinner)e.getSource()).getValue()).floatValue() != zoomValue) {
					listener.zoom(((Double)((JSpinner)e.getSource()).getValue()).floatValue());
					zoomValue = ((Double)((JSpinner)e.getSource()).getValue()).floatValue();
				}
			}
		});
		
			//JPanel Konfigurieren
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
			//Komponenten Hinzufügen
		add(loadWavFileButton);
		add(saveWavFileButton);
		add(deleteSelectedSamplesButton);
		add(zoomSpinner);
	}
	
	
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	
	
	
	
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////

	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	
	
	
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}
