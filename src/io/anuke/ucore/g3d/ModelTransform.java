package io.anuke.ucore.g3d;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class ModelTransform extends ModelRenderable{
	public ModelInstance model;

	public ModelTransform(ModelInstance model) {
		this.model = model;
	}
	
	public ModelTransform(String name){
		this(Models.geti(name));
	}


	@Override
	public void render(ModelBatch batch){
		model.transform.setToTranslation(x, y, z);
		batch.render(model, ModelHandler.instance().environment());
	}

}
