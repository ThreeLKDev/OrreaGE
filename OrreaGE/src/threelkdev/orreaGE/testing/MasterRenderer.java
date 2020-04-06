package threelkdev.orreaGE.testing;

import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import threelkdev.orreaGE.core.engine.Orrea;
import threelkdev.orreaGE.core.rendering.StaticRenderer;
import threelkdev.orreaGE.core.rendering.objects.ICamera;
import threelkdev.orreaGE.core.rendering.objects.Light;
import threelkdev.orreaGE.core.rendering.openGL.objects.IStaticBatch;
import threelkdev.orreaGE.core.rendering.terrain.TerrainMesh;
import threelkdev.orreaGE.core.rendering.terrain.TerrainRenderer;
import threelkdev.orreaGE.tools.colours.Colour;

public class MasterRenderer {
	
	private StaticRenderer staticRenderer;
	private TerrainRenderer terrainRenderer;
	
	public MasterRenderer() {
		this.staticRenderer = new StaticRenderer();
		this.terrainRenderer = new TerrainRenderer();
	}
	
	private static final Colour col1 = new Colour( 121, 205, 106, true );
	private static final Colour col2 = new Colour( 153, 101, 63, true );
	float blend = 0;
	
	public void render( TerrainMesh terrain, List< IStaticBatch > batches, ICamera camera, Light light ) {
		GL11.glClearColor( 0.3f, 0.33f, 0.36f, 1 );
		GL11.glClear( GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT );
		staticRenderer.render( batches, camera, light, true );
		if( Orrea.instance.getKeyboard().isKeyDown( GLFW.GLFW_KEY_R ) )
			blend += 0.005f;
		else if( Orrea.instance.getKeyboard().isKeyDown( GLFW.GLFW_KEY_E ) )
			blend -= 0.005f;
		if( terrain != null ) {
			terrain.setGrassColour( Colour.interpolateColours( col1, col2, blend, null ) );
			terrainRenderer.render( terrain, camera, light );
		}
	}
	
	public void cleanUp() {
		staticRenderer.cleanUp();
		terrainRenderer.cleanUp();
	}
	
}
