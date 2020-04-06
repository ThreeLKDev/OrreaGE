package threelkdev.orreaGE.core.rendering;

import org.lwjgl.opengl.GL11;

import threelkdev.orreaGE.core.rendering.openGL.objects.Attribute;
import threelkdev.orreaGE.core.rendering.openGL.objects.Vao;
import threelkdev.orreaGE.core.rendering.shaders.UiShader;
import threelkdev.orreaGE.core.ui.UiRenderData;
import threelkdev.orreaGE.tools.colours.Colour;
import threelkdev.orreaGE.tools.utils.OpenGLUtils;
import threelkdev.orreaGE.tools.utils.VaoUtils;

public class UiRenderer {
	
	private static final float[] POSITIONS = { 0, 0, 0, 1, 1, 0, 1, 1 };
	
	private final UiShader shader;
	private final Vao vao;
	
	public UiRenderer() {
		this.shader = new UiShader();
		this.vao = VaoUtils.createVao( POSITIONS, new Attribute( 0, GL11.GL_FLOAT, 2 ) );
	}
	
	public void render( Iterable< UiRenderData > uiData, int displayWidth, int displayHeight, float uiSize,
			int blurredSceneTexture ) {
		prepare( blurredSceneTexture );
		for( UiRenderData ui : uiData ) {
			renderUi( ui, displayWidth, displayHeight, uiSize );
		}
		endRendering();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
	private void prepare( int blurryImage ) {
		initOpenGLSettings();
		shader.start();
		OpenGLUtils.bindTextureToBank( blurryImage, 1 );
		vao.bind();
	}
	
	private void renderUi( UiRenderData ui, int displayWidth, int displayHeight, float uiSize ) {
		if( ui.getTexture() != null )
			OpenGLUtils.bindTextureToBank( ui.getTexture().getID(), 0 );
		setScissorTest( ui.getClippingBounds() );
		setUniformValues( ui, displayWidth, displayHeight, uiSize );
		GL11.glDrawArrays( GL11.GL_TRIANGLE_STRIP, 0, 4 );
	}
	
	private void setUniformValues( UiRenderData ui, int displayWidth, int displayHeight, float uiSize ) {
		shader.transform.loadVec4( ui.getX(), ui.getY(), ui.getWidth(), ui.getHeight() );
		setTextureUniforms( ui );
		setColourUniforms( ui.getOverrideColour() );
		setSizeUniforms( ui, displayWidth, displayHeight, uiSize );
		setBorderUniforms( ui );
	}
	
	private void endRendering() {
		disableOpenGLSettings();
		vao.unbind();
		shader.stop();
	}
	
	private void initOpenGLSettings() {
		OpenGLUtils.setStandardSettings( true, false, false );
		OpenGLUtils.enableAlphaBlending();
	}
	
	private void disableOpenGLSettings() {
		OpenGLUtils.disableScissorTest();
		OpenGLUtils.disableBlending();
	}
	
	private void setBorderUniforms( UiRenderData ui ) {
		if( ui.getBorderColour() != null )
			shader.borderColour.loadVec4( ui.getBorderColour() );
		shader.borderWidth.loadFloat( ui.getBorderWidth() );
	}
	
	private void setTextureUniforms( UiRenderData ui ) {
		shader.alpha.loadFloat( ui.getAlpha() );
		shader.useBlur.loadBoolean( ui.isBlurry() );
		shader.flipTexture.loadBoolean( ui.isFlippedTexture() );
		shader.useTexture.loadBoolean( ui.getTexture() != null );
	}
	
	private void setColourUniforms( Colour colour ) {
		shader.useOverrideColour.loadBoolean( colour != null );
		if( colour != null )
			shader.overrideColour.loadVec3( colour.getR(), colour.getG(), colour.getB() );
	}
	
	private void setSizeUniforms( UiRenderData ui, int displayWidth, int displayHeight, float uiSize ) {
		float pixelWidth = ui.getWidth() * displayWidth;
		shader.uiWidth.loadFloat( pixelWidth );
		float pixelHeight = ui.getHeight() * displayHeight;
		shader.uiHeight.loadFloat( pixelHeight );
		float roundedRadius = ui.getRoundedCornerRadius();
		if( roundedRadius <= 0 || uiSize < 1 ) {
			shader.radius.loadFloat( -1 );
			return;
		}
		float maxRadius = Math.min( pixelWidth, pixelHeight ) / 2f;
		roundedRadius = Math.min( maxRadius, roundedRadius * uiSize );
		shader.radius.loadFloat( roundedRadius );
	}
	
	private void setScissorTest( int[] bounds ) {
		if( bounds == null )
			OpenGLUtils.disableScissorTest();
		else
			OpenGLUtils.enableScissorTest( bounds[0], bounds[1], bounds[2], bounds[3] );
	}
}
