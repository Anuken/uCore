package io.anuke.ucore.layer3D;

import com.badlogic.gdx.graphics.g2d.Batch;

/** Base interface for all layered object renderers */
public interface LayerRenderer{
    void add(LayeredObject object);

    void remove(LayeredObject object);

    void render(Batch batch);
}
