package wenzel.paul.speechsynthesis.view.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import wenzel.paul.speechsynthesis.model.DrawWavPanelModel;


/**
 * Die Klasse nimmt ein Array von Double-Werten entgegen, welche von einem Stream einer WAV Datei sind
 * und zeichnet die Punkte ins Panel und verbindet sie durch Linien.
 * 
 * @author Paul Wenzel (wenzel.pagb@googlemail.com)
 *
 */
public class DrawWavPanel extends JPanel {
	
/////////////////////////////////////////////Datenfelder deklarieren////////////////////////////////////////////

	/** das Model vom DrawWavPanel */
	private DrawWavPanelModel model;
	
	// WAV-Graph
	/** das Array enthält alle Punkte, welche insgesamt eine Linie darstellen, die alle Punkte(Werte) verbindet */
	private ArrayList<Point> line;
	/** das Array enthält alle Punkte, welche sich aus den übergebenen Werten ergeben */
	private ArrayList<Point> points;
	
	private Dimension currentSize;
	
/////////////////////////////////////////////Konstruktor///////////////////////////////////////////////////////

	public DrawWavPanel(DrawWavPanelModel model) {
		//Datenfelder initialisieren
		this.model = model;
		
		line = new ArrayList<Point>();
		points = new ArrayList<Point>();
		
		setPreferredSize(new Dimension(model.getMinWidth(), model.getMinHeight()));
		currentSize = new Dimension(getWidth(), getHeight());
	}

/////////////////////////////////////////////Getter und Setter////////////////////////////////////////////////
	
	
/////////////////////////////////////////////geerbte Methoden/////////////////////////////////////////////////

	@Override
	public void paint(Graphics g) {
		// prüfen, ob sich die Größe vom Panel verändert HAT
		if (currentSize.getWidth() != getWidth() || currentSize.getHeight()!= getHeight()) {
			currentSize = new Dimension(getWidth(), getHeight());
			calculatePoints();
		}
		
		//prüfen, ob sich die Minimale Größe vom Panel verändern SOLL
		if (getPreferredSize().getWidth() != model.getMinWidth() || getPreferredSize().getHeight() != model.getMinHeight()) {
			setPreferredSize(new Dimension(model.getMinWidth(), model.getMinHeight()));
			calculatePoints();
		}
		
		// Größe vom ViewPort angeben
			// der ViePort ist maximal so groß, wie das Panel selber
		Rectangle viewPort = new Rectangle(0, 0, getWidth(), getHeight());
			// wenn sich das Panel in einem ScrollPane befindet, dann soll nur der sichtbare Bereich gezeichnet werden, um performanter zu sein
		if (getParent() instanceof JViewport) {
			viewPort = new Rectangle((int)((JViewport)getParent()).getViewPosition().getX(),
							   		 (int)((JViewport)getParent()).getViewPosition().getY(),
							   	   	 ((JScrollPane)((JViewport)getParent()).getParent()).getWidth(),
							   	     ((JScrollPane)((JViewport)getParent()).getParent()).getHeight());
		}
		
		// zeichnen
		super.paint(g);
			
		// zeichne den Hintergrund
		g.setColor(model.getBackgroundColor());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// zeichne die Linie
		g.setColor(model.getLineColor());
		for (Point point : line) {
			if (viewPort.contains(point)) {
				g.fillRect((int)(point.getX()), (int)(point.getY()), model.getPointDiameter(), model.getPointDiameter());
			}
		}
		
		// zeichne die einzelnen Punkte
		g.setColor(model.getPointColor());
		for (Point point : points) {
			if (viewPort.contains(point)) {
				g.fillOval((int)(point.getX()), (int)(point.getY()), model.getPointDiameter(), model.getPointDiameter());
			}
		}
	}

/////////////////////////////////////////////Methoden////////////////////////////////////////////////////////

	private void calculatePoints() {
		// alle Punkte, welche es zu zeichnen gilt neu berechnen
			// WAV-Graph Punkte neu berechnen
		line = new ArrayList<Point>();
		points = new ArrayList<Point>();
		
		for (int i = 0; i < model.getWavFileValues().length; i++) {
			// Punkte der Linie berechnen
			if (i != 0) {
				//beim ersten Punkt noch keine Linie zeichen
				line.addAll(line((int)(((double)getWidth() / (double)model.getWavFileValues().length) * (i - 1) - (double)model.getPointDiameter() / 2),
						         (int)((double)getHeight()/2 - (double)getHeight()/2 * (double)model.getWavFileValues()[i - 1] - (double)model.getPointDiameter() / 2),
						         (int)(((double)getWidth() / (double)model.getWavFileValues().length) * i - (double)model.getPointDiameter() / 2),
						         (int)((double)getHeight()/2 - (double)getHeight()/2 * (double)model.getWavFileValues()[i] - (double)model.getPointDiameter() / 2)));
			}
			
			// Punkt berechnen
			points.add(new Point((int)(((double)getWidth() / (double)model.getWavFileValues().length) * i - (double)model.getPointDiameter() / 2), (int)((double)getHeight()/2 - (double)getHeight()/2 * (double)model.getWavFileValues()[i] - (double)model.getPointDiameter() / 2)));
		}
		
		System.out.println(model.getWavFileValues().length);
	}
	
	/**
	 * Bresenham Algorithm  Quelle: http://tech-algorithm.com/articles/drawing-line-using-bresenham-algorithm/
	 * 
	 * @param x
	 * @param y
	 * @param x2
	 * @param y2
	 * @return
	 */
	private ArrayList<Point> line(int x,int y,int x2, int y2) {
		ArrayList<Point> ergebnis = new ArrayList<Point>();
	    
	    int w = x2 - x ;
	    int h = y2 - y ;
	    int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
	    if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
	    if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
	    if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
	    int longest = Math.abs(w) ;
	    int shortest = Math.abs(h) ;
	    if (!(longest>shortest)) {
	        longest = Math.abs(h) ;
	        shortest = Math.abs(w) ;
	        if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
	        dx2 = 0 ;            
	    }
	    int numerator = longest >> 1 ;
	    for (int i=0;i<=longest;i++) {
	    	ergebnis.add(new Point(x, (int)Math.round(y)));
	        numerator += shortest ;
	        if (!(numerator<longest)) {
	            numerator -= longest ;
	            x += dx1 ;
	            y += dy1 ;
	        } else {
	            x += dx2 ;
	            y += dy2 ;
	        }
	    }
	    
	    return ergebnis;
	}	
}