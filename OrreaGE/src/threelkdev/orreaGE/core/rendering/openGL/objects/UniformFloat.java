package threelkdev.orreaGE.core.rendering.openGL.objects;

import org.lwjgl.opengl.GL20;

public class UniformFloat extends Uniform {
	
	private float value;
	private boolean used = false;
	
	public UniformFloat( String name ) {
		super( name );
	}
	
	public void loadFloat( float value ) {
		if( used && value == this.value )
			return;
		GL20.glUniform1f( super.getLocation(), value );
		used = true;
		this.value = value;
	}
	
}
