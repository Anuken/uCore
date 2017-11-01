package io.anuke.ucore.cui.style;

import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.cui.Drawable;

public class Style{
	public transient String name;
	public transient String[] typeNames;
	public transient String stateName;
	
	public Drawable background;
	public Color color = new Color(Color.WHITE);
	
	public Float spaceLeft, spaceRight, spaceTop, spaceBottom;
	public Float padLeft, padRight, padTop, padBottom;
	public Boolean fillX, fillY;
	
	public Float transition;
	
	public Float borderRadius;
	public Float borderThickness;
	public Color borderColor = new Color(Color.WHITE);
}
