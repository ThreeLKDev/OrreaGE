package threelkdev.orreaGE.testing.ui;

import org.lwjgl.opengl.GL11;

import threelkdev.orreaGE.core.rendering.objects.ICamera;
import threelkdev.orreaGE.tools.utils.OpenGLUtils;

public class QuadRenderer {
	
	private final QuadShader shader;
	
	public QuadRenderer() {
		this.shader = new QuadShader();
	}
	
	public void render( Quad quad, ICamera camera ) {
		if( !quad.isLoaded() )
			return;
		
		shader.start();
		prepare( camera, quad );
		quad.getVao().bind();
		if( quad.getVao().indexBuffer != null )
			GL11.glDrawElements( GL11.GL_TRIANGLES, Quad.INDICES_COUNT, quad.getVao().indexDataType, 0 );
		else
			GL11.glDrawArrays( GL11.GL_TRIANGLES, 0, Quad.VERTEX_COUNT);
		quad.getVao().unbind();
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
	private void prepare( ICamera camera, Quad quad ) {
		doGeneralSettings();
		shader.getProjectionViewMatrix().loadMatrix( camera.getProjectionViewMatrix() );
	}
	
	private void doGeneralSettings() {
		OpenGLUtils.cullBackFaces( false );
		OpenGLUtils.enableAlphaBlending();
		OpenGLUtils.enableDepthTest( true );
		OpenGLUtils.antialias( true );
	}
	
}
