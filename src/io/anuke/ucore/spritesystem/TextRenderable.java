package io.anuke.ucore.spritesystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;

public class TextRenderable extends Renderable{
	private static GlyphLayout layout = new GlyphLayout();
	public float x,y;
	public String text;
	public BitmapFont font;
	public boolean centerx, centery;
	public Color color = Color.WHITE;
	
	public TextRenderable(BitmapFont font, String text){
		this.text = text;
		this.font = font;
	}
	
	public TextRenderable setColor(Color color){
		this.color = color;
		return this;
	}
	
	public TextRenderable center(){
		centerx = centery = true;
		return this;
	}
	
	@Override
	public TextRenderable setPosition(float x, float y){
		if(!MathUtils.isEqual(y, this.y, 0.001f))
			RenderableHandler.getInstance().requestSort();
		this.x = x;
		this.y = y;
		return this;
	}
	
	@Override
	public void draw(Batch batch){
		font.getData().setScale(1/5f);
		font.setColor(color);
		layout.setText(font, text);
		
		font.draw(batch, text, x - (centerx ? layout.width/2 : 0), y + (centery ? layout.height/2 : 0));
	}

	@Override
	public float layer(){
		return y;
	}
}
