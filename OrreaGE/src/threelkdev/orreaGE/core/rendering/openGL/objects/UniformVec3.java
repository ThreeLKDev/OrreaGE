package threelkdev.orreaGE.core.rendering.openGL.objects;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;

public class UniformVec3 extends Uniform {

	private float x,y,z;
	private boolean used = false;
	
	public UniformVec3( String name ) {
		super( name );
	}
	
	public void loadVec3( Vector3f vector ) {
		loadVec3( vector.x, vector.y, vector.z );
	}
	
	public void loadVec3( float x, float y, float z ) {
		if( used && x == this.x && y == this.y && z == this.z )
			return;
		this.used = true;
		this.x = x;
		this.y = y;
		this.z = z;
		GL20.glUniform3f( super.getLocation(), x, y, z );
	}
	
}
