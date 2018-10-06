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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import io.anuke.ucore.jbump.JBCollision.CollisionFilter;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Taken from the jBump source.
 *
 * @author tao
 */
public class JBWorld<E>{

    private final ArrayList<JBCell> getCellsTouchedBySegment_visited = new ArrayList<>();
    private final ObjectSet<JBItem> project_visited = new ObjectSet<>();
    private final Rectangle project_c = new Rectangle();
    private final ObjectSet<JBItem> project_dictItemsInCellRect = new ObjectSet<>();
    private final Rectangle add_c = new Rectangle();
    private final Rectangle remove_c = new Rectangle();
    private final Rectangle update_c1 = new Rectangle();
    private final Rectangle update_c2 = new Rectangle();
    private final ObjectSet<JBItem> check_visited = new ObjectSet<>();
    private final Array<JBCollision> check_cols = new Array<>();
    private final Array<JBCollision> check_projectedCols = new Array<>();
    private final JBResponse.Result check_result = new JBResponse.Result();
    private HashMap<Float, HashMap<Float, JBCell>> rows = new HashMap<>();
    private JBGrid grid = new JBGrid();
    private boolean tileMode = true;
    private float cellSize = 64;

    public boolean isTileMode(){
        return tileMode;
    }

    public void setTileMode(boolean tileMode){
        this.tileMode = tileMode;
    }

    private void addItemToCell(JBItem<E> item, float cx, float cy){
        if(!rows.containsKey(cy)){
            rows.put(cy, new HashMap<>());
        }

        HashMap<Float, JBCell> row = rows.get(cy);
        if(!row.containsKey(cx)){
            row.put(cx, new JBCell());
        }
        JBCell cell = row.get(cx);

        if(!cell.items.contains(item)){
            cell.items.add(item);
            cell.itemCount = cell.itemCount + 1;
        }
    }

    private boolean removeItemFromCell(JBItem item, float cx, float cy){
        if(!rows.containsKey(cy)){
            return false;
        }
        HashMap<Float, JBCell> row = rows.get(cy);
        if(!row.containsKey(cx)){
            return false;
        }
        JBCell cell = row.get(cx);
        if(!cell.items.contains(item)){
            return false;
        }
        cell.items.remove(item);
        cell.itemCount = cell.itemCount - 1;
        return true;
    }

