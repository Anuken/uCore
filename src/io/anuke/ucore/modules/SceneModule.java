package io.anuke.ucore.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.scene.Scene;
import io.anuke.ucore.scene.Skin;
import io.anuke.ucore.scene.ui.Dialog;
import io.anuke.ucore.scene.ui.layout.Table;

public class SceneModule extends Module{
    public Scene scene;
    public Skin skin;

    public SceneModule(){
        scene = new Scene(Core.batch);
        Inputs.addProcessor(scene);

        loadColors();
        loadSkin();
        loadContext();
    }

    protected void loadColors(){
        Colors.put("clear", Color.CLEAR);
        Colors.put("black", Color.BLACK);

        Colors.put("white", Color.WHITE);
        Colors.put("lightgray", Color.LIGHT_GRAY);
        Colors.put("gray", Color.GRAY);
        Colors.put("darkgray", Color.DARK_GRAY);

        Colors.put("blue", Color.BLUE);
        Colors.put("navy", Color.NAVY);
        Colors.put("royal", Color.ROYAL);
        Colors.put("slate", Color.SLATE);
        Colors.put("sky", Color.SKY);
        Colors.put("cyan", Color.CYAN);
        Colors.put("teal", Color.TEAL);

        Colors.put("green", Color.GREEN);
        Colors.put("charteuse", Color.CHARTREUSE);
        Colors.put("lime", Color.LIME);
        Colors.put("forest", Color.FOREST);
        Colors.put("olive", Color.OLIVE);

        Colors.put("yellow", Color.YELLOW);
        Colors.put("gold", Color.GOLD);
        Colors.put("goldenrod", Color.GOLDENROD);
        Colors.put("orange", Color.ORANGE);

        Colors.put("brown", Color.BROWN);
        Colors.put("tan", Color.TAN);
        Colors.put("firebrick", Color.FIREBRICK);

        Colors.put("red", Color.RED);
        Colors.put("scarlet", Color.SCARLET);
        Colors.put("coral", Color.CORAL);
        Colors.put("salmon", Color.SALMON);
        Colors.put("pink", Color.PINK);
        Colors.put("magneta", Color.MAGENTA);

        Colors.put("purple", Color.PURPLE);
        Colors.put("violet", Color.VIOLET);
        Colors.put("maroon", Color.MAROON);
        Colors.put("crimson", Color.SCARLET);
        Colors.put("scarlet", Color.SCARLET);
    }

    protected void loadSkin(){
        if(Gdx.files.internal("ui/uiskin.json").exists()){
            skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
            skin.font().setUseIntegerPositions(false);
        }else{
            Gdx.app.error("UI", "ERROR: No skin file found in ui/uiskin.json. UI features are disabled.");
        }
    }

    protected void loadContext(){
        Core.setScene(scene, skin);

        if(Core.font == null && skin != null){
            Core.font = skin.font();
        }
    }

    public boolean hasMouse(){
        try{
            return scene.hit(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), true) != null;
        }catch(Exception e){
            return true;
        }
    }

    public boolean hasMouse(float mousex, float mousey){
        return scene.hit(mousex, Gdx.graphics.getHeight() - mousey, true) != null;
    }

    public boolean hasDialog(){
        return scene.getKeyboardFocus() instanceof Dialog || scene.getScrollFocus() instanceof Dialog;
    }

    /** Updates and draws the stage. */
    public void act(){
        scene.act();
        scene.draw();
    }

    /** Creates and adds a new layout to fill the stage. */
    public Table fill(){
        return scene.table();
    }

    @Override
    public void update(){
        act();
    }

    @Override
    public void resize(int width, int height){
        scene.resize(width, height);
    }
}
