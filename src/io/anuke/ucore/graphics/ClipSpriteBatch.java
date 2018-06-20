package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.util.Mathf;

public class ClipSpriteBatch extends SpriteBatch {
    private Rectangle viewport = new Rectangle();
    private Rectangle clip = new Rectangle();
    private boolean doClip = true;

    public void enableClip(boolean enabled){
        this.doClip = enabled;
    }

    @Override
    public void draw (TextureRegion region, float x, float y, float width, float height) {
        clip.set(x, y, width, height);
        if(!doClip || clip.overlaps(viewport)){
            super.draw(region, x, y, width, height);
        }
    }

    @Override
    public void draw (TextureRegion region, float x, float y, float originX, float originY, float width, float height,
                      float scaleX, float scaleY, float rotation) {
        float max = Math.max(width * scaleX, height * scaleY) * Mathf.sqrt2;

        clip.set(x - originX, y - originY, max, max);

        if(!doClip || clip.overlaps(viewport)){
            super.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
        }
    }

    @Override
    public void begin() {
        super.begin();
        viewport.setSize(Core.camera.viewportWidth * Core.camera.zoom, Core.camera.viewportHeight * Core.camera.zoom)
        .setCenter(Core.camera.position.x, Core.camera.position.y);
    }
}
