package threelkdev.orreaGE.core.rendering.openGL.objects;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;

public class UniformVec2 extends Uniform {
	
	private float x, y;
	private boolean used = false;
	
	public UniformVec2( String name ) {
		super( name );
	}
	
	public void loadVec2( Vector2f vector ) {
		loadVec2( vector.x, vector.y );
	}
	
	public void loadVec2( float x, float y ) {
		if( used && this.x == x && this.y == y )
			return;
		this.used = true;
		this.x = x;
		this.y = y;
		GL20.glUniform2f( super.getLocation(), x, y );
	}
	
}
