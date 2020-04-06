package threelkdev.orreaGE.core.rendering.openGL.objects;

import org.lwjgl.opengl.GL20;

public class UniformInt extends Uniform {
	
	private int value;
	private boolean used = false;
	
	public UniformInt( String name ) {
		super( name );
	}
	
	public void loadInt( int value ) {
		if( used && value == this.value )
			return;
		GL20.glUniform1i( super.getLocation(), value );
		used = true;
		this.value = value;
	}
}
