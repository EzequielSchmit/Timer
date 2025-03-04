package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import vista.components.SingleDigitField;

public class PanelDeInicio extends JPanel {

	JButton botonConfiguraciones;
	Font timerFont, buttonFont;
	JButton startButton, stopButton;
	JPanel fieldsPanel, buttonsPanel;
	Timer timer;
	SingleDigitField[] fields;
	SoundManager soundManager;
	int timeLeftInSeconds = 0;
	int initialTimeLeftInSeconds = 0;
	boolean fieldModificationsEnabled = true;
	
	public PanelDeInicio(Ventana ventana) {
		setMinimumSize(new Dimension(650, 450));
		setPreferredSize(new Dimension(650, 450));
		loadResources();
		timerFont = timerFont.deriveFont(65f).deriveFont(Font.BOLD);
		buttonFont = timerFont.deriveFont(19f);//.deriveFont(Font.PLAIN);
		
		setLayout(new GridBagLayout());
		JPanel timerPanel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		timerPanel.setLayout(new GridBagLayout());
		timerPanel.setBackground(new Color(180,180,180));
		gbc.insets.top = gbc.insets.right = gbc.insets.left = 20;
		
		fieldsPanel = new JPanel();
		buttonsPanel = new JPanel();
		fieldsPanel.setOpaque(false);
		buttonsPanel.setOpaque(false);
		
		loadFields(fieldsPanel);
		loadButtons(buttonsPanel);
		
		gbc.gridy = 0;
		timerPanel.add(fieldsPanel, gbc);
		gbc.insets.top = 5;
		gbc.insets.bottom = 20;
		gbc.gridy++;
		timerPanel.add(buttonsPanel, gbc);
		
		setVisible(true);
		add(timerPanel);
		
		configureButtons();
		configureTimer();
		configureKeyBindings(ventana);
		
	}
	
