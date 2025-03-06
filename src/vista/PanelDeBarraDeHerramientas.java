package vista;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.SoundManager;

public class PanelDeBarraDeHerramientas extends JPanel {
	
	boolean volumeMuted = false;
	public PanelDeBarraDeHerramientas(Ventana ventana) {
		
		JSlider volumeControl = new JSlider(0, 100, 80);
		volumeControl.setOpaque(false);
		volumeControl.setToolTipText("Volumen de la alarma");
		ImageIcon[] volumeIcons = ventana.getVolumeIcons();
		JLabel volumeIcon = new JLabel(volumeIcons[2]);
		SoundManager soundManager = ventana.getSoundManager();
		soundManager.setVolume(80);
		volumeControl.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int value = volumeControl.getValue();
				soundManager.setVolume(value);
				if (value == 0)
					volumeIcon.setIcon(volumeIcons[0]);
				else if (value < 40)
					volumeIcon.setIcon(volumeIcons[1]);
				else 
					volumeIcon.setIcon(volumeIcons[2]);
			}
		});
		
		volumeIcon.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (volumeMuted) {
					int value = volumeControl.getValue();
					soundManager.setVolume(value);
					if (value == 0)
						volumeIcon.setIcon(volumeIcons[0]);
					else if (value < 40)
						volumeIcon.setIcon(volumeIcons[1]);
					else 
						volumeIcon.setIcon(volumeIcons[2]);
					volumeMuted = false;
				} else {
					soundManager.setVolume(0);
					volumeIcon.setIcon(volumeIcons[3]);
					volumeMuted = true;
				}
			}
		});
		
		add(volumeIcon);
		add(volumeControl);
		
	}
	
}
