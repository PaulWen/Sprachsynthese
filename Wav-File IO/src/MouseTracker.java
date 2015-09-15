
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
 
public class MouseTracker extends JFrame
        implements MouseListener, MouseMotionListener {
 
    private JLabel mousePosition;
    int x, y;
    int x1, x2, y1, y2;
    int w, h;
    private JLabel recStart;
    private JLabel recStop;
    private JLabel cords;
 
    public MouseTracker() {
        super("Rectangle Drawer");
 
        mousePosition = new JLabel();
        mousePosition.setHorizontalAlignment(SwingConstants.CENTER);
//        getContentPane().add(mousePosition, BorderLayout.CENTER);
 
        JLabel text1 = new JLabel();
        text1.setText("At the center the mouse pointer's coordinates will be displayed.");
//        getContentPane().add(text1, BorderLayout.SOUTH);
 
        recStart = new JLabel();
//        getContentPane().add(recStart, BorderLayout.WEST);
 
        recStop = new JLabel();
//        getContentPane().add(recStop, BorderLayout.EAST);
 
        cords = new JLabel();
//        getContentPane().add(cords, BorderLayout.NORTH);
 
 
        addMouseListener(this);
        addMouseMotionListener(this);
 
        setSize(800, 600);
        setVisible(true);
    }
 
    public void mouseClicked(MouseEvent event) {
//        mousePosition.setText("Clicked at [" + event.getX() +
//                ", " + event.getY() + "]");
    }
 
    public void mousePressed(MouseEvent event) {
 
        mousePosition.setText("Pressed at [" + (x1 = event.getX()) +
                ", " + (y1 = event.getY()) + "]");
 
        recStart.setText("Start:  [" + x1 +
                ", " + y1 + "]");
    }
 
    public void mouseReleased(MouseEvent event) {
        mousePosition.setText("Released at [" + (x2 = event.getX()) +
                ", " + (y2 = event.getY()) + "]");
 
        recStop.setText("End:  [" + x2 +
                ", " + y2 + "]");
        repaint();
 
    }
 
    public void mouseEntered(MouseEvent event) {
//        mousePosition.setText("Mouse entered at [" + event.getX() +
//                ", " + event.getY() + "]");
    }
 
    public void mouseExited(MouseEvent event) {
//        mousePosition.setText("Mouse outside window");
    }
 
    public void mouseDragged(MouseEvent event) {
        mousePosition.setText("Dragged at [" + (x = event.getX()) +
                ", " + (y = event.getY()) + "]");
        repaint();
 
    }
 
    public void mouseMoved(MouseEvent event) {
//        mousePosition.setText("Moved at [" + event.getX() +
//                ", " + event.getY() + "]");
//        repaint();
    }
 
    public void paint(Graphics g) {
        super.paint(g);
        g.drawString("Start Rec Here", x1, y1);
        g.drawString("End Rec Here", x, y);
 
        w = x1 - x;
        h = y1 - y;
        w = w * -1;
        h = h * -1;
 
        g.drawRect(x1, y1, w, h);
 
        cords.setText("w = " + w + ", h = " + h);
    }
 
    public static void main(String args[]) {
        MouseTracker application = new MouseTracker();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}