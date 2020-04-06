package threelkdev.orreaGE.core.rendering;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Trivial Model instance class containing a transformation matrix and rendering material.
 * @author Luca Kieran
 *
 */
public class ModelInstance {
	
	private final Model model;
	private boolean visible = true;
	private Material material;
	
	public Matrix4f transform;
	
	private boolean transformed = false;
	
	private AModelMaterialBatch batch;
	
	public ModelInstance( Model model ) { this( model, new Matrix4f() ); }
	public ModelInstance( Model model, Matrix4f transform ) { this( model, transform, null ); }
	public ModelInstance( Model model, Matrix4f transform, Material material ) {
		this.model = model;
		this.transform = transform;
		this.material = material;
	}
	
	/**
	 * Notifies this instance that it has been assigned to a batch, allowing it to notify the batch of any later transformations or material changes.
	 */
	protected void notifyBatch( AModelMaterialBatch batch ) {
		this.batch = batch;
	}
	
	/**
	 * Sets this instance's material, notifying its associated batch ( if one exists ) of this change.
	 */
	public void setMaterial( Material material ) {
		if( batch != null )
			batch.notifyMaterialChange( this, this.material, material );
		this.material = material;
	}
	
	/**
	 * Sets this instance's 'transformed' value to true, and notifies its associated batch ( if one exists ) of this change.
	 */
	private void notifyTransformed() {
		this.transformed = true;
		if( batch != null )
			batch.notifyTransform( this );
	}
	
	/**
	 * Translates this ModelInstance along the given Vector, relative to local space. <br />
	 * See {@link #translateWorld(Vector3f)} for translations relative to world space.
	 * @param movement - The Vector applied to this ModelInstance's transformation matrix.
	 */
	public void translateLocal( Vector3f movement ) {
		this.transform.translate( movement );
		this.translateWorld( movement );
		notifyTransformed();
	}
	
	/**
	 * Translates this ModelInstance along the given Vector, relative to world space. <br />
	 * See {@link #translateLocal(Vector3f)} for translations relative to local space.
	 * @param movement - The Vector applied to this ModelInstance's transformation matrix.
	 */
	public void translateWorld( Vector3f move ) {
		this.transform.m30 += move.x;
		this.transform.m31 += move.y;
		this.transform.m32 += move.z;
		notifyTransformed();
	}
	
	/**
	 * Translates this ModelInstance towards the given target, by the given distance.<br />
	 * @param target - The target to translate towards
	 * @param step - The maximum distance to translate by ( if the distance to {@code target } is less than this value, the ModelInstance will instead stop <i>at</i> the target )
	 * @param temp - A utility vector for performing the calculations, in order to avoid 'moving' the target. This vector will be modified.
	 * @return True if this ModelInstance is now at the target, false otherwise.
	 */
	public boolean moveTowards( Vector3f target, float step, Vector3f temp ) {
		if( temp == null )
			temp = new Vector3f();
		boolean result = true;
		temp.x = target.x - transform.m30;
		temp.y = target.y - transform.m31;
		temp.z = target.z - transform.m32;
		float tempLengthSquared = temp.lengthSquared();
		if( tempLengthSquared == 0 ) return true;
		if( !( tempLengthSquared <= step * step ) ) {
			temp.normalise();
			temp.scale( step );
			result = false;
		}
		translateWorld( temp );
		return result;
	}
	
	/**
	 * Retrieves the position of this ModelInstance's transform. ( The m30, m31, and m32 floats of a standard 4x4 matrix as x, y, and z, respectively )
	 * @param dest - The destination vector, or null if a new one is to be created.
	 * @return - The position
	 */
	public Vector3f getPosition( Vector3f dest ) { 
		if( dest == null )
			dest = new Vector3f();
		dest.set( transform.m30, transform.m31, transform.m32 );
		return dest;
	}
	
	/** Whether this ModelInstance has undergone a transformation since the last check. */
	protected boolean wasTransformed() { return this.transformed; }
	
	/** Resets this ModelInstance's {@code transformed } boolean to false, signifying that its transformation has been loaded. */
	protected void transformNoted() { this.transformed = false; }
	
	/** Gets this ModelInstance's {@link Material }*/
	public Material getMaterial() { return this.material; }
	
	/** The {@link Model } that this is an instance of. */
	public Model getModel() { return this.model; }
	
	/** If this ModelInstance should be rendered ( when applicable ). */
	public boolean isVisible() { return visible; }
	
	/** Disables or enables rendering of this ModelInstance. */
	public void setVisible( boolean visible ) { this.visible = visible; }
}
