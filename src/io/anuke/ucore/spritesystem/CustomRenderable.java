package io.anuke.ucore.spritesystem;

import com.badlogic.gdx.graphics.g2d.Batch;

public class CustomRenderable extends Renderable{
	float layer = 0f;
	BatchDrawable drawable;
	
	public CustomRenderable(float layer){
		this.layer = layer;
	}
	
	public CustomRenderable(float layer, BatchDrawable draw){
		this.layer = layer;
		this.drawable = draw;
	}
	
	public CustomRenderable(){
		this(0);
	}
	
	@Override
	public void reset(){

	}

	@Override
	public void draw(Batch batch){
		if(drawable != null) drawable.draw(batch);
	}

	@Override
	public Renderable setPosition(float x, float y){
		return this;
	}

	@Override
	public float layer(){
		return layer;
	}
	
	static public interface BatchDrawable{
		public void draw(Batch batch);
	}

}
