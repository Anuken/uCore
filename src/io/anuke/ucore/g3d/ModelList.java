package io.anuke.ucore.g3d;

import com.badlogic.gdx.utils.Array;

public class ModelList{
	public Array<ModelRenderable> models = new Array<ModelRenderable>();
	
	public void free(){
		ModelHandler.instance().remove(models);
	}
	
	public void add(ModelRenderable m){
		m.add();
		models.add(m);
	}
}
