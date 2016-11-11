package io.anuke.ucore.g3d;

import com.badlogic.gdx.graphics.g3d.ModelBatch;

public abstract class ModelRenderable{
	public float x, y, z;
	
	public abstract void render(ModelBatch batch);
	
	public ModelRenderable position(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public void add(){
		ModelHandler.instance().add(this);
	}
	
	public void remove(){
		ModelHandler.instance().remove(this);
	}
}
