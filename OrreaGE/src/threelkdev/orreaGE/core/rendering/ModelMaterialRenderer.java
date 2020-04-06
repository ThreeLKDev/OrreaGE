package threelkdev.orreaGE.core.rendering;

import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import threelkdev.orreaGE.core.rendering.objects.ICamera;
import threelkdev.orreaGE.core.rendering.objects.Light;
import threelkdev.orreaGE.core.rendering.openGL.objects.Vao;
import threelkdev.orreaGE.tools.utils.OpenGLUtils;
import threelkdev.orreaGE.tools.utils.Pair;

public class ModelMaterialRenderer {
	
	private final ModelMaterialShader shader;
	
	public ModelMaterialRenderer() {
		this.shader = new ModelMaterialShader();
	}
	
	public void render( ICamera camera, Light light, ModelMaterialBatch... batches ) {
		shader.start();
		prepareEnvironment( camera, light );
		
		boolean usedTexture = false;
		for( ModelMaterialBatch batch : batches ) {
			batch.prepare();
			Map< Material, Pair< Vao, Integer > > materialMap = batch.getVaoMap();
			
			for( Entry< Material, Pair< Vao, Integer > > matEntry : materialMap.entrySet() ) {
				if( matEntry.getKey() != null ) {
					if( matEntry.getKey().texture != null ) {
						OpenGLUtils.bindTextureToBank( matEntry.getKey().texture.getID(), 0 );
						usedTexture = true;
					} else usedTexture = false;
					shader.loadMaterial( matEntry.getKey() );
				}
				Vao vao = matEntry.getValue().key;
				int passedInteger = matEntry.getValue().value;
				
				vao.bind();
				if( vao.indexBuffer != null )
					GL11.glDrawElements( GL11.GL_TRIANGLES, passedInteger, vao.indexDataType, 0 );
				else
					GL11.glDrawArrays( GL11.GL_TRIANGLES, 0, passedInteger );
				
				vao.unbind();
				if( usedTexture ) {
					matEntry.getKey().texture.unbindFromBank( 0 );
				}
			}
			
		}
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
	private void prepareEnvironment( ICamera camera, Light light ) {
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
