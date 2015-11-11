package wenzel.paul.speechsynthesis.view.panels.controll;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
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
	private JButton keepSamplesInIntervalButton;
	
	private JTextField firstSampleTextField;
	private JTextField lastSampleTextField;
	
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
		
		keepSamplesInIntervalButton = new JButton("nur Samples im Intervall behalten");
		keepSamplesInIntervalButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				listener.keepSamplesInInterval(Integer.parseInt(firstSampleTextField.getText()), Integer.parseInt(lastSampleTextField.getText()));
			}
		});
		
		firstSampleTextField = new JTextField("first");
		lastSampleTextField = new JTextField("last");
		
			//JPanel Konfigurieren
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
			//Komponenten Hinzufügen
		add(loadWavFileButton);
		add(saveWavFileButton);
		add(attachWavFileButton);
		add(deleteSelectedSamplesButton);
		add(firstSampleTextField);
		add(lastSampleTextField);
		add(keepSamplesInIntervalButton);
	}
	
	
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	
	
	
	
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////

	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	
	
	
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}
