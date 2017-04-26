package io.anuke.ucore.drawpointers;

public enum DrawLayer{
	tile{
		public int compare(float la, DrawLayer b, float lb){
			if(b == tile){
				if(la == lb)
					return 0;
				return la < lb ? 1 : -1;
			}else{
				return -1;
			}
		}
	},
	object{
		public int compare(float la, DrawLayer b, float lb){
			if(b == object){
				if(la == lb)
					return 0;
				return la < lb ? 1 : -1;
			}else{
				return 1;
			}
		}
	};

	public abstract int compare(float la, DrawLayer b, float lb);
}
