package io.anuke.ucore.function;

import com.badlogic.gdx.math.Rectangle;

public interface TileHitboxProvider{
    void getHitbox(int x, int y, Rectangle out);
}
