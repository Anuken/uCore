package io.anuke.ucore.spritesort;

public enum DrawLayer{
	tile{
		public int compare(DrawLayer a, float la, DrawLayer b, float lb){
			if(a == tile){
				if(la == lb)
					return 0;
				return la < lb ? 1 : -1;
			}else{
				return -1;
			}

		}
	},
	object{
		public int compare(DrawLayer a, float la, DrawLayer b, float lb){
			if(b == object){
				if(la == lb)
					return 0;
				return la < lb ? 1 : -1;
			}else{
				return 1;
			}
		}

	};

	public int compare(DrawLayer a, float la, DrawLayer b, float lb){
		return 0;
	}
}
