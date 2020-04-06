package threelkdev.orreaGE.core.rendering.openGL.objects;

import org.lwjgl.opengl.GL20;

public class UniformSampler extends Uniform {
	
	private int currentValue;
	private boolean used = false;
	
	public UniformSampler( String name ) {
		super( name );
	}
	
	public void loadTexUnit( int texUnit ) {
		if( used && currentValue == texUnit )
			return;
		GL20.glUniform1i( super.getLocation(), texUnit );
		this.used = true;
		this.currentValue = texUnit;
	}
	
	
}
