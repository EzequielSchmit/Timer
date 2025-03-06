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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import vista.components.SelectableLabel;
import vista.components.SingleDigitField;

public class PanelDeInicio extends JPanel {

	Ventana ventana;
	Font timerFont, buttonFont;
	JButton startButton, stopButton;
	JLabel pipButton; 
	JPanel fieldsPanel, buttonsPanel, timerPanel;
	Timer timer;
	List<SelectableLabel> listOfFieldLabels;
	List<JButton> listOfButtons;
	SingleDigitField[] digitFields;
	int timeLeftInSeconds = 0;
	int initialTimeLeftInSeconds = 0;
	
	public PanelDeInicio(Ventana ventana) {
		this.ventana = ventana;
		timerFont = ventana.getTimerFont().deriveFont(66f).deriveFont(Font.BOLD);
		buttonFont = timerFont.deriveFont(17f);
		
		setLayout(new GridBagLayout());
		timerPanel = new JPanel();
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
		
	}
	
	public void configureKeyBindings() {
		configureKeyBindings(ventana);
		configureKeyBindings(ventana.getPipFrame());
	}
	
	private void configureKeyBindings(JFrame frame) {
		InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = frame.getRootPane().getActionMap();

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
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pipMode");
        actionMap.put("pipMode", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	ventana.setPictureInPictureActivated(!ventana.isPipActivated());
            }
        });
        
        
	}
	
	private void setTimer(int timeInSeconds) {
		int hours = timeInSeconds/3600;
		timeInSeconds -= hours*3600;
		int minutes = timeInSeconds/60;
		timeInSeconds -= minutes*60;
		int seconds = timeInSeconds;
		
		digitFields[5].setText( ((seconds)%10)+"" );
		digitFields[4].setText( ((seconds)/10)+"" );
		digitFields[3].setText( ((minutes)%10)+"" );
		digitFields[2].setText( ((minutes)/10)+"" );
		digitFields[1].setText( ((hours)%10)+"" );
		digitFields[0].setText( ((hours)/10)+"" );
	}
	
	private int getTimeOfFieldsInSeconds() {
		int time = 0;
		time += digitFields[5].getDigit();
		time += digitFields[4].getDigit()*10;
		time += digitFields[3].getDigit()*60;
		time += digitFields[2].getDigit()*600;
		time += digitFields[1].getDigit()*3600;
		time += digitFields[0].getDigit()*36000;
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
					ventana.getSoundManager().startSound();
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
		if (ventana.getSoundManager().isRinging()) {
			ventana.getSoundManager().stopSound();
		} else {
			timer.stop();
			setTimer(initialTimeLeftInSeconds);
		}
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
		setFieldModificationsEnabled(true);
		startButton.setText("START");
		digitFields[0].requestFocus();
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
		
		pipButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (pipButton.isOpaque())
					pipButton.setBackground(new Color(235,235,235));
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				pipButton.setBackground(new Color(245,245,245));
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				pipButton.setOpaque(false);
				pipButton.setBackground(new Color(255,255,255,0));
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				pipButton.setBackground(new Color(235,235,235));
				pipButton.setOpaque(true);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				pipButton.setOpaque(false);
				pipButton.setBackground(new Color(255,255,255,0));
				ventana.setPictureInPictureActivated(!ventana.isPipActivated());
			}
		});
		
		
	}

	private void loadButtons(JPanel buttonsPanel) {
		pipButton = new JLabel(ventana.getPipIcon());
		startButton = new JButton("START");
		stopButton = new JButton("STOP");
		listOfButtons = new ArrayList<>();
		listOfButtons.add(startButton);
		listOfButtons.add(stopButton);
		
		
		
		for (JButton b : listOfButtons) {
			b.setFont(buttonFont);
			b.setPreferredSize(new Dimension(120, b.getPreferredSize().height));
			b.setBackground(Color.WHITE);
			b.setForeground(Color.BLACK);
			b.setFocusPainted(false);
			for (KeyListener kl : b.getKeyListeners())
				b.removeKeyListener(kl);
		}
		
		buttonsPanel.setLayout(new BorderLayout());
		buttonsPanel.add(startButton, BorderLayout.WEST);
		buttonsPanel.add(pipButton, BorderLayout.CENTER);
		buttonsPanel.add(stopButton, BorderLayout.EAST);
		buttonsPanel.setPreferredSize(new Dimension(fieldsPanel.getPreferredSize().width, buttonsPanel.getPreferredSize().height));
	}

	private void loadFields(JPanel fieldsPanel) {
		listOfFieldLabels = new ArrayList<>(8);
		digitFields = new SingleDigitField[] {
				new SingleDigitField(9),
				new SingleDigitField(9),
				new SingleDigitField(1, 5),
				new SingleDigitField(5, 9),
				new SingleDigitField(5),
				new SingleDigitField(9)
		};
		listOfFieldLabels.add(digitFields[0]);
		listOfFieldLabels.add(digitFields[1]);
		listOfFieldLabels.add(new SelectableLabel(":"));
		listOfFieldLabels.add(digitFields[2]);
		listOfFieldLabels.add(digitFields[3]);
		listOfFieldLabels.add(new SelectableLabel(":"));
		listOfFieldLabels.add(digitFields[4]);
		listOfFieldLabels.add(digitFields[5]);
		
		Vector<Component> order = new Vector<Component>();
		
		
		fieldsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		for (JLabel f : listOfFieldLabels) {
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

	private void setFieldModificationsEnabled(boolean enabled) {
		for (SingleDigitField field : digitFields)
			field.setFocusable(enabled);
	}
	
	public void setTimerBackground(Color color) {
		timerPanel.setBackground(color);
	}
	
	public void setTimerFieldBackground(Color color) {
		for (JLabel f : listOfFieldLabels)
			f.setBackground(color);
	}
	
	public void setTimerFieldForeground(Color color) {
		for (SelectableLabel f : listOfFieldLabels) {
			f.setForeground(color);
			f.setFirstColor(color);
		}
	}

	public void setButtonsForeground(Color color) {
		for (JButton b : listOfButtons)
			b.setForeground(color);
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
