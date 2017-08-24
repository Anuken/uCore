package io.anuke.ucore.renderables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;

import io.anuke.ucore.drawpointers.DrawHandler.PointerDrawHandler;
import io.anuke.ucore.drawpointers.DrawPointer;

public class DrawLayerManager implements LayerManager, PointerDrawHandler{
	public Array<DrawLayer> allDrawLayers = Array.with(DrawLayers.shadow, DrawLayers.light, DrawLayers.darkness);
	
	@Override
	public void draw(Array<DrawPointer> renderables){

		Array<DrawLayer> blayers = new Array<>(allDrawLayers);

		DrawLayer selected = null;

		for(DrawPointer layer : renderables){

			boolean ended = false;

			if(selected != null && (!selected.layerEquals(layer.layer))){
				endBufferLayer(selected, blayers);
				selected = null;
				ended = true;
			}

			if(selected == null){

				for(DrawLayer fl : blayers){
					if(fl.layerEquals(layer.layer)){
						if(ended)
							layer.draw();
						selected = fl;
						beginBufferLayer(selected);
						break;
					}
				}
			}

			layer.draw();
		}
		if(selected != null){
			endBufferLayer(selected, blayers);
			selected = null;
		}
	}

	@Override
	public void drawRenderables(Array<Renderable> renderables){

		Array<DrawLayer> blayers = new Array<>(allDrawLayers);

		DrawLayer selected = null;

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
							layer.draw();
						selected = fl;
						beginBufferLayer(selected);
						break;
					}
				}
			}

			layer.draw();
		}
		if(selected != null){
			endBufferLayer(selected, blayers);
			selected = null;
		}
	}
	
	private void beginBufferLayer(DrawLayer selected){
		selected.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	private void endBufferLayer(DrawLayer selected, Array<DrawLayer> layers){
		selected.end();
		if(layers != null)
			layers.removeValue(selected, true);
	}
}
