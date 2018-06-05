package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.util.Tmp;

import static io.anuke.ucore.core.Core.batch;

public class Fill {
    private static Sprite sprite;
    private static float[] vertices = new float[20];
    private static TextureRegion circleRegion;
    private static TextureRegion[] shapeRegions;

    private static void initShapes(){
        if(shapeRegions == null){
            shapeRegions = new TextureRegion[9];
            for(int i = 3; i <= 8; i ++){
                shapeRegions[i] = Draw.region("shape-" + i);
            }
        }
    }

    public static void tri(float x1, float y1, float x2, float y2, float x3, float y3){
        int i = 0;
        float color = Core.batch.getPackedColor();
        vertices[i ++] = x1;
        vertices[i ++] = y1;
        vertices[i ++] = color;
        vertices[i ++] = 0;
        vertices[i ++] = 0;

        vertices[i ++] = x2;
        vertices[i ++] = y2;
        vertices[i ++] = color;
        vertices[i ++] = 0;
        vertices[i ++] = 0;

        vertices[i ++] = x3;
        vertices[i ++] = y3;
        vertices[i ++] = color;
        vertices[i ++] = 0;
        vertices[i ++] = 0;

        vertices[i ++] = x3;
        vertices[i ++] = y3;
        vertices[i ++] = color;
        vertices[i ++] = 0;
        vertices[i ++] = 0;

        batch.draw(Draw.getBlankRegion().getTexture(), vertices, 0, vertices.length);
    }

    public static void gradient(Color left, Color right, float alpha, float x, float y, float w, float h){
        if(sprite == null)
            sprite = new Sprite(Draw.getBlankRegion());

        sprite.setBounds(x, y, w, h);

        Tmp.c3.set(left);
        Tmp.c3.a *= alpha;
        float cl = Tmp.c3.toFloatBits();

        Tmp.c3.set(right);
        Tmp.c3.a *= alpha;
        float cr = Tmp.c3.toFloatBits();

        float[] v = sprite.getVertices();
        v[SpriteBatch.C1] = cl;
        v[SpriteBatch.C2] = cl;

        v[SpriteBatch.C3] = cr;
        v[SpriteBatch.C4] = cr;

        sprite.draw(batch);
    }

    public static void poly(float x, float y, int sides, float radius){
        initShapes();
        Draw.rect(shapeRegions[sides], x, y, radius*2, radius*2);
    }

    public static void poly(float x, float y, int sides, float radius, float rotation){
        initShapes();
        Draw.rect(shapeRegions[sides], x, y, radius*2, radius*2, rotation);
    }

    public static void circle(float x, float y, float radius){
        if(circleRegion == null){
            circleRegion = Draw.region("circle");
        }

        Draw.rect(circleRegion, x, y, radius*2, radius*2);
    }

    public static void rect(float x, float y, float width, float height){
        batch.draw(Draw.getBlankRegion(), x - width / 2f, y - height / 2f, width, height);
    }

    public static void square(float x, float y, float radius){
        rect(x, y, radius, radius);
    }

    public static void square(float x, float y, float radius, float rotation){
        Draw.rect(Draw.getBlankRegion(), x, y, radius * 2f, radius * 2, rotation);
    }

    public static void crect(float x, float y, float width, float height){
        batch.draw(Draw.getBlankRegion(), x, y, width, height);
    }

    public static void crect(Rectangle rect){
        batch.draw(Draw.getBlankRegion(), rect.x, rect.y, rect.width, rect.height);
    }
}
