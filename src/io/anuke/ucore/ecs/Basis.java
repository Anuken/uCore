package io.anuke.ucore.ecs;

import java.util.HashMap;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

public class Basis{
	private static Basis instance;
	
	private Array<Spark> sparks = new Array<>();
	private HashMap<Integer, Spark> sparkmap = new HashMap<>();
	
	private ObjectSet<Spark> toAdd = new ObjectSet<>();
	private ObjectSet<Spark> toRemove = new ObjectSet<>();
	
	private Array<Processor> processors = new Array<>();
	private Array<BasisListener> listeners = new Array<>();
	
	public Basis(){
		instance = this;
		addProcessor(new UpdateProcessor());
	}
	
	public void addListener(BasisListener listener){
		listeners.add(listener);
	}
	
	public void clearListeners(){
		listeners.clear();
	}
	
	public Array<Processor> getProcessors(){
		return processors;
	}
	
	public void addProcessor(Processor pro){
		processors.add(pro);
	}
	
	public void removeProcessor(Processor pro){
		processors.removeValue(pro, true);
	}
	
	public void removeProcessor(Class<? extends Processor> type){
		for(Processor p : processors){
			if(p.getClass() == type)
				processors.removeValue(p, true);
		}
	}

	public <T extends Processor> T getProcessor(Class<T> type){
		for(Processor p : processors){
			if(p.getClass() == type)
				return (T)p;
		}
		return null;
	}
	
	/**Enables or disabled all processors.*/
	public void setProcessorsEnabled(boolean enabled){
		for(Processor p : processors){
			p.setEnabled(enabled);
		}
	}
	
	public void addSpark(Spark spark){
		for(BasisListener l : listeners)
			l.added(spark);
		
		for(Trait trait : spark.getTraits()){
			trait.added(spark);
		}
		
		spark.getType().added(spark);
		
		toAdd.add(spark);
		spark.setBasis(this);
	}
	
	public void removeSpark(Spark spark){
		for(BasisListener l : listeners)
			l.removed(spark);
		
		for(Trait trait : spark.getTraits()){
			trait.removed(spark);
		}
		
		spark.getType().removed(spark);
		
		sparks.removeValue(spark, true);
	}
	
	public boolean removeSpark(int id){
		Spark spark = getSpark(id);
		
		if(spark != null){
			removeSpark(spark);
			return true;
		}else{
			return false;
		}
	}
	
	public Spark getSpark(int id){
		return sparkmap.get(id);
	}
	
	public Array<Spark> getSparks(){
		return sparks;
	}
	
	public void clearSparks(){
		toAdd.clear();
		toRemove.clear();
		sparks.clear();
		sparkmap.clear();
	}
	
	public void update(){
		for(int i = 0; i < processors.size; i ++){
			Processor pro = processors.get(i);
			if(pro.isEnabled()){
				pro.update(sparks);
			}
		}
		
		for(Spark spark : toAdd){
			sparks.add(spark);
			sparkmap.put(spark.getID(), spark);
		}
		
		toAdd.clear();
		
		for(Spark spark : toRemove){
			sparks.removeValue(spark, true);
			sparkmap.remove(spark.getID());
		}
		
		toRemove.clear();
	}
	
	public static Basis instance(){
		if(instance == null) new Basis();
		return instance;
	}
}
