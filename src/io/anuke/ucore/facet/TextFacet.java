package io.anuke.ucore.facet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;

import io.anuke.ucore.core.Draw;

public class TextFacet extends Facet{
	private static GlyphLayout layout = new GlyphLayout();
	public float x,y;
	public String text;
	public BitmapFont font;
	public int align = Align.center;
	public Color color = Color.WHITE;
	
	public TextFacet(BitmapFont font, String text){
		this.text = text;
		this.font = font;
	}
	
	public TextFacet setColor(Color color){
		this.color = color;
		return this;
	}
	
	public TextFacet align(int align){
		this.align = align;
		return this;
	}
	
	@Override
	public TextFacet set(float x, float y){
		if(!MathUtils.isEqual(y, this.y, 0.001f))
			Facets.instance().requestSort();
		this.x = x;
		this.y = y;
		return this;
	}
	
	@Override
	public void draw(){
		font.getData().setScale(1/5f);
		font.setColor(color);
		layout.setText(font, text);
		
		font.draw(Draw.batch(), text, x, y, 0.5f, align, false);
	}

	@Override
	public float getLayer(){
		return y;
	}

	@Override
	public void reset(){
		font = null;
		x = 0;
		y = 0;
		text = "";
		color = Color.WHITE;
		align = Align.center;
	}
}
