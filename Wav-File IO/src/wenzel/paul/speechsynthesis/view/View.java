package wenzel.paul.speechsynthesis.view;


import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import wenzel.paul.speechsynthesis.controller.listener.ViewListener;
import wenzel.paul.speechsynthesis.model.ViewModel;
import wenzel.paul.speechsynthesis.view.panels.DrawWavPanel;
import wenzel.paul.speechsynthesis.view.panels.controll.AnalysisControllPanel;
import wenzel.paul.speechsynthesis.view.panels.controll.PlaybackControllPanel;
import wenzel.paul.speechsynthesis.view.panels.controll.WavFileControllPanel;

public class View extends JFrame {
	
/////////////////////////////////////////////Datenfelder deklarieren////////////////////////////////////////////
	
	private ViewModel model;
	private ViewListener listener;
	
	private JScrollPane scrollPane;
	private JTabbedPane tabbedPane;
	
	private DrawWavPanel drawPanel;
	
	private WavFileControllPanel wavFileControllPanel;
	private PlaybackControllPanel playbackControllPanel;
	private AnalysisControllPanel analysisControllPanel;

/////////////////////////////////////////////Konstruktor///////////////////////////////////////////////////////

	/**
	 * Konstruktor der Klasse View
	 */
	public View(ViewModel model, final ViewListener listener) {
		this.model = model;
		this.listener = listener;
		
		//Panels konfigurieren
		drawPanel = new DrawWavPanel(model, listener);
		scrollPane = new JScrollPane(drawPanel);
		
		wavFileControllPanel = new WavFileControllPanel(listener);
		playbackControllPanel = new PlaybackControllPanel(listener);
		analysisControllPanel = new AnalysisControllPanel(listener);
		
		//JTappedPane erstellen
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Datei", null, wavFileControllPanel, "Laden einer WAV-Datei");
		tabbedPane.addTab("Wiedergabe", null, playbackControllPanel, "WAV-Datei wiedergeben");
		tabbedPane.addTab("Analysieren", null, analysisControllPanel, "die WAV-Datei analysieren");

		//JFrame konfigurieren
		setLayout(new BorderLayout());
		setTitle("WAV-File Analyser");
		setSize(1000, 800);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
			//Komponenten hinzufügen
		add(scrollPane, BorderLayout.CENTER);
		add(tabbedPane, BorderLayout.SOUTH);
						
		setVisible(true);
		repaint();
	}
	
/////////////////////////////////////////////geerbte Methoden/////////////////////////////////////////////////
	
	@Override
	public void repaint() {
		drawPanel.revalidate();
		super.repaint();
	}
	
/////////////////////////////////////////////Methoden////////////////////////////////////////////////////////

	
}
