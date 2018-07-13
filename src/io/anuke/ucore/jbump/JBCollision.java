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

/**
 * @author tao
 */
public class JBCollision{
    public boolean overlaps;
    public float ti;
    public Vector2 move = new Vector2();
    public Vector2 normal = new Vector2();
    public Vector2 touch = new Vector2();
    public Rectangle itemRect = new Rectangle();
    public Rectangle otherRect = new Rectangle();
    public JBWorld.JBItem item;
    public JBWorld.JBItem other;
    public JBResponse type;

    public JBCollision(){
    }

    public void set(boolean overlaps, float ti, float moveX, float moveY, float normalX, float normalY, float touchX, float touchY, float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2){
        this.overlaps = overlaps;
        this.ti = ti;
        this.move.set(moveX, moveY);
        this.normal.set(normalX, normalY);
        this.touch.set(touchX, touchY);
        this.itemRect.set(x1, y1, w1, h1);
        this.otherRect.set(x2, y2, w2, h2);
    }

    public interface CollisionFilter{

        CollisionFilter defaultFilter = (item, other) -> JBResponse.slide;

        JBResponse filter(JBWorld.JBItem item, JBWorld.JBItem other);
    }
}
