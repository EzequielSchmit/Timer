package vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Ventana extends JFrame {
	public Ventana() {
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(new PanelDeInicio(this));
//		setUndecorated(true); //funcion a implementar
		setTitle("Temporizador");
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setMinimumSize(getPreferredSize());
		setIconImage(new ImageIcon("resources/alarm-clock.png").getImage());
	}

}
