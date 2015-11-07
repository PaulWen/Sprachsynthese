package wenzel.paul.speechsynthesis.view.panels.controll;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

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
		
			//JPanel Konfigurieren
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
			//Komponenten Hinzufügen
		add(wavFilePresentationCheckBox);
		add(peeksPresentationCheckBox);
		add(polygonsOfPeeksPresentationCheckBox);
	}
	
//////////////////////////////////////////////Getter und Setter//////////////////////////////////////////////
	
	
	
	
	
	
///////////////////////////////////////////////geerbte Methoden//////////////////////////////////////////////

	
	
//////////////////////////////////////////////////Methoden///////////////////////////////////////////////////
	
	
	
	
	
///////////////////////////////////////////////Innere Klassen////////////////////////////////////////////////	
	
	
	
	
}
