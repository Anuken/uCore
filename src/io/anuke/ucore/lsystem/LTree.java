package io.anuke.ucore.lsystem;

import com.badlogic.gdx.utils.Array;

public class LTree{
    public Array<Line> lines = new Array<>();
    public Array<Leaf> leaves = new Array<>();
    public int branches;

    public static class Leaf{
        public float x, y, rotation;

        public Leaf(float x, float y, float rotation){
            this.x = x;
            this.y = y;
            this.rotation = rotation;
        }
    }

    public static class Line{
        public float x1, y1, x2, y2;

        public Line(float x1, float y1, float x2, float y2){
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

}
