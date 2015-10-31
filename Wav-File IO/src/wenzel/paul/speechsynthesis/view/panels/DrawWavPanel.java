package wenzel.paul.speechsynthesis.view.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import wenzel.paul.speechsynthesis.controller.listener.DrawWavPanelListener;
import wenzel.paul.speechsynthesis.model.DrawWavPanelModel;


/**
 * Die Klasse nimmt ein Array von Double-Werten entgegen, welche von einem Stream einer WAV Datei sind
 * und zeichnet die Punkte ins Panel und verbindet sie durch Linien.
 * 
 * @author Paul Wenzel (wenzel.pagb@googlemail.com)
 *
 */
public class DrawWavPanel extends JPanel implements MouseListener, MouseMotionListener {
	
/////////////////////////////////////////////Datenfelder deklarieren////////////////////////////////////////////

	/** das Model vom DrawWavPanel */
	private DrawWavPanelModel model;
	
	// WAV-Graph
	/** das Array enthält alle Punkte, welche insgesamt eine Linie darstellen, die alle Punkte(Werte) verbindet */
	private ArrayList<Point> line;
	/** das Array enthält alle Punkte, welche sich aus den übergebenen Werten ergeben */
	private ArrayList<Point> points;
	
	private Dimension currentSize;
	
	/** die durch die Maus markierte Fläche innerhalb vom Panel */
	private Rectangle markedArea;
	
/////////////////////////////////////////////Konstruktor///////////////////////////////////////////////////////

	public DrawWavPanel(final DrawWavPanelModel model, final DrawWavPanelListener listener) {
		//Datenfelder initialisieren
		this.model = model;
		
		line = new ArrayList<Point>();
		points = new ArrayList<Point>();
		
		setPreferredSize(new Dimension(model.getMinWidth(), model.getMinHeight()));
		currentSize = new Dimension(getWidth(), getHeight());
		
		markedArea = new Rectangle(0, 0, 0, 0);
		
		//MouseListener implementieren, welcher mitbekommt, wenn ein Sample angeklickt wurde
		this.addMouseListener(new MouseAdapter() {
		
			@Override
			public void mouseClicked(MouseEvent event) {
				// prüfen, ob ein Sample angeklickt wurde
				int indexOfSample = 0;
				Iterator<Point> pointsIterator = points.iterator();
				while (pointsIterator.hasNext()) {
					Point point = pointsIterator.next();
					if (event.getPoint().distance(point.getX(), point.getY()) <= model.getPointDiameter()*2) {
						listener.addMarkedSample(indexOfSample);
						return;
					}
					indexOfSample++;
				}
			}
			
		});
		
		//MouseListener implementieren, welche mitbekommt, wenn eine Fläche markiert wird
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				markedArea.setLocation(e.getX(), e.getY());
				
				System.out.println(e.getPoint());
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				markedArea.setSize(e.getX() - (int)markedArea.getLocation().getX(),
						e.getY() - (int)markedArea.getLocation().getY());
				
				// gucken, welche Punkte alle markiert wurden
				ArrayList<Integer> indexOfSelectedSamples = new ArrayList<Integer>();
				
				int indexOfSample = 0;
				Iterator<Point> pointsIterator = points.iterator();
				while (pointsIterator.hasNext()) {
					Point point = pointsIterator.next();
					if (markedArea.contains(point)) {
						indexOfSelectedSamples.add(indexOfSample);
						listener.addMarkedSample(indexOfSample);
					}
					indexOfSample++;
				}
				
				// Controller informieren
				
				// neu zeichnen ohne das Rechteck
				markedArea = new Rectangle(0, 0, 0, 0);
				repaint();
			}
			
		});
		this.addMouseMotionListener(new MouseAdapter() {
			
			@Override
			public void mouseDragged(MouseEvent e) {
				markedArea.setSize(e.getX() - (int)markedArea.getLocation().getX(),
						e.getY() - (int)markedArea.getLocation().getY());
				
				repaint();
			}
			
		});
		
	}

