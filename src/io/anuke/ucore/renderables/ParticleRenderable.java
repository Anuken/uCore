package io.anuke.ucore.renderables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;

import io.anuke.ucore.core.Draw;

public class ParticleRenderable extends Renderable{
	public PooledEffect effect;
	
	public ParticleRenderable(PooledEffect effect){
		this.effect = effect;
		sort(Sorter.object);
	}
	
	@Override
	public void draw(){
		effect.draw(Draw.batch(), Gdx.graphics.getDeltaTime());
	}

	@Override
	public Renderable set(float x, float y){
		effect.getEmitters().get(0).setPosition(x, y);
		return this;
	}

	@Override
	public float getLayer(){
		return effect.getEmitters().get(0).getY();
	}

	@Override
	public void reset(){
		effect = null;
	}
}
