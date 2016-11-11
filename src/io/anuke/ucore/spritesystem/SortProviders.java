package io.anuke.ucore.spritesystem;

public enum SortProviders implements SortProvider{
	tile{
		public int compare(Renderable a, Renderable b){
			if(b.provider instanceof SortProviders){
				if(b.provider == SortProviders.tile){
					if(a.layer() == b.layer()) return 0;
					return a.layer() < b.layer() ? 1 : -1;
				}else{
					return -1;
				}
			}
			return 0;
		}
	},
	object{
		public int compare(Renderable a, Renderable b){
			if(a.provider instanceof SortProviders && b.provider instanceof SortProviders){
				if(b.provider == SortProviders.object){
					if(a.layer() == b.layer()) return 0;
					return a.layer() < b.layer() ? 1 : -1;
				}else{
					return 1;
				}
			}
			return 0;
		}
	};
}
