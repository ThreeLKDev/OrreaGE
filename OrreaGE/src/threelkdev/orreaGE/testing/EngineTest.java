package threelkdev.orreaGE.testing;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import threelkdev.orreaGE.core.engine.EngineConfigs;
import threelkdev.orreaGE.core.engine.Orrea;
import threelkdev.orreaGE.core.inputs.Keyboard;
import threelkdev.orreaGE.core.loading.BackgroundLoader;
import threelkdev.orreaGE.core.loading.CsvModelLoader;
import threelkdev.orreaGE.core.rendering.Material;
import threelkdev.orreaGE.core.rendering.Model;
import threelkdev.orreaGE.core.rendering.ModelInstance;
import threelkdev.orreaGE.core.rendering.ModelMaterialBatch;
import threelkdev.orreaGE.core.rendering.ModelMaterialRenderer;
import threelkdev.orreaGE.core.rendering.objects.Camera;
import threelkdev.orreaGE.core.rendering.objects.Light;
import threelkdev.orreaGE.core.rendering.openGL.objects.IStaticBatch;
import threelkdev.orreaGE.core.rendering.openGL.textures.Texture;
import threelkdev.orreaGE.core.rendering.terrain.TerrainMesh;
import threelkdev.orreaGE.core.ui.UiMaster;
import threelkdev.orreaGE.core.windows.Window;
import threelkdev.orreaGE.tools.files.FileUtils;
import threelkdev.orreaGE.tools.files.FileUtils.ResourceType;
import threelkdev.orreaGE.tools.math.RollingAverage;
import threelkdev.orreaGE.tools.pools.Vec3Pool;
import threelkdev.orreaGE.tools.pools.Vec4Pool;
import threelkdev.orreaGE.tools.utils.OpenGLUtils;
import threelkdev.orreaGE.worldgen.FlatMeshGenerator;

public class EngineTest {
	
//	static Orrea SHARD;
	
	private static TerrainMesh terrain;
	
	public static float RAD = 5;
	
