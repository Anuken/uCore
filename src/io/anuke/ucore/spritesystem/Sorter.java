package io.anuke.ucore.spritesystem;

public enum Sorter{
	tile{
		public int compare(Renderable a, Renderable b){
			if(b.provider == tile){
				if(a.layer() == b.layer())
					return 0;
				return a.layer() < b.layer() ? 1 : -1;
			}else{
				return -1;
			}

		}
	},
	object{
		public int compare(Renderable a, Renderable b){
			if(b.provider == object){
				if(a.layer() == b.layer())
					return 0;
				return a.layer() < b.layer() ? 1 : -1;
			}else{
				return 1;
			}

		}
	};
	public abstract int compare(Renderable a, Renderable b);
}
