package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.util.Mathf;

public class Shapes{

    public static void laser(String line, String edge, float x, float y, float x2, float y2, float scale){
        laser(line, edge, x, y, x2, y2, Mathf.atan2(x2 - x, y2 - y), scale);
    }

    public static void laser(String line, String edge, float x, float y, float x2, float y2){
        laser(line, edge, x, y, x2, y2, Mathf.atan2(x2 - x, y2 - y), 1f);
    }

    public static void laser(String line, String edge, float x, float y, float x2, float y2, float rotation, float scale){

        Lines.stroke(12f * scale);
        Lines.line(Draw.region(line), x, y, x2, y2, CapStyle.none, 0f);
        Lines.stroke(1f);

        TextureRegion region = Draw.region(edge);

        Draw.rect(edge, x, y, region.getRegionWidth() * Draw.scale(), region.getRegionHeight() * scale * Draw.scale(), rotation + 180);

        Draw.rect(edge, x2, y2, region.getRegionWidth() * Draw.scale(), region.getRegionHeight() * scale * Draw.scale(), rotation);
    }

    public static void tri(float x, float y, float width, float length, float rotation){
        float oy = 17f / 63f * length;
        Core.batch.draw(Draw.region("shape-3"), x - width / 2f, y - oy, width / 2f, oy, width, length, 1f, 1f, rotation - 90);
    }
}
