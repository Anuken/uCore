package io.anuke.ucore.spritesystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;

public class ParticleRenderable extends Renderable{
	public PooledEffect effect;
	
	public ParticleRenderable(PooledEffect effect){
		this.effect = effect;
		setProvider(Sorter.object);
	}
	
	@Override
	public void draw(Batch batch){
		effect.draw(batch, Gdx.graphics.getDeltaTime());
	}

	@Override
	public Renderable setPosition(float x, float y){
		effect.getEmitters().get(0).setPosition(x, y);
		return this;
	}

	@Override
	public float layer(){
		return effect.getEmitters().get(0).getY();
	}

	@Override
	public void reset(){
		effect = null;
	}
}
