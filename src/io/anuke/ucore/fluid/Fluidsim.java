package io.anuke.ucore.fluid;

import com.badlogic.gdx.math.MathUtils;
import io.anuke.ucore.util.Mathf;

public class Fluidsim{
    public int iterations = 5;

    public float maxValue = 1.0f;
    public float minValue = 0.005f;

    public float maxCompression = 0.25f;

    public float minFlow = 0.005f;
    public float maxFlow = 4f;

    public float flowSpeed = 1f;

    public FluidProvider provider;

    public Fluidsim(FluidProvider provider){
        this.provider = provider;
    }

    public void simulate(){
        for(int i = 0; i < iterations; i++){
            runCycle();
        }
    }

    private float CalculateVerticalFlowValue(float remainingLiquid, float destination){
        float sum = remainingLiquid + destination;
        float value = 0;

        if(sum <= maxValue){
            value = maxValue;
        }else if(sum < 2 * maxValue + maxCompression){
            value = (maxValue * maxValue + sum * maxCompression) / (maxValue + maxCompression);
        }else{
            value = (sum + maxCompression) / 2f;
        }

        return value;
    }

    private void add(int x, int y, float amount){
        provider.setChanges(x, y, provider.getChanges(x, y) + amount);
        provider.setSettled(x, y, false);
        provider.setSettleCount(x, y, 0);
    }

    private void runCycle(){
        float flow = 0;

        // Reset the diffs array
        provider.clearChanges();

        // Main loop
        for(int x = 0; x < provider.getWidth(); x++){
            for(int y = 0; y < provider.getHeight(); y++){

                // Validate cell
                if(provider.isSolid(x, y)){
                    provider.setLiquid(x, y, 0);
                    continue;
                }

                float liquid = provider.getLiquid(x, y);

                if(Mathf.zero(liquid))
                    continue;
                if(provider.isSettled(x, y))
                    continue;
                if(liquid < minValue){
                    provider.setLiquid(x, y, 0);
                    continue;
                }


                // Keep track of how much liquid this cell started off with
                float startValue = liquid;
                float remainingValue = liquid;
                flow = 0;

                // Flow to bottom cell
                if(!provider.isSolid(x, y - 1)){

                    float botl = provider.getLiquid(x, y - 1);

                    // Determine rate of flow
                    flow = CalculateVerticalFlowValue(liquid, botl) - botl;
                    if(botl > 0 && flow > minFlow)
                        flow *= flowSpeed;

                    // Constrain flow
                    flow = Math.max(flow, 0);
                    if(flow > Math.min(maxFlow, liquid))
                        flow = Math.min(maxFlow, liquid);

                    // Update temp values
                    if(!Mathf.zero(flow)){
                        remainingValue -= flow;
                        provider.setChanges(x, y, provider.getChanges(x, y) - flow);
                        provider.setChanges(x, y - 1, provider.getChanges(x, y - 1) + flow);
                        provider.setSettled(x, y - 1, false);
                        provider.setSettleCount(x, y - 1, 0);
                    }
                }

                // Check to ensure we still have liquid in this cell
                if(remainingValue < minValue){
                    add(x, y, -remainingValue);
                    continue;
                }

                // Flow to left cell
                if(!provider.isSolid(x - 1, y)){

                    // Calculate flow rate
                    flow = (remainingValue - provider.getLiquid(x - 1, y)) / 4f;
                    if(flow > minFlow)
                        flow *= flowSpeed;

                    // constrain flow
                    flow = Math.max(flow, 0);
                    if(flow > Math.min(maxFlow, remainingValue))
                        flow = Math.min(maxFlow, remainingValue);

                    // Adjust temp values
                    if(flow != 0){
                        remainingValue -= flow;
                        add(x, y, -flow);
                        add(x - 1, y, flow);
                    }
                }

                // Check to ensure we still have liquid in this cell
                if(remainingValue < minValue){
                    add(x, y, -remainingValue);
                    continue;
                }

                // Flow to right cell
                if(!provider.isSolid(x + 1, y)){

                    // calc flow rate
                    flow = (remainingValue - provider.getLiquid(x + 1, y)) / 3f;
                    if(flow > minFlow)
                        flow *= flowSpeed;

                    // constrain flow
                    flow = Math.max(flow, 0);
                    if(flow > Math.min(maxFlow, remainingValue))
                        flow = Math.min(maxFlow, remainingValue);

                    // Adjust temp values
                    if(flow != 0){
                        remainingValue -= flow;
                        add(x, y, -flow);
                        add(x + 1, y, flow);
                    }
                }

                // Check to ensure we still have liquid in this cell
                if(remainingValue < minValue){
                    add(x, y, -remainingValue);
                    continue;
                }

                // Flow to Top cell
                if(!provider.isSolid(x, y + 1)){

                    flow = remainingValue - CalculateVerticalFlowValue(remainingValue, provider.getLiquid(x, y + 1));
                    if(flow > minFlow)
                        flow *= flowSpeed;

                    // constrain flow
                    flow = Math.max(flow, 0);
                    if(flow > Math.min(maxFlow, remainingValue))
                        flow = Math.min(maxFlow, remainingValue);

                    // Adjust values
                    if(flow != 0){
                        remainingValue -= flow;
                        add(x, y, -flow);
                        add(x, y + 1, flow);
                    }
                }

                // Check to ensure we still have liquid in this cell
                if(remainingValue < minValue){
                    add(x, y, -remainingValue);
                    continue;
                }

                // Check if cell is settled
                if(MathUtils.isEqual(startValue, remainingValue)){
                    provider.setSettleCount(x, y, provider.getSettleCount(x, y) + 1);
                    if(provider.getSettleCount(x, y) >= 10){
                        provider.setSettled(x, y, true);
                    }
                }else{
                    provider.setSettled(x + 1, y, false);
                    provider.setSettled(x - 1, y, false);
                    provider.setSettled(x, y + 1, false);
                    provider.setSettled(x, y - 1, false);
                }
            }
        }

        // Update Cell values
        for(int x = 0; x < provider.getWidth(); x++){
            for(int y = 0; y < provider.getHeight(); y++){
                provider.setLiquid(x, y, provider.getLiquid(x, y) + provider.getChanges(x, y));
                if(provider.getLiquid(x, y) < minValue){
                    provider.setLiquid(x, y, 0);
                    provider.setSettled(x, y, false);
                    provider.setSettleCount(x, y, 0);
                }
            }
        }
    }

}
