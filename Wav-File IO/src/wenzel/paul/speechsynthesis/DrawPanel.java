import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;


public class DrawPanel extends JPanel {
	
/////////////////////////////////////////////Datenfelder deklarieren////////////////////////////////////////////

	/** die Werte die als Punkte eingezeichnet werden sollen (komplett verteilt auf die gesammte verfügbare Breite) */
	private double[] values;
	
	private ArrayList<Point> points;
	
	private Color color; //Farbe in welcher gemalt wird
	private int width;
	private int height;
	private int pointDiameter;
	private int zoom;

/////////////////////////////////////////////Konstruktor///////////////////////////////////////////////////////

	public DrawPanel(Color color, int width, int height, int pointDiameter, int zoom, double[] values) {
		//Datenfelder initialisieren
		this.color = color;
		this.width = width;
		this.height = height;
		this.pointDiameter = pointDiameter;
		this.zoom = zoom;
		this.values = values;
		points = new ArrayList<Point>();
		
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.white);
		
		for (int i = 0; i < values.length; i++) {
			if (i == 0) {
				points.addAll(line(0, 0, (int)((width * zoom / values.length) * i - pointDiameter / 2), (int)(height/2 - height/2 * values[i] - pointDiameter / 2)));
			} else {
				points.addAll(line((int)((width * zoom / values.length) * i - 1 - pointDiameter / 2), (int)(height/2 - height/2 * values[i - 1] - pointDiameter / 2), (int)((width * zoom / values.length) * i - pointDiameter / 2), (int)(height/2 - height/2 * values[i] - pointDiameter / 2)));
			}
		}
	}

/////////////////////////////////////////////Getter und Setter////////////////////////////////////////////////
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
/////////////////////////////////////////////geerbte Methoden/////////////////////////////////////////////////

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		g.setColor(color);
		
		for (int i = 0; i < points.size(); i++) {
//			g.fillOval((int)(points.get(i).getX() - pointDiameter / 2), (int)(points.get(i).getY() - pointDiameter / 2), pointDiameter, pointDiameter);
			g.fillRect((int)(points.get(i).getX()), (int)(points.get(i).getY()), pointDiameter, pointDiameter);
		}
		for (int i = 0; i < values.length; i++) {
			g.setColor(Color.BLACK);
			g.fillOval((int)((width * zoom / values.length) * i - pointDiameter / 2), (int)(height/2 - height/2 * values[i] - pointDiameter / 2), 2, 2);
		}
	}

/////////////////////////////////////////////Methoden////////////////////////////////////////////////////////

	/**
	 * Bresenham Algorithm  Quelle: http://tech-algorithm.com/articles/drawing-line-using-bresenham-algorithm/

	 * @param x
	 * @param y
	 * @param x2
	 * @param y2
	 * @return
	 */
	public ArrayList<Point> line(int x,int y,int x2, int y2) {
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
