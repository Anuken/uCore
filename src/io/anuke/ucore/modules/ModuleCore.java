package io.anuke.ucore.modules;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Graphics;
import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.util.ThreadArray;

public abstract class ModuleCore implements Screen {
    protected ObjectMap<Class<? extends Module>, Module> modules = new ObjectMap<>();
    protected Array<Module> modulearray = new ThreadArray<>();

    public ModuleCore(){
    }

    abstract public void init();

    public void preInit(){
    }

    public void postInit(){
    }

    public void update(){
        Inputs.update();
    }

    /** Adds a module to the list. */
    protected <N extends Module> void module(N t){
        modules.put(t.getClass(), t);
        modulearray.add(t);
        t.preInit();
    }

    @Override
    public void resize(int width, int height){
        Graphics.resize();
        for(Module module : modulearray){
            module.resize(width, height);
        }
    }

    @Override
    public final void show(){
        Inputs.initialize();

        init();
        preInit();
        for(Module module : modulearray){
            module.init();
        }
        postInit();
    }

    public void render() {
        for(Module module : modulearray){
            module.update();
        }

        update();
    }

    @Override
    public void render(float delta){
        render();
    }

    @Override
    public void pause(){
        for(Module module : modulearray)
            module.pause();
    }

    @Override
    public void resume(){
        for(Module module : modulearray)
            module.resume();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose(){
        for(Module module : modulearray)
            module.dispose();

        Core.dispose();
    }
}
