/*
 * Copyright 2017 tao.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.anuke.ucore.jbump;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.*;

/**
 * @author tao
 */
public class JBGrid{
    private final Vector2 grid_traverse_c1 = new Vector2();
    private final Vector2 grid_traverse_c2 = new Vector2();
    private final Vector2 grid_traverse_initStepX = new Vector2();
    private final Vector2 grid_traverse_initStepY = new Vector2();
    private final Vector2 toCellRect = new Vector2();

    public static void toWorld(float cellSize, float cx, float cy, Vector2 point){
        point.set((cx - 1) * cellSize, (cy - 1) * cellSize);
    }

    public static void toCell(float cellSize, float x, float y, Vector2 point){
        point.set((float) floor(x / cellSize) + 1, (float) floor(y / cellSize) + 1);
    }

    public static int traverseInitStep(float cellSize, float ct, float t1, float t2, Vector2 point){
        float v = t2 - t1;
        if(v > 0){
            point.set(cellSize / v, ((ct + v) * cellSize - t1) / v);
            return 1;
        }else if(v < 0){
            point.set(-cellSize / v, ((ct + v - 1) * cellSize - t1) / v);
            return -1;
        }else{
            point.set(Float.MAX_VALUE, Float.MAX_VALUE);
            return 0;
        }
    }

    public void traverse(float cellSize, float x1, float y1, float x2, float y2, TraverseCallback f){
        toCell(cellSize, x1, y1, grid_traverse_c1);
        float cx1 = grid_traverse_c1.x;
        float cy1 = grid_traverse_c1.y;
        toCell(cellSize, x2, y2, grid_traverse_c2);
        float cx2 = grid_traverse_c2.x;
        float cy2 = grid_traverse_c2.y;
        int stepX = traverseInitStep(cellSize, cx1, x1, x2, grid_traverse_initStepX);
        int stepY = traverseInitStep(cellSize, cy1, y1, y2, grid_traverse_initStepY);
        float dx = grid_traverse_initStepX.x;
        float tx = grid_traverse_initStepX.y;
        float dy = grid_traverse_initStepY.x;
        float ty = grid_traverse_initStepY.y;
        float cx = cx1, cy = cy1;

        f.onTraverse(cx, cy);

        while(abs(cx - cx2) + abs(cy - cy2) > 1){
            if(tx < ty){
                tx = tx + dx;
                cx = cx + stepX;
                f.onTraverse(cx, cy);
            }else{
                if(tx == ty){
                    f.onTraverse(cx + stepX, cy);
                }
                ty = ty + dy;
                cy = cy + stepY;
                f.onTraverse(cx, cy);
            }
        }

        if(cx != cx2 || cy != cy2){
            f.onTraverse(cx2, cy2);
        }
    }

    public Rectangle toCellRect(float cellSize, float x, float y, float w, float h, Rectangle rect){
        toCell(cellSize, x, y, toCellRect);
        float cx = toCellRect.x;
        float cy = toCellRect.y;

        float cr = (float) ceil((x + w) / cellSize);
        float cb = (float) ceil((y + h) / cellSize);

        rect.set(cx, cy, cr - cx + 1, cb - cy + 1);
        return rect;
    }

    public interface TraverseCallback{
        void onTraverse(float cx, float cy);
    }
}
