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

import com.badlogic.gdx.math.Vector2;
import io.anuke.ucore.jbump.JBCollision.CollisionFilter;

/**
 * @author tao
 */
public interface JBResponse{

    JBResponse slide = (world, collision, x, y, w, h, goalX, goalY, filter, result) -> {
        Vector2 tch = collision.touch;
        Vector2 move = collision.move;
        float sx = tch.x, sy = tch.y;
        if(move.x != 0 || move.y != 0){
            if(collision.normal.x == 0){
                sx = goalX;
            }else{
                sy = goalY;
            }
        }

        x = tch.x;
        y = tch.y;
        goalX = sx;
        goalY = sy;
        result.projectedCollisions.clear();
        world.project(collision.item, x, y, w, h, goalX, goalY, filter, result.projectedCollisions);
        result.set(goalX, goalY);
        return result;
    };
    JBResponse touch = (world, collision, x, y, w, h, goalX, goalY, filter, result) -> {
        result.projectedCollisions.clear();
        result.set(collision.touch.x, collision.touch.y);
        return result;
    };
    JBResponse cross = (world, collision, x, y, w, h, goalX, goalY, filter, result) -> {
        result.projectedCollisions.clear();
        world.project(collision.item, x, y, w, h, goalX, goalY, filter, result.projectedCollisions);
        result.set(goalX, goalY);
        return result;
    };

    Result response(JBWorld world, JBCollision collision, float x, float y, float w, float h, float goalX, float goalY, CollisionFilter filter, Result result);

    class Result{

        public float goalX;
        public float goalY;
        public JBCollisions projectedCollisions = new JBCollisions();

        public void set(float goalX, float goalY){
            this.goalX = goalX;
            this.goalY = goalY;
        }
    }
}
