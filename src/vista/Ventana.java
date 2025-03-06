package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.SoundManager;

public class Ventana extends JFrame {
	
	private SoundManager soundManager;
	private Font timerFont;
	private Image imageIcon;
	private ImageIcon volumeIcons[];
	private ImageIcon pipIcon;
	private PanelDeInicio panelDeInicio;
	private PanelDeBarraDeHerramientas panelDeBarraDeHerramientas;
	private PictureInPictureFrame pipFrame;
	private JPanel contentPane;
	public Ventana() {
		
		loadResources();
		
		contentPane = new JPanel(new BorderLayout());
		contentPane.setMinimumSize(new Dimension(650, 450));
		contentPane.setPreferredSize(new Dimension(650, 450));
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		panelDeBarraDeHerramientas = new PanelDeBarraDeHerramientas(this);
		panelDeInicio = new PanelDeInicio(this);
		
		
		panelDeBarraDeHerramientas.setBackground(new Color(235,235,235));
		panelDeInicio.setBackground(new Color(255,255,255));
		panelDeInicio.setTimerBackground(new Color(220,220,220));
		Color foregroundColor = new Color(70,70,70);
		int val = 80;
		foregroundColor = new Color (val,val,val);
		panelDeInicio.setTimerFieldForeground(foregroundColor);
		panelDeInicio.setButtonsForeground(foregroundColor);
		pipFrame = new PictureInPictureFrame(this);
		
		contentPane.add(panelDeBarraDeHerramientas, BorderLayout.NORTH);
		contentPane.add(panelDeInicio, BorderLayout.CENTER);
		
		setContentPane(contentPane);
		
//		setUndecorated(true); //funcion a implementar
		setTitle("Temporizador");
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setMinimumSize(getPreferredSize());
		setIconImage(imageIcon);
		
		panelDeInicio.configureKeyBindings();
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}
	
	public Font getTimerFont() {
		return timerFont;
	}
	
	public ImageIcon[] getVolumeIcons() {
		return volumeIcons;
	}
	
	public ImageIcon getPipIcon() {
		return pipIcon;
	}
	
	private void loadResources() {
		try {
			timerFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/OpenSans-VariableFont_wdth,wght.ttf"));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(timerFont);
		} catch (FontFormatException | IOException e) {
			System.out.println("error");
			timerFont = getFont();
		}
		soundManager = new SoundManager("resources/sounds/alarm-sound.wav"); //piano-sound.wav or alarm-sound.wav
		imageIcon = new ImageIcon("resources/icons/alarm-clock.png").getImage();
		volumeIcons = new ImageIcon[] {
				new ImageIcon("resources/icons/volume-off.png"),
				new ImageIcon("resources/icons/volume-down.png"),
				new ImageIcon("resources/icons/volume.png"),
				new ImageIcon("resources/icons/volume-mute.png")
				
		};
		pipIcon = new ImageIcon("resources/icons/pip.png");
	}
	
	public void setPictureInPictureActivated(boolean activated) {
		if (activated)
			activatePictureInPicture();
		else
			deactivatePictureInPicture();
	}
	
	private void activatePictureInPicture() {
		contentPane.remove(panelDeInicio);
		setVisible(false);
		pipFrame.activate();
	}
	
	private void deactivatePictureInPicture() {
		pipFrame.deactivate();
		contentPane.add(panelDeInicio, BorderLayout.CENTER);
		setVisible(true);
	}

	public boolean isPipActivated() {
		return pipFrame.isPipActivated();
	}
	
	public PanelDeInicio getPanelDeInicio() {
		return panelDeInicio;
	}

	public PanelDeBarraDeHerramientas getPanelDeBarraDeHerramientas() {
		return panelDeBarraDeHerramientas;
	}
	
	public PictureInPictureFrame getPipFrame() {
		return pipFrame;
	}
	

}