	static RollingAverage fpsCounter;

	
	static ModelInstance target = null;
	static ModelInstance[] targets = new ModelInstance[ 8 ];
	static GameObject[] objs = new GameObject[ 8 ];
	public static void main( String[] args ) {
		
		fpsCounter = new RollingAverage( 16 );
		
		EngineConfigs configs = EngineConfigs.getDefaultConfigs();
		configs.windowTitle = "Orrea Test";
		configs.vsync = false;
		configs.fps = 240;
		configs.UiSize = 1;
		configs.windowMinHeight = 0;
		configs.windowMinWidth = 0;
		
		Orrea.init( configs );
		Window window = Orrea.instance.getWindow();
		
		UiTest.createUI();
		
		MasterRenderer renderer = new MasterRenderer();
		List< IStaticBatch > batches = new ArrayList< IStaticBatch >();
		
		
		Camera camera = new Camera( Orrea.instance.getMouse(), Orrea.instance.getWindow() );
		Light light = new Light( 0.4f, 0.6f );
		light.setDirection( new Vector3f( 0.6f, -1, 0 ) );
		
		BackgroundLoader.addGeneralRequest( () -> 
			terrain = new FlatMeshGenerator().generate()
		); 
		
		//
		Model test = Model.getFlatHedron( 5, 1f, 0.5f );
		Material modelMat = new Material( null );
		modelMat.setDiffuse( new Vector3f( 0.5f, 0.5f, 0.5f ) );
		modelMat.setAlpha( 1f );
		test.defaultMaterial = modelMat;
		ModelMaterialBatch batch = new ModelMaterialBatch( test );
		Model cube = Model.getCube();
		Material cubeMat = new Material( Texture.newTexture( FileUtils.getResource( ResourceType.TEXTURE, "TestCubeTex1024.png" ) ).nearestFiltering().clampEdges().create() );
		cubeMat.setDiffuse( new Vector3f( 1f, 1f, 1f ) );
		cube.defaultMaterial = cubeMat;
		ModelMaterialBatch cubeBatch = new ModelMaterialBatch( cube );
		ModelInstance cubeInstance = new ModelInstance( cube );
		cubeInstance.transform.translate( new Vector3f( 50, 3, 50 ) );
		System.out.println(" Pos: " + cubeInstance.getPosition( null ).toString() );
		cubeBatch.addInstance( cubeInstance );
		target = new ModelInstance( test );
		batch.addInstance( target );
		
		for( int i = 0; i < 20; i++ ) {
			Vector4f colour = Vec4Pool.getRandom();
			Material instanceMat = new Material( 
					i == 0
					? Texture.newTexture( FileUtils.getTexture( "TestSpiralTex.png" ) ).nearestFiltering().clampEdges().create()
					: null );
			instanceMat.setDiffuse( new Vector3f( colour ) );
//			instanceMat.setAlpha( colour.w );
			for( int j = 0; j < 50; j++ ) {
				ModelInstance instance = new ModelInstance( test );
				
				Vector3f pos = Vec3Pool.getRandom( 100f );
				Vector3f rot = Vec3Pool.getRandom( ( float ) Math.PI * 2f);
				instance.transform.translate( pos )
				.rotate( rot.x, new Vector3f( 1, 0, 0 ) )
				.rotate( rot.y, new Vector3f( 0, 1, 0 ) )
				.rotate( rot.z, new Vector3f( 0, 0, 1 ) );
				
				Vec3Pool.release( pos );
				Vec3Pool.release( rot );
				
				instance.setMaterial( instanceMat );
				batch.addInstance( instance );
				
				if( i == 3 && j == 4 )
					target = instance;
				
				if( i < 8 && j == 0  ) {
					targets[ j ] = instance;
					objs[ i ] = new GameObject( instance ) {
						boolean run = false;
						
						@Override
						public void onInit() {
							Orrea.instance.addKeyPressListener( GLFW.GLFW_KEY_H, () -> {
								run = true;
							} );
						}
						Vector3f move = new Vector3f();
						Vector3f temp = new Vector3f();
						@Override
						public void update( float delta ) {
							if( run ) {
								if( this.model.moveTowards( move, delta, temp) )
									run = false;
							}
						}
					};
				}
				
			}
			
			Vec4Pool.release( colour );
			
		}
		
		CsvModelLoader cml = new CsvModelLoader();
		Model tree = cml.loadModel( FileUtils.getModel( "polyTree.txt" ) );
		Material treeMat = new Material( null );
		treeMat.setDiffuse( new Vector3f( 0.5f, 0.5f, 0.5f ) );
		tree.setDefaultMaterial( treeMat );
		ModelMaterialBatch treeBatch = new ModelMaterialBatch( tree );
		
		for( int i = 0; i < 30; i++ ) {
			ModelInstance instance = new ModelInstance( tree );
			Vector4f rand = Vec4Pool.getRandom();
			rand.w = 0.4f + rand.w * 0.4f * 0.01f;
			Vector3f pos = Vec3Pool.get( rand.x * 100f, 0, rand.z * 100f );
			Vector3f rot = Vec3Pool.get( 0, 1, 0 );
			System.out.println( pos );
			instance.transform.rotate( rand.y, rot );
			instance.transform.translate( pos );
			instance.transform.scale( new Vector3f( rand.w, rand.w, rand.w ) );
			
			Vec3Pool.release( pos );
			Vec3Pool.release( rot );
			Vec4Pool.release( rand );
			treeBatch.addInstance( instance );
		}
		
		ModelInstance zero = new ModelInstance( tree );
		zero.transform.translate( new Vector3f( 100f, 0, 100f ) );
		treeBatch.addInstance( zero );
		
		ModelMaterialRenderer modelRenderer = new ModelMaterialRenderer();
		//
		
//		GameMenu gameMenu = new GameMenu();
//		UiMaster.add( gameMenu, ConstraintFactory.getFill() );
		
		while( !Orrea.instance.getWindow().closeButtonPressed() ) {
			OpenGLUtils.clearFrameBuffer();
			Keyboard keyboard = Orrea.instance.getKeyboard();
			
			fpsCounter.addValue( 1f / Orrea.instance.getDeltaSeconds() );
			UiTest.displayFPS( ( int ) fpsCounter.calculate(), Orrea.instance.getDeltaSeconds() );
			
			for( GameObject go : objs ) {
				if( go != null )go.update( 0.01f );
			}
			
			if( keyboard.keyPressEvent( GLFW.GLFW_KEY_F ) ) {
				window.goFullscreen( !window.isFullscreen() );
			}
			
			if( keyboard.isKeyDown( GLFW.GLFW_KEY_UP ) )
				UiMaster.setManualUiSize( UiMaster.getUiSize() + 0.0045f );
			else if( keyboard.isKeyDown( GLFW.GLFW_KEY_DOWN ) )
				UiMaster.setManualUiSize( UiMaster.getUiSize() - 0.0045f );
			
			camera.move();
			renderer.render( terrain, batches, camera, light );
			modelRenderer.render( camera, light, batch , cubeBatch, treeBatch );
			Orrea.instance.update();
			
			if( Orrea.instance.getKeyboard().keyPressEvent( GLFW.GLFW_KEY_ESCAPE ) ) {
//				gameMenu.display( !gameMenu.isDisplayed() );
			}
		}
		renderer.cleanUp();
		modelRenderer.cleanUp();
		Orrea.instance.cleanUp();
		
	}
	
}
