package wenzel.paul.speechsynthesis.view.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import wenzel.paul.speechsynthesis.controller.Main;
import wenzel.paul.speechsynthesis.controller.listener.DrawWavPanelListener;
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
	/** das Array enthält alle Punkte, welche sich aus den übergebenen Werten ergeben */
	private ArrayList<Point> points;
	
	/** die durch die Maus markierte Fläche innerhalb vom Panel */
	private Rectangle markedArea;
	
/////////////////////////////////////////////Konstruktor///////////////////////////////////////////////////////

	public DrawWavPanel(final DrawWavPanelModel model, final DrawWavPanelListener listener) {
		//Datenfelder initialisieren
		this.model = model;
		
		points = new ArrayList<Point>();
		
		setPreferredSize(new Dimension(model.getMinWidth(), model.getMinHeight()));
		
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
		
		// MouseListener implementieren, welche mitbekommen, wenn eine Fläche markiert wird
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				markedArea.setLocation(e.getX(), e.getY());
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
		// die Variable gibt Auskunft darüber ob bereits eine Schicht gezeichnet wurde auf die Zeichenfläche
		boolean layerDrawn = false;
		
		Graphics2D g2 = (Graphics2D) g;
		
		// Größe vom Fenster setzen
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
		
		//falls die komplette WAV-Datei gezeichnet werden soll diese Zeichnen
		if (model.isShowWavFilePresentation()) {
			// Punkte neu berechnen
			calculatePoints();
			
			// zeichne die Linie
			g.setColor(model.getLineColor());
			for (int i = 0; i < points.size() - 1; i++) {
				if (viewPort.contains(points.get(i)) || viewPort.contains(points.get(i + 1))) {
					g2.setStroke(new BasicStroke(model.getPointDiameter()));
					int strokeThignessCorrection = model.getPointDiameter() / 2;
					g2.draw(new Line2D.Float(points.get(i).x + strokeThignessCorrection, points.get(i).y + strokeThignessCorrection, points.get(i + 1).x + strokeThignessCorrection, points.get(i + 1).y + strokeThignessCorrection));
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
			layerDrawn = true;
		}
		
		//falls die Peeks besonders hervorgehoben werden diese hervorheben
		if (model.isShowPeeksPresentation()) {
			// wenn davor bereits eine Schicht gezeichnet wurde, dann zuvor eine transparente Schicht einfügen, welche die Schichten etwas trennt voneinander
			if (layerDrawn) {
				// zeichne eine transparente Zwischenschicht
				g.setColor(model.getTransparentBackgroundColor());
				g.fillRect(0, 0, getWidth(), getHeight());
			}
			
			// zeichne die Linie
			g.setColor(Color.RED);
			g2.setStroke(new BasicStroke(model.getPointDiameter()));
			int strokeThignessCorrection = model.getPointDiameter() / 2;
			for (int i = 0; i < model.getWavFile().getInicesOfPeeks().length - 1; i++) {
				int indexOfPeek = model.getWavFile().getInicesOfPeeks()[i];
				int indexOfNextPeek = model.getWavFile().getInicesOfPeeks()[i + 1];
				if (viewPort.contains(points.get(indexOfPeek)) || viewPort.contains(points.get(indexOfNextPeek))) {
					g2.draw(new Line2D.Float(points.get(indexOfPeek).x + strokeThignessCorrection, points.get(indexOfPeek).y + strokeThignessCorrection, points.get(indexOfNextPeek).x + strokeThignessCorrection, points.get(indexOfNextPeek).y + strokeThignessCorrection));
				}
			}

			// zeichne die einzelnen Punkte
			g.setColor(model.getPointColor());
			for (int indexOfPoint : model.getWavFile().getInicesOfPeeks()) {
				if (viewPort.contains(points.get(indexOfPoint))) {
					g.fillOval((int)(points.get(indexOfPoint).getX()), (int)(points.get(indexOfPoint).getY()), model.getPointDiameter(), model.getPointDiameter());
				}
			}
			
			layerDrawn = true;
		}
		
		// falls die Polygone eingezeichnet werden sollen
		if (model.isShowPolygonsOfPeeksPresentation()) {
			// wenn davor bereits eine Schicht gezeichnet wurde, dann zuvor eine transparente Schicht einfügen, welche die Schichten etwas trennt voneinander
			if (layerDrawn) {
				// zeichne eine transparente Zwischenschicht
				g.setColor(model.getTransparentBackgroundColor());
				g.fillRect(0, 0, getWidth(), getHeight());
			}
			
			// zeichne die Linie
			g.setColor(Color.BLUE);
			g2.setStroke(new BasicStroke(model.getPointDiameter()));
			int strokeThignessCorrection = model.getPointDiameter() / 2;
			for (int i = 0; i < model.getWavFile().getInicesOfPeeks().length - 1; i++) {
				int indexOfPeek = model.getWavFile().getInicesOfPeeks()[i];
				int indexOfNextPeek = model.getWavFile().getInicesOfPeeks()[i + 1];
				if (viewPort.contains(points.get(indexOfPeek)) || viewPort.contains(points.get(indexOfNextPeek))) {
					// Polygone um die Punkte zeichnen
					Polygon polygon = Main.calculatePolygonOfTwoPoints(points.get(indexOfPeek).x, points.get(indexOfPeek).y, points.get(indexOfNextPeek).x, points.get(indexOfNextPeek).y, 20);
					// Polygon aufgrund der dicke der Linie ungefähr korrigieren für das Zeichnen
					for (int j = 0; j < polygon.xpoints.length; j++) {
						polygon.xpoints[j] += strokeThignessCorrection;
					}
					for (int j = 0; j < polygon.ypoints.length; j++) {
						polygon.ypoints[j] += strokeThignessCorrection;
					}
					// dem Polygon sagen, dass sich seine Punkte verändert haben
					polygon.invalidate();
					
					// das Polygon zeichnen
					g.drawPolyline(polygon.xpoints, polygon.ypoints, polygon.npoints);
				}
			}
			
			// zeichne die einzelnen Punkte
			g.setColor(model.getPointColor());
			for (int indexOfPoint : model.getWavFile().getInicesOfPeeks()) {
				if (viewPort.contains(points.get(indexOfPoint))) {
					g.fillOval((int)(points.get(indexOfPoint).getX()), (int)(points.get(indexOfPoint).getY()), model.getPointDiameter(), model.getPointDiameter());
				}
			}
			
			layerDrawn = true;
			
		}
		
		// zeichne das Rechteck, welches die markierte Fläche verdeutlicht, wenn es eine width oder hight > 0 hat
		if (markedArea.width > 0 || markedArea.height > 0) {
			g.setColor(Color.black);
			g.drawRect(markedArea.x, markedArea.y, markedArea.width, markedArea.height);
		}
		
	}
	
/////////////////////////////////////////////Methoden////////////////////////////////////////////////////////

	private void calculatePoints() {
		// WAV-Graph Punkte neu berechnen
		points = new ArrayList<Point>();
		
		for (int i = 0; i < model.getWavFile().getNumberOfFrames(); i++) {
			// Punkt berechnen
			points.add(new Point((int)(((double)getWidth() / (double)model.getWavFile().getNumberOfFrames()) * i - (double)model.getPointDiameter() / 2), (int)((double)getHeight()/2 - (double)getHeight()/2 * (double)model.getWavFile().getWavFileValues()[0][i] - (double)model.getPointDiameter() / 2)));
		}
	}
	
}