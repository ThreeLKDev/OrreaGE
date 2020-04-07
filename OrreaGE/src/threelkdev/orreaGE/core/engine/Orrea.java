package threelkdev.orreaGE.core.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFWImage.Buffer;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import threelkdev.orreaGE.core.inputs.Keyboard;
import threelkdev.orreaGE.core.inputs.Mouse;
import threelkdev.orreaGE.core.loading.BackgroundLoader;
import threelkdev.orreaGE.core.loading.IconLoader;
import threelkdev.orreaGE.core.ui.UiMaster;
import threelkdev.orreaGE.core.windows.Window;
import threelkdev.orreaGE.tools.errors.ErrorManager;
import threelkdev.orreaGE.tools.files.FileUtils;
import threelkdev.orreaGE.tools.lang.GameText;

public class Orrea {
	
	private final Window window;
	private final Mouse mouse;
	private final Keyboard keyboard;
	private final Timer timer;
	
	private final Map< Integer, List< Runnable > > keyPressListeners, keyReleaseListeners, keyListeners;
	
	public static Orrea instance;
	
	private Orrea( Window window, Mouse mouse, Keyboard keyboard, Timer timer ) {
		this.window = window;
		this.mouse = mouse;
		this.keyboard = keyboard;
		this.timer = timer;
		this.keyListeners = new HashMap< Integer, List< Runnable > >();
		this.keyPressListeners = new HashMap< Integer, List< Runnable > >();
		this.keyReleaseListeners = new HashMap< Integer, List< Runnable > >();
		instance = this;
	}
	
	public static Orrea init( EngineConfigs configs ) {
		ErrorManager.init( EngineFiles.ERROR_FOLDER );
		GameText.init( EngineFiles.LANG_FILE, configs.languageId );
		BackgroundLoader.init();
		Window window = setUpWindow( configs );
		Keyboard keyboard = new Keyboard( window.getId() );
		Mouse mouse = new Mouse( window );
		Timer timer = new Timer( window.getFps() );
		registerResourceFolders();
		initOpenGl( window.getPixelWidth(), window.getPixelHeight() );
		initUiMaster( window, mouse, configs.windowMinWidth, configs.windowMinHeight, configs.UiSize );
		return new Orrea( window, mouse, keyboard, timer );
	}
	
	private static void registerResourceFolders() {
		FileUtils.registerPath( FileUtils.ResourceType.SHADER, "res/ThreeLKDev/shaders" );
		FileUtils.registerPath( FileUtils.ResourceType.MODEL, "res/ThreeLKDev/models" );
		FileUtils.registerPath( FileUtils.ResourceType.TEXTURE, "res/ThreeLKDev/textures" );
		FileUtils.registerPath( FileUtils.ResourceType.SOUND, "res/ThreeLKDev/sounds" );
		FileUtils.registerPath( FileUtils.ResourceType.OTHER, "res/ThreeLKDev" );
	}
	
	public Window getWindow() {
		return window;
	}
	
	public Keyboard getKeyboard() {
		return keyboard;
	}
	
	public Mouse getMouse() {
		return mouse;
	}
	
	public float getDeltaSeconds() {
		return timer.getDelta();
	}
	
	public void update() {
		UiMaster.update( timer.getDelta() );
		BackgroundLoader.update();
		keyboard.update();
		mouse.update();
		window.update();
		timer.update();
		
		for( int key : keyListeners.keySet() )
			if( keyboard.isKeyDown( key ) ) 
				for( Runnable run : keyListeners.get( key ) ) 
					run.run();
		for( int key : keyPressListeners.keySet() )
			if( keyboard.keyPressEvent( key ) )
				for( Runnable run : keyPressListeners.get( key ) )
					run.run();
		for( int key : keyReleaseListeners.keySet() )
			if( keyboard.keyReleaseEvent( key ) )
				for( Runnable run : keyReleaseListeners.get( key ) )
					run.run();
	}
	
	public void addKeyListener( int key, Runnable run ) {
		if( !keyListeners.containsKey( key ) )
			keyListeners.put( key, new ArrayList< Runnable >() );
		keyListeners.get( key ).add( run );
	}
	
	public void addKeyPressListener( int key, Runnable run ) {
		if( !keyPressListeners.containsKey( key ) )
			keyPressListeners.put( key,  new ArrayList< Runnable >() );
		keyPressListeners.get( key ).add( run );
	}
	
	public void addKeyReleaseListener( int key, Runnable run ) {
		if( !keyReleaseListeners.containsKey( key ) )
			keyReleaseListeners.put( key, new ArrayList< Runnable >() );
		keyReleaseListeners.get( key ).add( run );
	}
	
	
	public void removeKeyListener( int key, Runnable run ) {
		if( keyListeners.containsKey( key ) )
			keyListeners.get( key ).remove( run );
	}
	
	public void removeKeyPressListener( int key, Runnable run ) {
		if( keyPressListeners.containsKey( key ) )
			keyPressListeners.get( key ).remove( run );
	}
	
	public void removeKeyReleaseListener( int key, Runnable run ) {
		if( keyReleaseListeners.containsKey( key ) )
			keyReleaseListeners.get( key ).remove( run );
	}

	public void cleanUp() {
		BackgroundLoader.cleanUp();
		UiMaster.cleanUp();
		window.destroy();
	}
	
	private static void initOpenGl( int pixelWidth, int pixelHeight ) {
		GL.createCapabilities();
		GL11.glViewport( 0,  0, pixelWidth, pixelHeight );
	}
	
	private static Window setUpWindow( EngineConfigs configs ) {
		Buffer buffer = null;
		if ( EngineFiles.ICON_FOLDER.exists() ) {
			buffer = IconLoader.loadIcon( EngineFiles.ICON_FOLDER.listFiles() );
		}
		Window window = Window.newWindow( configs.windowWidth, configs.windowHeight, configs.windowTitle )
				.setVsync( configs.vsync ).fullscreen( configs.fullscreen ).withIcon( buffer ).setMSAA( configs.isMSAA ).setFps( configs.fps )
				.create();
		window.addSizeChangeListener( ( width, height ) -> GL11.glViewport( 0,  0, width, height ) );
		if( buffer != null )
			buffer.close();
		return window;
	}
	
	private static void initUiMaster( Window window, Mouse mouse, int minWidth, int minHeight, float uiSize ) {
		UiMaster.init( mouse, window.getPixelWidth(), window.getPixelHeight(), uiSize );
		UiMaster.setMinDisplaySize( minWidth, minHeight );
		window.addSizeChangeListener( ( width, height ) -> UiMaster.notifyScreenSizeChange( width, height ) );
	}
}
