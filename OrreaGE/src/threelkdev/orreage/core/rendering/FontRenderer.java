package threelkdev.orreage.core.rendering;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import threelkdev.orreage.core.rendering.shaders.FontShader;
import threelkdev.orreage.core.ui.text.Font;
import threelkdev.orreage.core.ui.text.Text;
import threelkdev.orreage.core.ui.text.TextMesh;
import threelkdev.orreage.tools.colours.Colour;
import threelkdev.orreage.tools.utils.OpenGLUtils;

public class FontRenderer {
	
	private final FontShader shader;
	
	public FontRenderer() {
		shader = new FontShader();
	}
	
	public void render( Map< Font, List< Text > > texts, int displayWidth, int displayHeight ) {
		prepare( displayWidth, displayHeight );
		for( Entry< Font, List< Text > > fontTexts : texts.entrySet() ) {
			fontTexts.getKey().getFontAtlas().bindToBank( 0 );
			for( Text text : fontTexts.getValue() )
				renderText( text );
		}
		endRendering();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
	private void prepare( int displayWidth, int displayHeight ) {
		OpenGLUtils.setStandardSettings( false, false, false );
		OpenGLUtils.enableAlphaBlending();
		shader.start();
		shader.getDisplaySize().loadVec2( displayWidth, displayHeight );
	}
	
	private void endRendering() {
		shader.stop();
		OpenGLUtils.disableBlending();
	}
	
	private void renderText( Text text ) {
		if( text.isEmpty() )
			return;
		TextMesh mesh = text.getMesh();
		mesh.getVao().bind();
		setScissorTest( text.getClippingBounds() );
		shader.getTransform().loadVec3( text.getAbsX(), text.getAbsY(), text.getTextScale() );
		Colour colour = text.getColour();
		shader.getColour().loadVec4( colour.getR(), colour.getG(), colour.getB(), text.getTotalAlpha() );
		GL11.glDrawArrays( GL11.GL_TRIANGLES, 0, mesh.getVertexCount() );
		mesh.getVao().unbind();
	}
	
	private void setScissorTest( int[] bounds ) {
		if( bounds == null )
			OpenGLUtils.disableScissorTest();
		else
			OpenGLUtils.enableScissorTest( bounds[ 0 ], bounds[ 1 ], bounds[ 2 ], bounds[ 3 ] );
	}
	
}
