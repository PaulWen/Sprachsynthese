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
import wenzel.paul.speechsynthesis.model.WavFileControllPanelModel;

/**
 * Die Klasse {@link WavFileControllPanel} [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public class WavFileControllPanel extends JPanel {
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	private WavFileControllPanelModel model;
	
	private JButton loadWavFileButton;
	private JButton saveWavFileButton;
	private JButton attachWavFileButton;
	private JButton deleteSelectedSamplesButton;
	
	private JSpinner zoomSpinner;
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link WavFileControllPanel}. 
	 */
	public WavFileControllPanel(final WavFileControllPanelModel model, final WavFileControllPanelListener listener) {
		//Datenfelder initialisieren
		this.model = model;
		
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
		
		attachWavFileButton = new JButton("WAV-Datei anhängen");
		attachWavFileButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				listener.attachWavFile();
			}
		});

		deleteSelectedSamplesButton = new JButton("lösche markierte Samples");
		deleteSelectedSamplesButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				listener.deleteMarkedSamples();
			}
		});
		
			//Zoom Konfigurieren
		zoomSpinner = new JSpinner(new SpinnerNumberModel(model.getCurrentZoomLevel(), 0, Main.MAX_WINDOW_WIDTH_PER_FRAME, Main.MIN_WINDOW_WIDTH_PER_FRAME));
		zoomSpinner.setToolTipText("Zoom-Level");
		JSpinner.NumberEditor editor = (JSpinner.NumberEditor)zoomSpinner.getEditor();
        DecimalFormat format = editor.getFormat();
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(1);
        editor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
		zoomSpinner.addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				// nur wenn sich der Wert wirklich geändert hat eine Action melden
				if (((Double)((JSpinner)e.getSource()).getValue()).floatValue() != model.getCurrentZoomLevel()) {
					listener.zoom(((Double)((JSpinner)e.getSource()).getValue()).floatValue());
				}
			}
		});
		
			//JPanel Konfigurieren
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
			//Komponenten Hinzufügen
		add(loadWavFileButton);
		add(saveWavFileButton);
		add(attachWavFileButton);
		add(deleteSelectedSamplesButton);
		add(zoomSpinner);
	}
	
	
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	
	
	
	
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////

	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	
	
	
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}
