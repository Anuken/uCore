package io.anuke.ucore.ecs;

import java.util.HashMap;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

public class Basis{
	private static Basis instance;
	
	private Array<Spark> sparks = new Array<>();
	private HashMap<Long, Spark> sparkmap = new HashMap<Long, Spark>();
	
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

	public Processor getProcessor(Class<? extends Processor> type){
		for(Processor p : processors){
			if(p.getClass() == type)
				return p;
		}
		return null;
	}
	
	public void addSpark(Spark spark){
		for(BasisListener l : listeners)
			l.added(spark);
		
		toAdd.add(spark);
		spark.setBasis(this);
	}
	
	public void removeSpark(Spark spark){
		for(BasisListener l : listeners)
			l.removed(spark);
		
		for(Trait trait : spark.getTraits()){
			trait.removed(spark);
		}
		
		sparks.removeValue(spark, true);
	}
	
	public void clearSparks(){
		toAdd.clear();
		toRemove.clear();
		sparks.clear();
		sparkmap.clear();
	}
	
	public void update(){
		for(Processor pro : processors){
			if(pro.isEnabled())
				pro.update(sparks);
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
