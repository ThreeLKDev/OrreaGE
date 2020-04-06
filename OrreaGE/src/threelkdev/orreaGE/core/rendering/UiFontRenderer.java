package threelkdev.orreaGE.core.rendering;

import org.lwjgl.opengl.GL11;

import threelkdev.orreaGE.core.rendering.openGL.objects.Attribute;
import threelkdev.orreaGE.core.rendering.openGL.objects.Vao;
import threelkdev.orreaGE.core.rendering.shaders.UiFontShader;
import threelkdev.orreaGE.core.ui.RenderLevelData;
import threelkdev.orreaGE.core.ui.UiRenderBundle;
import threelkdev.orreaGE.tools.utils.OpenGLUtils;
import threelkdev.orreaGE.tools.utils.VaoUtils;

public class UiFontRenderer {

	private static final float[] POSITIONS = { 0, 0, 0, 1, 1, 0, 1, 1 };
	
	private final UiFontShader shader;
	private final Vao vao;
	
	public UiFontRenderer() {
		shader = new UiFontShader();
		this.vao = VaoUtils.createVao( POSITIONS, new Attribute( 0, GL11.GL_FLOAT, 2 )) ;
	}
	
	public void render( UiRenderBundle renderBundle, int displayWidth, int displayHeight, float uiSizeFactor ) {
		for( RenderLevelData levelData : renderBundle.getRenderData() ) {
//			for()
		}
	}
	
	private void prepare( int displayWidth, int displayHeight ) {
		OpenGLUtils.setStandardSettings( false, false, false );
		OpenGLUtils.enableAlphaBlending();
		shader.start();
		shader.displaySize.loadVec2( displayWidth, displayHeight ); // div both by uiSizeFactor?
	}
	
	
}
