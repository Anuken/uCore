package io.anuke.ucore.function;

import com.badlogic.gdx.math.Rectangle;

public interface BoundingBoxProvider<T>{
    void getBoundingBox(T type, Rectangle out);
}
