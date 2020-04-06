package threelkdev.orreaGE.core.rendering.openGL.objects;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector4f;

import threelkdev.orreaGE.tools.colours.Colour;

public class UniformVec4 extends Uniform {
	
	public UniformVec4( String name ) {
		super( name );
	}
	
	public void loadVec4( Vector4f vector ) {
		loadVec4( vector.x, vector.y, vector.z, vector.w );
	}
	
	public void loadVec4( Colour colour ) {
		loadVec4( colour.getR(), colour.getG(), colour.getB(), colour.getA() );
	}
	
	public void loadVec4( float x, float y, float z, float w ) {
		GL20.glUniform4f( super.getLocation(), x, y, z, w );
	}
	
}
