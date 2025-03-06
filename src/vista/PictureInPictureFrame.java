package vista;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PictureInPictureFrame extends JFrame {
	private Ventana ventana;
	private boolean activated;
	private Point initialClick;
	protected boolean dragging;
	public PictureInPictureFrame(Ventana ventana) {
		
		this.ventana = ventana;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		setResizable(false);
		setUndecorated(true);
		
    }
	
	private void configurarListenersParaArrastre() {
		addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                dragging = true;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    Point currentPoint = e.getLocationOnScreen();
                    setLocation(currentPoint.x - initialClick.x, currentPoint.y - initialClick.y);
                }
            }
        });
	}
	
	public void activate() {
		setVisible(true);
		setContentPane(ventana.getPanelDeInicio());
		pack();
		setLocationRelativeTo(null);
		activated = true;
	}
	
	public void deactivate() {
		setContentPane(new JPanel());
		setVisible(false);
		activated = false;
	}

	public boolean isPipActivated() {
		return activated;
	}
	
}
