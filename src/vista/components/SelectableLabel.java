package vista.components;

import java.awt.Color;

import javax.swing.JLabel;

public class SelectableLabel extends JLabel{
	
	private Color firstColor, secondColor;
	
	public SelectableLabel() {
		this("");
	}
	
	public SelectableLabel(String text) {
		this(Color.DARK_GRAY.darker(), Color.WHITE, text);
	}
	
	public SelectableLabel(Color firstColor, Color secondColor) {
		this(firstColor, secondColor, "");
		
	}
		
	public SelectableLabel(Color firstColor, Color secondColor, String text) {
		super(text);
		this.setFirstColor(firstColor);
		this.setSecondColor(secondColor);
	}

	public Color getFirstColor() {
		return firstColor;
	}

	public void setFirstColor(Color firstColor) {
		this.firstColor = firstColor;
	}

	public Color getSecondColor() {
		return secondColor;
	}

	public void setSecondColor(Color secondColor) {
		this.secondColor = secondColor;
	}
	
	
	

	
}
