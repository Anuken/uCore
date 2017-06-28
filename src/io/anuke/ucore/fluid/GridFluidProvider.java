package io.anuke.ucore.fluid;

import io.anuke.ucore.util.Mathf;

public class GridFluidProvider implements FluidProvider{
	private final int width, height;
	
	private float[][] liquid;
	private float[][] changes;
	private boolean[][] settled;
	private int[][] settlecount;
	private boolean[][] solid;
	

	public GridFluidProvider(int width, int height){
		this.width = width;
		this.height = height;
		
		liquid = new float[width][height];
		changes = new float[width][height];
		settled = new boolean[width][height];
		settlecount = new int[width][height];
		solid = new boolean[width][height];
	}
	
	@Override
	public boolean isSolid(int x, int y){
		return !Mathf.inBounds(x, y, solid) || solid[x][y];
	}

	@Override
	public boolean isSettled(int x, int y){
		return settled[x][y];
	}

	@Override
	public void setSettled(int x, int y, boolean settle){
		if(!Mathf.inBounds(x, y, settled)) return;
		settled[x][y] = settle;
	}

	@Override
	public int getSettleCount(int x, int y){
		return settlecount[x][y];
	}

	@Override
	public void setSettleCount(int x, int y, int count){
		settlecount[x][y] = count;
	}

	@Override
	public float getLiquid(int x, int y){
		return liquid[x][y];
	}

	@Override
	public void setLiquid(int x, int y, float amount){
		liquid[x][y] = amount;
	}

	@Override
	public void clearChanges(){
		for(int x = 0; x < width; x ++){
			for(int y = 0; y < height; y ++){
				changes[x][y] = 0;
			}
		}
	}

	@Override
	public float getChanges(int x, int y){
		return changes[x][y];
	}

	@Override
	public void setChanges(int x, int y, float amount){
		changes[x][y] = amount;
	}

	@Override
	public int getWidth(){
		return width;
	}

	@Override
	public int getHeight(){
		return height;
	}

}
