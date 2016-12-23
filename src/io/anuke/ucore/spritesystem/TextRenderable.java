package io.anuke.ucore.spritesystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;

public class TextRenderable extends Renderable{
	private static GlyphLayout layout = new GlyphLayout();
	public float x,y;
	public String text;
	public BitmapFont font;
	public int align = Align.center;
	public Color color = Color.WHITE;
	
	public TextRenderable(BitmapFont font, String text){
		this.text = text;
		this.font = font;
	}
	
	public TextRenderable setColor(Color color){
		this.color = color;
		return this;
	}
	
	public TextRenderable align(int align){
		this.align = align;
		return this;
	}
	
	@Override
	public TextRenderable setPosition(float x, float y){
		if(!MathUtils.isEqual(y, this.y, 0.001f))
			RenderableHandler.instance().requestSort();
		this.x = x;
		this.y = y;
		return this;
	}
	
	@Override
	public void draw(Batch batch){
		font.getData().setScale(1/5f);
		font.setColor(color);
		layout.setText(font, text);
		
		font.draw(batch, text, x, y, 0.5f, align, false);
	}

	@Override
	public float layer(){
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
