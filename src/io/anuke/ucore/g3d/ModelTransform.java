package io.anuke.ucore.g3d;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class ModelTransform extends ModelInstance{

	public ModelTransform(Model model) {
		super(model);
	}
	
	public ModelTransform(String name){
		super(Models.get(name));
	}
	
	public void rotate(Vector3 vector, float rot){
		this.transform.rotate(vector, rot);
	}
	
	public void position(float x, float y, float z){
		transform.setTranslation(x, y, z);
	}
	
	public void add(){
		ModelHandler.instance().add(this);
	}
	
	public void remove(){
		ModelHandler.instance().remove(this);
	}

}
