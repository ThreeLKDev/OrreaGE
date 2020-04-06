package threelkdev.orreaGE.core.rendering;

public abstract class AModelMaterialBatch {

	public abstract void notifyMaterialChange( ModelInstance instance, Material from, Material to );
	public abstract void notifyTransform( ModelInstance instance );
	
	protected abstract void onAddInstance( ModelInstance instance );
	protected abstract void onRemoveInstance( ModelInstance instance );
	
	public void addInstance( ModelInstance instance ) {
		instance.notifyBatch( this );
		onAddInstance( instance );
	}
	
	public void removeInstance( ModelInstance instance ) {
		instance.notifyBatch( null );
		onRemoveInstance( instance );
	}
	
}
