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
	
	private JSpinner zoomSpinner;
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link WavFileControllPanel}. 
	 */
	public WavFileControllPanel(final WavFileControllPanelListener listener) {
		//Datenfelder initialisieren
	
		
			//Buttons Konfigurieren
		loadWavFileButton = new JButton("lade WAV-Datei");
		loadWavFileButton.setActionCommand("ladeLabyrinthButton");
		loadWavFileButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				listener.openWavFile();
			}
		});
		
			//Zoom Konfigurieren
		zoomSpinner = new JSpinner(new SpinnerNumberModel(1.0f, 0, 10.0f, 0.1f));
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
		add(zoomSpinner);
	}
	
	
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	
	
	
	
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////

	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	
	
	
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}
