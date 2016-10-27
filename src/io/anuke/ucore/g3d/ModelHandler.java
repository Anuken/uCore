package io.anuke.ucore.g3d;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Array;

public class ModelHandler{
	private static ModelHandler instance;
	
	private Array<ModelInstance> models = new Array<ModelInstance>();
	
	private ModelHandler(){}
	
	public static ModelHandler instance(){
		if(instance == null) instance = new ModelHandler();
		return instance;
	}
	
	public void render(ModelBatch batch){
		for(ModelInstance model : models)
			batch.render(model);
	}
	
	public void add(ModelInstance model){
		models.add(model);
	}
	
	public void remove(ModelInstance model){
		models.removeValue(model, true);
	}
	
	public void remove(Iterable<ModelInstance> list){
		for(ModelInstance r : list)
			remove(r);
	}
	
	public void clear(){
		models.clear();
	}
}