	private void configureKeyBindings(Ventana ventana) {
		InputMap inputMap = ventana.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = ventana.getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "startPressed");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "startPressed");
        
        actionMap.put("startPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "stopPressed");
        actionMap.put("stopPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });
	}
	
	private void setTimer(int timeInSeconds) {
		int hours = timeInSeconds/3600;
		timeInSeconds -= hours*3600;
		int minutes = timeInSeconds/60;
		timeInSeconds -= minutes*60;
		int seconds = timeInSeconds;
		
		fields[5].setText( ((seconds)%10)+"" );
		fields[4].setText( ((seconds)/10)+"" );
		fields[3].setText( ((minutes)%10)+"" );
		fields[2].setText( ((minutes)/10)+"" );
		fields[1].setText( ((hours)%10)+"" );
		fields[0].setText( ((hours)/10)+"" );
	}
	
	private int getTimeOfFieldsInSeconds() {
		int time = 0;
		time += fields[5].getDigit();
		time += fields[4].getDigit()*10;
		time += fields[3].getDigit()*60;
		time += fields[2].getDigit()*600;
		time += fields[1].getDigit()*3600;
		time += fields[0].getDigit()*36000;
		return time;
	}
	
	private void configureTimer() {
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timeLeftInSeconds--;
				setTimer(timeLeftInSeconds);
				if (timeLeftInSeconds <= 0) {
					startButton.setText("START");
					startButton.setEnabled(false);
					timer.stop();
					soundManager.startSound();
				}
			}

		});
	}
	
	private void start() {
		if (!startButton.getText().equalsIgnoreCase("pause")) {
			if (getTimeOfFieldsInSeconds() > 0) {						
				timeLeftInSeconds = getTimeOfFieldsInSeconds();
				if (startButton.getText().equalsIgnoreCase("start"))
					initialTimeLeftInSeconds = timeLeftInSeconds;
				startButton.setText("PAUSE");
				stopButton.setEnabled(true);
				timer.start();
				setFieldModificationsEnabled(false);
			}
		} else {
			startButton.setText("CONTINUE");
			stopButton.setEnabled(false);
			timer.stop();
		}
	}
	
	private void stop() {
		if (soundManager.isRinging()) {
			soundManager.stopSound();
		} else {
			timer.stop();
			setTimer(initialTimeLeftInSeconds);
		}
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
		setFieldModificationsEnabled(true);
		startButton.setText("START");
		fields[0].requestFocus();
	}

	private void configureButtons() {
		stopButton.setEnabled(false);
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				start();
			}
		});
		
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		
		
	}

	private void loadButtons(JPanel buttonsPanel) {
		startButton = new JButton("START");
		stopButton = new JButton("STOP");
		JButton[] buttons = new JButton[] {
				startButton, stopButton
		};
		
		for (JButton b : buttons) {
			b.setFont(buttonFont);
			b.setPreferredSize(new Dimension(140, b.getPreferredSize().height));
			b.setBackground(Color.WHITE);
			b.setForeground(Color.BLACK);
			b.setFocusPainted(false);
			for (KeyListener kl : b.getKeyListeners())
				b.removeKeyListener(kl);
		}
		
		buttonsPanel.setLayout(new BorderLayout());
		buttonsPanel.add(startButton, BorderLayout.WEST);
		buttonsPanel.add(stopButton, BorderLayout.EAST);
		buttonsPanel.setPreferredSize(new Dimension(fieldsPanel.getPreferredSize().width, buttonsPanel.getPreferredSize().height));
	}

	private void loadFields(JPanel fieldsPanel) {
		
		List<JLabel> listOfLabels = new ArrayList<JLabel>(8);
		fields = new SingleDigitField[] {
				new SingleDigitField(9),
				new SingleDigitField(9),
				new SingleDigitField(1, 5),
				new SingleDigitField(5, 9),
				new SingleDigitField(5),
				new SingleDigitField(9)
		};
		listOfLabels.add(fields[0]);
		listOfLabels.add(fields[1]);
		listOfLabels.add(new JLabel(":"));
		listOfLabels.add(fields[2]);
		listOfLabels.add(fields[3]);
		listOfLabels.add(new JLabel(":"));
		listOfLabels.add(fields[4]);
		listOfLabels.add(fields[5]);
		
		Vector<Component> order = new Vector<Component>();
		
		
		fieldsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		for (JLabel f : listOfLabels) {
			f.setBackground(Color.WHITE);
			f.setOpaque(true);
			f.setFont(timerFont);
			fieldsPanel.add(f);
			if (f instanceof SingleDigitField)
				order.add(f);
		}

		FocusTraversalPolicy ftp = new MyOwnFocusTraversalPolicy(order);
		fieldsPanel.setFocusTraversalPolicy(ftp);
		fieldsPanel.setFocusCycleRoot(true);
	}

	private void loadResources() {
		try {
			timerFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/OpenSans-VariableFont_wdth,wght.ttf"));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(timerFont);
			soundManager = new SoundManager("resources/sound.wav");
			
		} catch (FontFormatException | IOException e) {
			// timerFont = Font.getFont("Arial");
			timerFont = getFont();
		}
	}
	
	private void setFieldModificationsEnabled(boolean enabled) {
		fieldModificationsEnabled = enabled;
		for (SingleDigitField field : fields)
			field.setFocusable(enabled);
	}
	
	private boolean areFieldModificationsEnabled() {
		return fieldModificationsEnabled;
	}

	public static class MyOwnFocusTraversalPolicy extends FocusTraversalPolicy {
		Vector<Component> order;

		public MyOwnFocusTraversalPolicy(Vector<Component> order) {
			this.order = new Vector<Component>(order.size());
			this.order.addAll(order);
		}

		public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
			int idx = (order.indexOf(aComponent) + 1) % order.size();
			return order.get(idx);
		}

		public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
			int idx = order.indexOf(aComponent) - 1;
			if (idx < 0) {
				idx = order.size() - 1;
			}
			return order.get(idx);
		}

		public Component getDefaultComponent(Container focusCycleRoot) {
			return order.get(0);
		}

		public Component getLastComponent(Container focusCycleRoot) {
			return order.lastElement();
		}

		public Component getFirstComponent(Container focusCycleRoot) {
			return order.get(0);
		}
	}

}