/////////////////////////////////////////////Getter und Setter////////////////////////////////////////////////
	
	
/////////////////////////////////////////////geerbte Methoden/////////////////////////////////////////////////

	@Override
	public void paint(Graphics g) {
		// wenn sich die gewünschte Größe oder die Größe des Fensters verändert hat
		if (getPreferredSize().getWidth() != model.getMinWidth() || getPreferredSize().getHeight() != model.getMinHeight() ||
			currentSize.width != getWidth() || currentSize.height != getHeight())
		{
			setPreferredSize(new Dimension(model.getMinWidth(), model.getMinHeight()));
			
			// die echte Größe anpassen, wenn das Fenster kleiner als gewünscht ist
			int width = getWidth();
			// die Breite soll immer der gewünschten Breite entsprechen
			if (width != model.getMinWidth()) {
				width = model.getMinWidth();
			}
			
			int height = getHeight();
			// die Höhe soll immer maximal sein!
			if (height < model.getMinHeight()) {
				height = model.getMinHeight();
			}
			
			setSize(width, height);
			currentSize = new Dimension(getWidth(), getHeight());
			
			// Punkte neu berechnen
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
		
		// zeichne die Punkte, welche hervorgehoben werden sollen
		g.setColor(model.getHilightColor());
		for (int index : model.getIndexOfSamplesToHilight()) {
			Point point = points.get(index);
			if (viewPort.contains(point)) {
				g.fillRect((int)(point.getX()), (int)(point.getY()), model.getPointDiameter(), model.getPointDiameter());
			}
		}
		
		// zeichne das Rechteck, welches die markierte Fläche verdeutlicht
		g.setColor(Color.black);
		g.drawRect(markedArea.x, markedArea.y, markedArea.width, markedArea.height);
	}
	
	
	// Mouse Listener
	
	 public void mouseClicked(MouseEvent event) {
	    }
	 
	    public void mousePressed(MouseEvent event) {
	    }
	 
	    public void mouseReleased(MouseEvent event) {
	        repaint();
	    }
	 
	    public void mouseEntered(MouseEvent event) {
	    }
	 
	    public void mouseExited(MouseEvent event) {
	    }
	 
	    public void mouseDragged(MouseEvent event) {
	        repaint();
	 
	    }
	 
	    public void mouseMoved(MouseEvent event) {
	        repaint();
	    }
	
/////////////////////////////////////////////Methoden////////////////////////////////////////////////////////

	private void calculatePoints() {
		// alle Punkte, welche es zu zeichnen gilt neu berechnen
			// WAV-Graph Punkte neu berechnen
		line = new ArrayList<Point>();
		points = new ArrayList<Point>();
		
		for (int i = 0; i < model.getWavFile().getNumberOfFrames(); i++) {
			// Punkte der Linie berechnen
			if (i != 0) {
				//beim ersten Punkt noch keine Linie zeichen
				line.addAll(line((int)(((double)getWidth() / (double)model.getWavFile().getNumberOfFrames()) * (i - 1) - (double)model.getPointDiameter() / 2),
						         (int)((double)getHeight()/2 - (double)getHeight()/2 * (double)model.getWavFile().getWavFileValues()[0][i - 1] - (double)model.getPointDiameter() / 2),
						         (int)(((double)getWidth() / (double)model.getWavFile().getNumberOfFrames()) * i - (double)model.getPointDiameter() / 2),
						         (int)((double)getHeight()/2 - (double)getHeight()/2 * (double)model.getWavFile().getWavFileValues()[0][i] - (double)model.getPointDiameter() / 2)));
			}
			
			// Punkt berechnen
			points.add(new Point((int)(((double)getWidth() / (double)model.getWavFile().getNumberOfFrames()) * i - (double)model.getPointDiameter() / 2), (int)((double)getHeight()/2 - (double)getHeight()/2 * (double)model.getWavFile().getWavFileValues()[0][i] - (double)model.getPointDiameter() / 2)));
		}
	}
	
	/**
	 * Bresenham Algorithm
	 * Quelle: http://tech-algorithm.com/articles/drawing-line-using-bresenham-algorithm/
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