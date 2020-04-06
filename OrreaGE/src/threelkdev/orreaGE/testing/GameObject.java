package threelkdev.orreaGE.testing;

import threelkdev.orreaGE.core.rendering.ModelInstance;

public class GameObject {
	
	public final ModelInstance model;
	
	public GameObject( ModelInstance model ) {
		this.model = model;
		this.onInit();
	}
	
	public void onInit() {
		
	}
	
	public void update( float delta ) {
		
	}
	
}
