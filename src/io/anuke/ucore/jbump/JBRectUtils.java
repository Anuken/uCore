/*
 * Copyright 2017 DongBat.
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

import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * @author tao
 */
public class JBRectUtils{

    public static final float DELTA = 1e-5f;
    private final static Rectangle diff = new Rectangle();
    private final static Vector2 nearestCorner = new Vector2();
    private final static Vector2 ti = new Vector2();
    private final static Vector2 n1 = new Vector2();
    private final static Vector2 n2 = new Vector2();
    private final static JBCollision col = new JBCollision();

    public static void getNearestCorner(float x, float y, float w, float h, float px, float py, Vector2 result){
        result.set(nearest(px, x, x + w), nearest(y, y, y + h));
    }

    public static boolean getSegmentIntersectionIndices(float x, float y, float w, float h, float x1, float y1, float x2, float y2, float ti1, float ti2, Vector2 ti, Vector2 n1, Vector2 n2){
        float dx = x2 - x1;
        float dy = y2 - y1;

        float nx = 0, ny = 0;
        float nx1 = 0, ny1 = 0, nx2 = 0, ny2 = 0;
        float p, q, r;

        for(int side = 1; side <= 4; side++){
            switch(side){
                case 1:
                    nx = -1;
                    ny = 0;
                    p = -dx;
                    q = x1 - x;
                    break;
                case 2:
                    nx = 1;
                    ny = 0;
                    p = dx;
                    q = x + w - x1;
                    break;
                case 3:
                    nx = 0;
                    ny = -1;
                    p = -dy;
                    q = y1 - y;
                    break;
                default:
                    nx = 0;
                    ny = -1;
                    p = dy;
                    q = y + h - y1;
                    break;
            }

            if(p == 0){
                if(q <= 0){
                    return false;
                }
            }else{
                r = q / p;
                if(p < 0){
                    if(r > ti2){
                        return false;
                    }else if(r > ti1){
                        ti1 = r;
                        nx1 = nx;
                        ny1 = ny;
                    }
                }else{
                    if(r < ti1){
                        return false;
                    }else if(r < ti2){
                        ti2 = r;
                        nx2 = nx;
                        ny2 = ny;
                    }
                }
            }
        }
        ti.set(ti1, ti2);
        n1.set(nx1, ny1);
        n2.set(nx2, ny2);
        return true;
    }

    public static void getDiff(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2, Rectangle result){
        result.set(x2 - x1 - w1, y2 - y1 - h1, w1 + w2, h1 + h2);
    }

    public static boolean containsPoint(float x, float y, float w, float h, float px, float py){
        return px - x > DELTA && py - y > DELTA && x + w - px > DELTA && y + h - py > DELTA;
    }

    public static boolean isIntersecting(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2){
        return x1 < x2 + w2 && x2 < x1 + w1 && y1 < y2 + h2 && y2 < y1 + h1;
    }

    public static float getSquareDistance(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2){
        float dx = x1 - x2 + (w1 - w2) / 2;
        float dy = y1 - y2 + (h1 - h2) / 2;
        return dx * dx + dy * dy;
    }

    public static JBCollision detectCollision(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2, float goalX, float goalY){
        float dx = goalX - x1;
        float dy = goalY - y1;

        getDiff(x1, y1, w1, h1, x2, y2, w2, h2, diff);
        float x = diff.x;
        float y = diff.y;
        float w = diff.width;
        float h = diff.height;

        boolean overlaps = false;
        Float ti = null;
        float nx = 0, ny = 0;

        if(containsPoint(x, y, w, h, 0, 0)){
            getNearestCorner(x, y, w, h, 0, 0, nearestCorner);
            float px = nearestCorner.x;
            // float py = nearestCorner.y;
            float wi = min(w1, abs(px));
            //float hi = min(h1, abs(py));
            ti = -wi * h1;
            overlaps = true;
        }else{
            boolean intersect = getSegmentIntersectionIndices(x, y, w, h, 0, 0, dx, dy, -Float.MAX_VALUE, Float.MAX_VALUE, JBRectUtils.ti, n1, n2);
            float ti1 = JBRectUtils.ti.x;
            float ti2 = JBRectUtils.ti.y;
            float nx1 = n1.x;
            float ny1 = n1.y;

            if(intersect && ti1 < 1 && abs(ti1 - ti2) >= DELTA && (0 < ti1 + DELTA || 0 == ti1 && ti2 > 0)){
                ti = ti1;
                nx = nx1;
                ny = ny1;
                overlaps = false;
            }
        }
        if(ti == null){
            return null;
        }
        float tx, ty;

        if(overlaps){
            if(dx == 0 && dy == 0){
                getNearestCorner(x, y, w, h, 0, 0, nearestCorner);
                float px = nearestCorner.x;
                float py = nearestCorner.y;
                if(abs(px) < abs(py)){
                    py = 0;
                }else{
                    px = 0;
                }
                nx = sign(px);
                ny = sign(py);
                tx = x1 + px;
                ty = y1 + py;
            }else{
                boolean intersect = getSegmentIntersectionIndices(x, y, w, h, 0, 0, dx, dy, -Float.MAX_VALUE, 1, JBRectUtils.ti, n1, n2);
                float ti1 = JBRectUtils.ti.x;
                nx = n1.x;
                ny = n1.y;
                if(!intersect){
                    return null;
                }
                tx = x1 + dx * ti1;
                ty = y1 + dy * ti1;
            }
        }else{
            tx = x1 + dx * ti;
            ty = y1 + dy * ti;
        }
        col.set(overlaps, ti, dx, dy, nx, ny, tx, ty, x1, y1, w1, h1, x2, y2, w2, h2);
        return col;
    }

    public static int sign(float x){
        if(x > 0){
            return 1;
        }else if(x < 0){
            return -1;
        }
        return 0;
    }

    public static float nearest(float x, float a, float b){
        if(Math.abs(a - x) < Math.abs(b - x)){
            return a;
        }
        return b;
    }

}
