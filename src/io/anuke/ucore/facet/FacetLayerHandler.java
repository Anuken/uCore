package io.anuke.ucore.facet;

import com.badlogic.gdx.utils.Array;

public class FacetLayerHandler implements FacetHandler{
	public Array<FacetLayer> allDrawLayers = Array.with(FacetLayers.shadow, FacetLayers.light, FacetLayers.darkness);

	@Override
	public void drawRenderables(Iterable<Facet> renderables){

		Array<FacetLayer> blayers = new Array<>(allDrawLayers);

		FacetLayer selected = null;

		for(Facet layer : renderables){

			boolean ended = false;

			if(selected != null && (!selected.acceptFacet(layer))){
				endBufferLayer(selected, blayers);
				selected = null;
				ended = true;
			}

			if(selected == null){

				for(FacetLayer fl : blayers){
					if(fl.acceptFacet(layer)){
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
	
	private void beginBufferLayer(FacetLayer selected){
		selected.begin();
	}

	private void endBufferLayer(FacetLayer selected, Array<FacetLayer> layers){
		selected.end();
		if(layers != null)
			layers.removeValue(selected, true);
	}
}
