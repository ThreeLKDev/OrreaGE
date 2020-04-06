package threelkdev.orreaGE.core.rendering.openGL.objects;

import org.lwjgl.opengl.GL20;

public class UniformBoolean extends Uniform {
	
	private boolean currentBool;
	private boolean used = false;
	
	public UniformBoolean( String name ) {
		super( name );
	}
	
	public void loadBoolean( boolean bool ) {
		if( used && currentBool == bool )
			return;
		this.used = true;
		this.currentBool = bool;
		GL20.glUniform1f( super.getLocation(), bool ? 1f : 0 );
	}
}
