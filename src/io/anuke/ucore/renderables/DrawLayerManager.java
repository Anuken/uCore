package io.anuke.ucore.renderables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import io.anuke.ucore.core.DrawContext;
import io.anuke.ucore.graphics.FrameBufferMap;

public class DrawLayerManager implements LayerManager{
	FrameBufferMap buffers = new FrameBufferMap();
	SpriteBatch batch;
	OrthographicCamera camera;

	@Override
	public void draw(Array<Renderable> renderables, Batch batch){
		this.batch = (SpriteBatch)batch;
		camera = DrawContext.camera;
		batch.end();

		Array<DrawLayer> blayers = new Array<DrawLayer>(DrawLayer.values());

		DrawLayer selected = null;

		batch.begin();

		for(Renderable layer : renderables){

			boolean ended = false;

			if(selected != null && (!selected.layerEquals(layer.getLayer()))){
				endBufferLayer(selected, blayers);
				selected = null;
				ended = true;
			}

			if(selected == null){

				for(DrawLayer fl : blayers){
					if(fl.layerEquals(layer.getLayer())){
						if(ended)
							layer.draw(batch);
						selected = fl;
						beginBufferLayer(selected);
						break;
					}
				}
			}

			layer.draw(batch);
		}
		if(selected != null){
			endBufferLayer(selected, blayers);
			selected = null;
		}
		batch.end();
		batch.begin();

		batch.setColor(Color.WHITE);
	}
	
	private void beginBufferLayer(DrawLayer selected){
		selected.beginDraw(batch, camera, buffers.get(selected.name));

		batch.end();

		//processor.captureEnd();

		buffers.begin(selected.name);
		buffers.get(selected.name).getColorBufferTexture().bind(selected.bind);
		for(Texture t : DrawContext.atlas.getTextures())
			t.bind(0);

		if(selected.shader != null)
			batch.setShader(selected.shader);
		batch.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	private void endBufferLayer(DrawLayer selected, Array<DrawLayer> layers){
		batch.end();
		if(selected.shader != null)
			batch.setShader(null);
		buffers.end(selected.name);
		buffers.get(selected.name).getColorBufferTexture().bind(0);
		
		//processor.captureNoClear();

		batch.begin();
		selected.end();
		batch.setColor(Color.WHITE);
		if(layers != null)
			layers.removeValue(selected, true);
	}
}
