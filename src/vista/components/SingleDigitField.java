package vista.components;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

public class SingleDigitField extends SelectableLabel{
	private final int highestDigit;
	private int digit;
	
	public SingleDigitField(int highestDigit) {
		
		this (0, highestDigit);
	}
	
	public SingleDigitField(int digit, int highestDigit) {
		super();
		this.highestDigit = (0 <= highestDigit && highestDigit <= 9) ? highestDigit : 9;
		setText(( (digit >= 0 && digit <= highestDigit && highestDigit <= 9) ? digit : 0 )+"");
		digit = Integer.parseInt(getText());
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocus();
			}
		});
		
		addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				setForeground(getFirstColor());
				setBackground(getSecondColor());
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				setForeground(getSecondColor());
				setBackground(getFirstColor());
			}
		});
		
		SingleDigitField THIS = this;
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (c == KeyEvent.CHAR_UNDEFINED || !Character.isDigit(c)) {
					//setText("0");
				} else {
					int value = Integer.parseInt(c+"");
					if (value <= highestDigit) {
						setText(value +"");
						THIS.digit = value;
						transferFocus();						
					}
					
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		setFocusable(true);
		setOpaque(true);
	}
	
	public void setDigit(int digit) {
		int newDigit = (digit >= 0 && digit <= highestDigit) ?digit : 0;
		this.digit = newDigit;
		super.setText(newDigit+"");
	}
	
	@Override
	public void setText(String text) {
		try {
			int digit = Integer.parseInt(text);
			int newDigit = (digit >= 0 && digit <= highestDigit) ? digit : 0;
			this.digit = newDigit;
			super.setText(newDigit+"");
		} catch (NumberFormatException e) {
			
		}
	}
	
	public int getDigit() {
		return digit;
	}
}
