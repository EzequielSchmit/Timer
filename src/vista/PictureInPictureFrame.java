package vista;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PictureInPictureFrame extends JFrame {
	private Ventana ventana;
	private boolean activated;
	public PictureInPictureFrame(Ventana ventana) {
		
		this.ventana = ventana;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		setResizable(false);
		
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
