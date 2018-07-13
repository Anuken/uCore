package io.anuke.ucore.layer3D;

import com.badlogic.gdx.graphics.g2d.Batch;

/** Base interface for all layered object renderers */
public interface LayerRenderer{
    public void add(LayeredObject object);

    public void remove(LayeredObject object);

    public void render(Batch batch);
}
