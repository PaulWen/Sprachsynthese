package wenzel.paul.speechsynthesis.view.panels.controll;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import wenzel.paul.speechsynthesis.controller.Main;
import wenzel.paul.speechsynthesis.controller.listener.PresentationLayersControllPanelListener;
import wenzel.paul.speechsynthesis.model.PresentationLayersControllPanelModel;

/**
 * Die Klasse {@link PresentationLayersControllPanel} [...]
 * 
 * 
 * @author Paul Wenzel
 *
 */
public class PresentationLayersControllPanel extends JPanel {
	
/////////////////////////////////////////////////Datenfelder/////////////////////////////////////////////////
	
	/** das Model vom DrawWavPanel */
	private PresentationLayersControllPanelModel model;
	
	private JCheckBox wavFilePresentationCheckBox;
	private JCheckBox peeksPresentationCheckBox;
	private JCheckBox polygonsOfPeeksPresentationCheckBox;
	private JCheckBox secondWavFilePresentationCheckBox;

	private JButton openSecondWavFilePresentationButton;
	private JButton swapFirstAndSecondWavFileButton;
	
	private JSpinner zoomSpinner;
	
/////////////////////////////////////////////////Konstruktor/////////////////////////////////////////////////
	
	/**
	 * Der Konstruktor der Klasse {@link PresentationLayersControllPanel}. 
	 */
	public PresentationLayersControllPanel(final PresentationLayersControllPanelModel model, final PresentationLayersControllPanelListener listener) {
		//Datenfelder initialisieren
		this.model = model;
				
			//Buttons Konfigurieren
		wavFilePresentationCheckBox = new JCheckBox("WAV-Datei");
		wavFilePresentationCheckBox.setSelected(model.isShowWavFilePresentation());
		wavFilePresentationCheckBox.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent e) {
				listener.showWavFilePresentation(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		
		peeksPresentationCheckBox = new JCheckBox("Peeks");
		peeksPresentationCheckBox.setSelected(model.isShowPeeksPresentation());
		peeksPresentationCheckBox.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent e) {
				listener.showPeeksPresentation(e.getStateChange() == ItemEvent.SELECTED);
			}
		});

		polygonsOfPeeksPresentationCheckBox = new JCheckBox("Polygone der Peeks");
		polygonsOfPeeksPresentationCheckBox.setSelected(model.isShowPolygonsOfPeeksPresentation());
		polygonsOfPeeksPresentationCheckBox.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent e) {
				listener.showPolygonsOfPeeksPresentation(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		
		secondWavFilePresentationCheckBox = new JCheckBox("zweite WAV-Datei");
		secondWavFilePresentationCheckBox.setSelected(model.isShowSecondWavFilePresentation());
		secondWavFilePresentationCheckBox.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent e) {
				listener.showSecondWavFilePresentation(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		
		openSecondWavFilePresentationButton = new JButton("öffne zweite WAV-Datei");
		openSecondWavFilePresentationButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				listener.openSecondWavFile();
			}
		});

		swapFirstAndSecondWavFileButton = new JButton("erste und Zweite WAV-Datei tauschen");
		swapFirstAndSecondWavFileButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				listener.swapFirstAndSecondWavFile();
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
		add(wavFilePresentationCheckBox);
		add(peeksPresentationCheckBox);
		add(polygonsOfPeeksPresentationCheckBox);
		add(secondWavFilePresentationCheckBox);
		add(openSecondWavFilePresentationButton);
		add(swapFirstAndSecondWavFileButton);
		add(zoomSpinner);
	}
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	
	
	
	
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////

	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	
	
	
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}
