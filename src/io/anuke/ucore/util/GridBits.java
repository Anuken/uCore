package io.anuke.ucore.util;

public class GridBits{
    private final com.badlogic.gdx.utils.Bits bits;
    private final int width, height;

    public GridBits(int width, int height){
        this.width = width;
        this.height = height;
        bits = new com.badlogic.gdx.utils.Bits(width * height);
    }

    public boolean get(int x, int y){
        return bits.get(x + y*width);
    }

    public void set(int x, int y, boolean b){
        if(b){
            bits.set(x + y * width);
        }else{
            bits.clear(x + y * width);
        }
    }

    public int width(){
        return width;
    }

    public int height(){
        return height;
    }
}