    private ObjectSet<JBItem> getDictItemsInCellRect(float cl, float ct, float cw, float ch, ObjectSet<JBItem> result){
        result.clear();
        for(float cy = ct; cy < ct + ch; cy++){
            if(rows.containsKey(cy)){
                HashMap<Float, JBCell> row = rows.get(cy);
                for(float cx = cl; cx < cl + cw; cx++){
                    if(row.containsKey(cx)){
                        JBCell cell = row.get(cx);
                        if(cell.itemCount > 0){
                            for(JBItem item : cell.items){
                                result.add(item);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private ArrayList<JBCell> getCellsTouchedBySegment(float x1, float y1, float x2, float y2, final ArrayList<JBCell> result){
        result.clear();
        getCellsTouchedBySegment_visited.clear();
        // use set
        final ArrayList<JBCell> visited = getCellsTouchedBySegment_visited;
        grid.traverse(cellSize, x1, y1, x2, y2, (cx, cy) -> {
            if(!rows.containsKey(cy)){
                return;
            }
            HashMap<Float, JBCell> row = rows.get(cy);
            if(!row.containsKey(cx)){
                return;
            }
            JBCell cell = row.get(cx);
            if(visited.contains(cell)){
                return;
            }
            visited.add(cell);
            result.add(cell);
        });

        return result;
    }

    public Array<JBCollision> project(JBItem item, float x, float y, float w, float h, float goalX, float goalY, Array<JBCollision> collisions){
        return project(item, x, y, w, h, goalX, goalY, CollisionFilter.defaultFilter, collisions);
    }

    public Array<JBCollision> project(JBItem item, float x, float y, float w, float h, float goalX, float goalY, CollisionFilter filter, Array<JBCollision> collisions){
        collisions.clear();
        ObjectSet<JBItem> visited = project_visited;
        visited.clear();
        if(item != null){
            visited.add(item);
        }
        float tl = min(goalX, x);
        float tt = min(goalY, y);
        float tr = max(goalX + w, x + w);
        float tb = max(goalY + h, y + h);

        float tw = tr - tl;
        float th = tb - tt;

        grid.toCellRect(cellSize, tl, tt, tw, th, project_c);
        float cl = project_c.x, ct = project_c.y, cw = project_c.width, ch = project_c.height;

        ObjectSet<JBItem> dictItemsInCellRect = getDictItemsInCellRect(cl, ct, cw, ch, project_dictItemsInCellRect);
        for(JBItem other : dictItemsInCellRect){
            if(!visited.contains(other)){
                visited.add(other);
                JBResponse response = filter.filter(item, other);
                if(response != null){
                    Rectangle o = item.rect;
                    float ox = o.x, oy = o.y, ow = o.width, oh = o.height;
                    JBCollision col = JBRectUtils.detectCollision(x, y, w, h, ox, oy, ow, oh, goalX, goalY);

                    if(col != null){
                        JBCollision j = new JBCollision();
                        j.set(col.overlaps, col.ti, col.move.x, col.move.y, col.normal.x, col.normal.y,
                        col.touch.x, col.touch.y, col.itemRect.x, col.itemRect.y,
                        col.itemRect.width, col.itemRect.height, col.otherRect.x, col.otherRect.y,
                        col.otherRect.width, col.otherRect.height);
                        j.item = item;
                        j.other = other;
                        j.type = response;

                        collisions.add(j);
                    }
                }
            }
        }
        if(tileMode){
            collisions.sort();
        }
        return collisions;
    }

    //public Rectangle getRect(JBItem item){
    //    return rects.get(item);
   // }

    public int countCells(){
        int count = 0;
        for(HashMap<Float, JBCell> row : rows.values()){
            count += row.keySet().size();
        }
        return count;
    }

    public boolean hasItem(JBItem item){
        return item.rect != null;
    }

    public Vector2 toWorld(float cx, float cy, Vector2 result){
        JBGrid.toWorld(cellSize, cx, cy, result);
        return result;
    }

    public Vector2 toCell(float x, float y, Vector2 result){
        JBGrid.toCell(cellSize, x, y, result);
        return result;
    }

    public JBItem<E> add(JBItem<E> item, float x, float y, float w, float h){
        if(item.rect != null){
            return item;
        }
        item.rect = new Rectangle(x, y, w, h);
        grid.toCellRect(cellSize, x, y, w, h, add_c);
        float cl = add_c.x, ct = add_c.y, cw = add_c.width, ch = add_c.height;
        for(float cy = ct; cy < ct + ch; cy++){
            for(float cx = cl; cx < cl + cw; cx++){
                addItemToCell(item, cx, cy);
            }
        }
        return item;
    }

    public void remove(JBItem item){
        Rectangle rect = item.rect;
        if(rect == null) return;
        float x = rect.x, y = rect.y, w = rect.width, h = rect.height;

        item.rect = null;
        grid.toCellRect(cellSize, x, y, w, h, remove_c);
        float cl = remove_c.x, ct = remove_c.y, cw = remove_c.width, ch = remove_c.height;

        for(float cy = ct; cy < ct + ch; cy++){
            for(float cx = cl; cx < cl + cw; cx++){
                removeItemFromCell(item, cx, cy);
            }
        }
    }

    public void update(JBItem item, float x2, float y2){
        Rectangle rect = item.rect;
        float /* x = rect.x, y = rect.y, */ w = rect.width, h = rect.height;
        update(item, x2, y2, w, h);
    }

    public void update(JBItem item, float x2, float y2, float w2, float h2){
        Rectangle rect = item.rect;
        float x1 = rect.x, y1 = rect.y, w1 = rect.width, h1 = rect.height;
        if(x1 != x2 || y1 != y2 || w1 != w2 || h1 != h2){

            Rectangle c1 = grid.toCellRect(cellSize, x1, y1, w1, h1, update_c1);
            Rectangle c2 = grid.toCellRect(cellSize, x2, y2, w2, h2, update_c2);

            float cl1 = c1.x, ct1 = c1.y, cw1 = c1.width, ch1 = c1.height;
            float cl2 = c2.x, ct2 = c2.y, cw2 = c2.width, ch2 = c2.height;

            if(cl1 != cl2 || ct1 != ct2 || cw1 != cw2 || ch1 != ch2){
                float cr1 = cl1 + cw1 - 1, cb1 = ct1 + ch1 - 1;
                float cr2 = cl2 + cw2 - 1, cb2 = ct2 + ch2 - 1;
                boolean cyOut;

                for(float cy = ct1; cy <= cb1; cy++){
                    cyOut = cy < ct2 || cy > cb2;
                    for(float cx = cl1; cx <= cr1; cx++){
                        if(cyOut || cx < cl2 || cx > cr2){
                            removeItemFromCell(item, cx, cy);
                        }
                    }
                }

                for(float cy = ct2; cy <= cb2; cy++){
                    cyOut = cy < ct1 || cy > cb1;
                    for(float cx = cl2; cx <= cr2; cx++){
                        if(cyOut || cx < cl1 || cy > cr1){
                            addItemToCell(item, cx, cy);
                        }
                    }
                }
            }
            rect.set(x2, y2, w2, h2);
        }
    }

    public JBResponse.Result check(JBItem item, float goalX, float goalY, final CollisionFilter filter){
        ObjectSet<JBItem> visited = check_visited;
        visited.clear();
        visited.add(item);

        CollisionFilter visitedFilter = (item1, other) -> {
            if(visited.contains(other)){
                return null;
            }
            if(filter == null){
                return CollisionFilter.defaultFilter.filter(item1, other);
            }
            return filter.filter(item1, other);
        };

        Rectangle rect = item.rect;
        float x = rect.x, y = rect.y, w = rect.width, h = rect.height;
        Array<JBCollision> cols = check_cols;
        cols.clear();
        Array<JBCollision> projectedCols = project(item, x, y, w, h, goalX, goalY, filter, check_projectedCols);
        JBResponse.Result result = check_result;
        while(projectedCols != null && projectedCols.size > 0){
            JBCollision col = projectedCols.get(0);
            cols.add(col);

            visited.add(col.other);

            JBResponse response = col.type;
            response.response(this, col, x, y, w, h, goalX, goalY, visitedFilter, result);
            goalX = result.goalX;
            goalY = result.goalY;
            projectedCols = result.projectedCollisions;
        }

        result.set(goalX, goalY);
        result.projectedCollisions.clear();
        for(int i = 0; i < cols.size; i++){
            result.projectedCollisions.add(cols.get(i));
        }
        return result;
    }

    public JBResponse.Result move(JBItem item, float goalX, float goalY, CollisionFilter filter){
        JBResponse.Result result = check(item, goalX, goalY, filter);
        update(item, result.goalX, result.goalY);
        return result;
    }

    public static class JBCell{
        public int itemCount = 0;
        public float x;
        public float y;
        public ObjectSet<JBItem> items = new ObjectSet<>();
    }

    public static class JBItem<E>{
        private Rectangle rect;
        public E userData;

        public JBItem(){
        }

        public JBItem(E userData){
            this.userData = userData;
        }
    }
}
