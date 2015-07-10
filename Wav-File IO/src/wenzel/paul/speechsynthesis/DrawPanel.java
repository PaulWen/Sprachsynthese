package wenzel.paul.speechsynthesis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;


public class DrawPanel extends JPanel {
	
/////////////////////////////////////////////Datenfelder deklarieren////////////////////////////////////////////

	/** die Werte die als Punkte eingezeichnet werden sollen (komplett verteilt auf die gesammte verfügbare Breite) */
	private double[] values;
	
	private ArrayList<Point> line;
	private ArrayList<Point> points;
	
	private Color backgroundColor; //Farbe vom Hintergrund
	private Color lineColor; //Farbe in welcher die Linie gemalt wird
	private Color pointColor; //Farbe in welcher die unkte gemalt werden
	private double width;
	private double height;
	private double pointDiameter;
	
	int i = 0;

/////////////////////////////////////////////Konstruktor///////////////////////////////////////////////////////

	public DrawPanel(Color background, Color lineColor, Color pointColor, int pointDiameter, double[] values) {
		//Datenfelder initialisieren
		this.backgroundColor = background;
		this.lineColor = lineColor;
		this.pointColor = pointColor;
		this.width = values.length;
		this.height = 500;
		this.pointDiameter = pointDiameter;
		this.values = values;

		
		setPreferredSize(new Dimension((int)width, (int)height));
		calculatePoints();
	}

/////////////////////////////////////////////Getter und Setter////////////////////////////////////////////////
	
	public void setWidth(int width) {
		this.width = width;
		setPreferredSize(new Dimension((int)width, (int)height));
		calculatePoints();
		repaint();
	}
	
	public void setHeight(int height) {
		this.height = height;
		setPreferredSize(new Dimension((int)width, (int)height));
		calculatePoints();
		repaint();
	}
	
/////////////////////////////////////////////geerbte Methoden/////////////////////////////////////////////////

	@Override
	public void paint(Graphics g) {
		Rectangle viewPort = new Rectangle(0, 0, (int)width, (int)height);
		
		// wenn sich das Panel in einem ScrollPane befindet, dann soll nur der sichtbare Bereich gezeichnet werden, um performanter zu sein
		if (getParent() instanceof JViewport) {
			viewPort = new Rectangle((int)((JViewport)getParent()).getViewPosition().getX(),
							   		 (int)((JViewport)getParent()).getViewPosition().getY(),
							   	   	 ((JScrollPane)((JViewport)getParent()).getParent()).getWidth(),
							   	     ((JScrollPane)((JViewport)getParent()).getParent()).getHeight());
			
			// die Höhe an die Höhe vom Parent angleichen
			setHeight(((JScrollPane)((JViewport)getParent()).getParent()).getHeight() - 20);
		}
		
		super.paint(g);
			
		// zeichne den Hintergrund
		g.setColor(backgroundColor);
		g.fillRect(0, 0, (int)width, (int)height);
		
		// zeichne die Linie
		g.setColor(lineColor);
		for (Point point : line) {
			if (viewPort.contains(point)) {
				g.fillRect((int)(point.getX()), (int)(point.getY()), (int)pointDiameter, (int)pointDiameter);
			}
		}
		
		// zeichne die einzelnen Punkte
		g.setColor(pointColor);
		for (Point point : points) {
			if (viewPort.contains(point)) {
				g.fillOval((int)(point.getX()), (int)(point.getY()), (int)pointDiameter, (int)pointDiameter);
			}
		}
	}

/////////////////////////////////////////////Methoden////////////////////////////////////////////////////////

	private void calculatePoints() {
		line = new ArrayList<Point>();
		points = new ArrayList<Point>();
		
		for (int i = 0; i < values.length; i++) {
			// Punkte der Linie berechnen
			if (i != 0) {
				//beim ersten Punkt noch keine Linie zeichen
				line.addAll(line((int)((width / values.length) * i - 1 - pointDiameter / 2), (int)(height/2 - height/2 * values[i - 1] - pointDiameter / 2), (int)((width / values.length) * i - pointDiameter / 2), (int)(height/2 - height/2 * values[i] - pointDiameter / 2)));
			}
			
			// Punkt berechnen
			points.add(new Point((int)((width / values.length) * i - pointDiameter / 2), (int)(height/2 - height/2 * values[i] - pointDiameter / 2)));
		}
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
