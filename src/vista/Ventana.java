package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Ventana extends JFrame {
	
	private SoundManager soundManager;
	private Font timerFont;
	private Image imageIcon;
	private ImageIcon volumeIcons[];
	
	public Ventana() {
		
		loadResources();
		
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setMinimumSize(new Dimension(650, 450));
		contentPane.setPreferredSize(new Dimension(650, 450));
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		PanelDeBarraDeHerramientas panelDeBarraDeHerramientas = new PanelDeBarraDeHerramientas(this);
		PanelDeInicio panelDeInicio = new PanelDeInicio(this);
		
		panelDeBarraDeHerramientas.setBackground(new Color(235,235,235));
		panelDeInicio.setBackground(new Color(255,255,255));
		panelDeInicio.setTimerBackground(new Color(220,220,220));
		Color foregroundColor = new Color(70,70,70);
		int val = 80;
		foregroundColor = new Color (val,val,val);
		panelDeInicio.setTimerFieldForeground(foregroundColor);
		panelDeInicio.setButtonsForeground(foregroundColor);
		
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
	
	private void loadResources() {
		imageIcon = new ImageIcon("resources/icons/alarm-clock.png").getImage();
		try {
			timerFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/OpenSans-VariableFont_wdth,wght.ttf"));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(timerFont);
		} catch (FontFormatException | IOException e) {
			System.out.println("error");
			timerFont = getFont();
		}
		soundManager = new SoundManager("resources/sounds/alarm-sound.wav"); //piano-sound.wav or alarm-sound.wav
		volumeIcons = new ImageIcon[] {
				new ImageIcon("resources/icons/volume-off.png"),
				new ImageIcon("resources/icons/volume-down.png"),
				new ImageIcon("resources/icons/volume.png"),
				new ImageIcon("resources/icons/volume-mute.png")
				
		};
	}

}
