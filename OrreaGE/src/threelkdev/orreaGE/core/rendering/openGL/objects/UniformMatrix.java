package threelkdev.orreaGE.core.rendering.openGL.objects;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

public class UniformMatrix extends Uniform {
	
	private static final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer( 16 );
	
	public UniformMatrix( String name ) {
		super( name );
	}
	
	public void loadMatrix( Matrix4f matrix ) {
		matrix.store( matrixBuffer );
		matrixBuffer.flip();
		GL20.glUniformMatrix4fv( super.getLocation(), false, matrixBuffer );
	}
	
}
