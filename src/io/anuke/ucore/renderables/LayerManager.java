package io.anuke.ucore.renderables;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

public interface LayerManager{
	public void draw(Array<Renderable> renderables, Batch batch);
}
