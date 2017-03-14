package io.anuke.ucore.spritesystem;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool.Poolable;

public abstract class Renderable implements Comparable<Renderable>, Poolable{
	public Sorter provider = Sorter.tile;
	
	public abstract void draw(Batch batch);
	
	public abstract Renderable set(float x, float y);
	
	public abstract float layer();
	
	public Renderable sort(Sorter provider){
		this.provider = provider;
		return this;
	}
	
	public void add(RenderableList list){
		list.add(this);
	}
	
	public void add(String name, RenderableGroup group){
		group.add(name, this);
	}
	
	public <T extends Renderable> T  add(){
		RenderableHandler.instance().add(this);
		return (T)this;
	}
	
	public void remove(){
		RenderableHandler.instance().remove(this);
	}
	
	public SpriteRenderable sprite(){
		return (SpriteRenderable)this;
	}
	
	public void onFree(){
		
	}

	public int compareTo(Renderable o){
		return provider.compare(this, o);
	}
}
