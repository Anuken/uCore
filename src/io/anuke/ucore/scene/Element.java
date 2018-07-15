package io.anuke.ucore.scene;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import io.anuke.ucore.function.BooleanProvider;
import io.anuke.ucore.function.IntConsumer;
import io.anuke.ucore.function.Supplier;
import io.anuke.ucore.graphics.Draw;
import io.anuke.ucore.scene.event.*;
import io.anuke.ucore.scene.utils.Disableable;

/**
 * Extends the BaseElement (Actor) class to provide more functionality.
 * (this is probably a terrible idea)
 */
public class Element extends BaseElement{
    private static final Vector2 vec = new Vector2();

    protected float alpha = 1f;
    protected Vector2 translation = new Vector2(0, 0);
    private BooleanProvider visibility;
    private Runnable update;
    private Supplier<Touchable> touchableSupplier = null;

    @Override
    public void draw(Batch batch, float parentAlpha){
        validate();
        draw();
    }

    /** Simple drawing. Use the alpha variable if needed. */
    public void draw(){

    }

    /** Find and draws a drawable by name on the width/height. */
    protected void patch(String name){
        Draw.patch(name, getX(), getY(), getWidth(), getHeight());
    }

    /** Find and draws a drawable by name on the width/height. Padding on the sides is applied. */
    protected void patch(String name, float padding){
        Draw.patch(name, getX() + padding, getY() + padding, getWidth() - padding * 2, getHeight() - padding * 2);
    }

    @Override
    public void act(float delta){
        super.act(delta);
        if(touchableSupplier != null)
            setTouchable(touchableSupplier.get());
        if(update != null)
            update.run();
    }

    public void updateVisibility(){
        if(visibility != null)
            setVisible(visibility.get());
    }

    public Vector2 worldPos(){
        return localToStageCoordinates(vec.set(0, 0));
    }

    public void setTranslation(float x, float y){
        translation.x = x;
        translation.y = y;
    }

    public Vector2 getTranslation(){
        return translation;
    }

    public void keyDown(int key, Runnable l){
        keyDown(k -> {
            if(k == key)
                l.run();
        });
    }

    /** Adds a keydown input listener. */
    public void keyDown(IntConsumer cons){
        addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                cons.accept(keycode);
                return true;
            }
        });
    }

    /** Fakes a click event on all ClickListeners. */
    public void fireClick(){
        for(EventListener listener : getListeners()){
            if(listener instanceof ClickListener){
                ((ClickListener) listener).clicked(new InputEvent(), -1, -1);
            }
        }
    }

    /** Adds a click listener. */
    public ClickListener clicked(Runnable r){
        ClickListener click;
        Element elem = this;
        addListener(click = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(r != null && !(elem instanceof Disableable && ((Disableable) elem).isDisabled())) r.run();
            }
        });
        return click;
    }

    /** Adds a touch listener. */
    public void tapped(Runnable r){
        addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                r.run();
                return true;
            }
        });
    }

    /** Adds a hover/mouse enter listener. */
    public void hovered(Runnable r){
        addListener(new InputListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Element fromActor){
                r.run();
            }
        });
    }

    /** Adds a hover/mouse enter listener. */
    public void exited(Runnable r){
        addListener(new InputListener(){
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Element fromActor){
                r.run();
            }
        });
    }

    /** Adds a mouse up listener. */
    public void released(Runnable r){
        addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                r.run();
            }
        });
    }

    /** Fires a change event on all listeners. */
    public void change(){
        fire(new ChangeListener.ChangeEvent());
    }

    /** Adds a click listener. */
    public void changed(Runnable r){
        Element elem = this;
        addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Element actor){
                if(!(elem instanceof Disableable && ((Disableable) elem).isDisabled())) r.run();
            }
        });
    }

    public void update(Runnable r){
        update = r;
    }

    public Element visible(BooleanProvider vis){
        visibility = vis;
        return this;
    }

    public void setTouchable(Supplier<Touchable> touch){
        this.touchableSupplier = touch;
    }
}
