package io.anuke.ucore.fluid;

/** An interface for setting and getting fluid values. An implementation with a 2D array is typically used. */
public interface FluidProvider{

    public boolean isSolid(int x, int y);

    public boolean isSettled(int x, int y);

    public void setSettled(int x, int y, boolean settle);

    public int getSettleCount(int x, int y);

    public void setSettleCount(int x, int y, int count);

    public float getLiquid(int x, int y);

    public void setLiquid(int x, int y, float amount);

    public void clearChanges();

    public float getChanges(int x, int y);

    public void setChanges(int x, int y, float amount);

    public int getWidth();

    public int getHeight();
}
