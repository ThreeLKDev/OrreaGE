package threelkdev.orreaGE.core.rendering;

import org.lwjgl.opengl.GL11;

import threelkdev.orreaGE.core.rendering.objects.ICamera;
import threelkdev.orreaGE.core.rendering.objects.Light;
import threelkdev.orreaGE.tools.utils.OpenGLUtils;

public class ModelRenderer {
	
	private final ModelShader shader;
	
	public ModelRenderer() {
		this.shader = new ModelShader();
	}
	
	public void render( TestModelBatch batch, ICamera camera, Light light ) {
		shader.start();
		prepare( camera, light );
		
		batch.prepVao();
		batch.getVao().bind();
		
		if( batch.getVao().indexBuffer != null )
			GL11.glDrawElements( GL11.GL_TRIANGLES, batch.getIndexCount(), batch.getVao().indexDataType, 0 );
		else
			GL11.glDrawArrays( GL11.GL_TRIANGLES, 0, batch.getVertexCount() );
		
		batch.getVao().unbind();
		
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
	private void prepare( ICamera camera, Light light ) {
		doGeneralSettings();
		shader.getProjectionViewMatrix().loadMatrix( camera.getProjectionViewMatrix() );
		shader.getLightBias().loadVec2( light.getBias() );
		shader.getLightColour().loadVec3( light.getColour().getVector() );
		shader.getLightDirection().loadVec3( light.getDirection() );
	}
	
	private void doGeneralSettings() {
		OpenGLUtils.cullBackFaces( true );
		OpenGLUtils.enableAlphaBlending();
		OpenGLUtils.enableDepthTest( true );
		OpenGLUtils.antialias( true );
	}
	
}
