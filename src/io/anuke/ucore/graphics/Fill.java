package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.util.Angles;
import io.anuke.ucore.util.Tmp;

import static io.anuke.ucore.core.Core.batch;

public class Fill{
    private static Sprite sprite;
    private static float[] vertices = new float[20];
    private static TextureRegion circleRegion;
    private static TextureRegion[] shapeRegions;

    private static void initShapes(){
        if(shapeRegions == null){
            shapeRegions = new TextureRegion[9];
            for(int i = 3; i <= 8; i++){
                if(Draw.hasRegion("shape-" + i)){
                    shapeRegions[i] = Draw.region("shape-" + i);
                }
            }
        }
    }

    public static void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
        int i = 0;
        float color = Core.batch.getPackedColor();
        float u = Draw.getBlankRegion().getU();
        float v = Draw.getBlankRegion().getV();
        vertices[i++] = x1;
        vertices[i++] = y1;
        vertices[i++] = color;
        vertices[i++] = u;
        vertices[i++] = v;

        vertices[i++] = x2;
        vertices[i++] = y2;
        vertices[i++] = color;
        vertices[i++] = u;
        vertices[i++] = v;

        vertices[i++] = x3;
        vertices[i++] = y3;
        vertices[i++] = color;
        vertices[i++] = u;
        vertices[i++] = v;

        vertices[i++] = x4;
        vertices[i++] = y4;
        vertices[i++] = color;
        vertices[i++] = u;
        vertices[i++] = v;

        batch.draw(Draw.getBlankRegion().getTexture(), vertices, 0, vertices.length);
    }

    public static void tri(float x1, float y1, float x2, float y2, float x3, float y3){
        int i = 0;
        float color = Core.batch.getPackedColor();
        float u = Draw.getBlankRegion().getU();
        float v = Draw.getBlankRegion().getV();
        vertices[i++] = x1;
        vertices[i++] = y1;
        vertices[i++] = color;
        vertices[i++] = u;
        vertices[i++] = v;

        vertices[i++] = x2;
        vertices[i++] = y2;
        vertices[i++] = color;
        vertices[i++] = u;
        vertices[i++] = v;

        vertices[i++] = x3;
        vertices[i++] = y3;
        vertices[i++] = color;
        vertices[i++] = u;
        vertices[i++] = v;

        vertices[i++] = x3;
        vertices[i++] = y3;
        vertices[i++] = color;
        vertices[i++] = u;
        vertices[i++] = v;

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

    public static void polyTri(float x, float y, int sides, float radius){
        polyTri(x, y, sides, radius, 0f);
    }

    public static void polyTri(float x, float y, int sides, float radius, float rotation){
        float space = 360f/sides;

        for(int i = 0; i < sides-1; i += 2){
            float px = Angles.trnsx(space * i+rotation, radius);
            float py = Angles.trnsy(space * i+rotation, radius);
            float px2 = Angles.trnsx(space * (i+1)+rotation, radius);
            float py2 = Angles.trnsy(space * (i+1)+rotation, radius);
            float px3 = Angles.trnsx(space * (i+2)+rotation, radius);
            float py3 = Angles.trnsy(space * (i+2)+rotation, radius);
            quad(x, y, x+px, y+py, x+px2, y+py2, x+px3, y+py3);
        }


        int mod = sides % 3;

        for(int i = sides - mod - 1; i < sides; i++){
            float px = Angles.trnsx(space * i+rotation, radius);
            float py = Angles.trnsy(space * i+rotation, radius);
            float px2 = Angles.trnsx(space * (i+1)+rotation, radius);
            float py2 = Angles.trnsy(space * (i+1)+rotation, radius);
            tri(x, y, x + px, y + py, x + px2, y + py2);
        }
    }

    public static void poly(float x, float y, int sides, float radius){
        initShapes();
        Draw.rect(shapeRegions[sides], x, y, radius * 2, radius * 2);
    }

    public static void poly(float x, float y, int sides, float radius, float rotation){
        initShapes();
        Draw.rect(shapeRegions[sides], x, y, radius * 2, radius * 2, rotation);
    }

    public static void circle(float x, float y, float radius){
        if(circleRegion == null){
            circleRegion = Draw.region("circle");
        }

        Draw.rect(circleRegion, x, y, radius * 2, radius * 2);
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
