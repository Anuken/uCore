package io.anuke.ucore.cui.style;

import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.cui.Drawable;

public class Style{
	public transient String name;
	public transient String[] typeNames;
	public Drawable background;
	public Border border;
	public Color color = Color.WHITE;
	public Float spaceLeft, spaceRight, spaceTop, spaceBottom;
	public Float padLeft, padRight, padTop, padBottom;
	public Boolean fillX, fillY;
}
