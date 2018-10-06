package io.anuke.ucore.fluid;

/** An interface for setting and getting fluid values. An implementation with a 2D array is typically used. */
public interface FluidProvider{

    boolean isSolid(int x, int y);

    boolean isSettled(int x, int y);

    void setSettled(int x, int y, boolean settle);

    int getSettleCount(int x, int y);

    void setSettleCount(int x, int y, int count);

    float getLiquid(int x, int y);

    void setLiquid(int x, int y, float amount);

    void clearChanges();

    float getChanges(int x, int y);

    void setChanges(int x, int y, float amount);

    int getWidth();

    int getHeight();
}
