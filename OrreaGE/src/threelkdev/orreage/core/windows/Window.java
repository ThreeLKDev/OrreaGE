package threelkdev.orreage.core.windows;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

public class Window {
	
	private final long id;
	private final Sync frameSync;
	
	private int pxWidth, pxHeight;
	private int desiredWidth, desiredHeight;
	private int widthScreenCoords, heightScreenCoords;
	
	private boolean fullscreen;
	private boolean vsync;
	private int fps;
	
	private List< WindowSizeListener > listeners = new ArrayList< WindowSizeListener >();
	
	public static WindowBuilder newWindow( int width, int height, String title ) {
		return new WindowBuilder( width, height, title );
	}
	
	protected Window( long id, int desiredWidth, int desiredHeight, int fps, boolean fullscreen, boolean vsync ) {
		this.id = id;
		this.desiredHeight = desiredHeight;
		this.desiredWidth = desiredWidth;
		this.fullscreen = fullscreen;
		this.vsync = vsync;
		this.fps = fps;
		this.frameSync = new Sync( fps );
		getInitialWindowSizes();
		addScreenSizeListener();
		addPixelSizeListener();
	}
	
	public long getId() {
		return id;
	}
	
	public int getScreenCoordWidth() {
		return widthScreenCoords;
	}
	
	public int getScreenCoordHeight() {
		return heightScreenCoords;
	}
	
	public int getFps() {
		return fps;
	}
	
	public float getAspectRatio() {
		return ( float ) pxWidth / pxHeight;
	}
	
	public int getPixelWidth() {
		return pxWidth;
	}
	
	public int getPixelHeight() {
		return pxHeight;
	}
	
	public boolean isFullscreen() {
		return fullscreen;
	}
	
	public boolean isVsync() {
		return vsync;
	}
	
	public void setFps( int fps ) {
		frameSync.setFps( fps );
	}
	
	public void addSizeChangeListener( WindowSizeListener listener ) {
		listeners.add( listener );
	}
	
	public void update() {
		glfwSwapBuffers( id );
		glfwPollEvents();
		frameSync.sync();
	}
	
	public boolean closeButtonPressed() {
		return glfwWindowShouldClose( id );
	}
	
	public void destroy() {
		glfwFreeCallbacks( id );
		glfwDestroyWindow( id );
		glfwTerminate();
		glfwSetErrorCallback( null ).free();
	}
	
	public void setVsync( boolean vsync ) {
		this.vsync = vsync;
		glfwSwapInterval( vsync ? 1 : 0 );
	}
	
	public void goFullscreen( boolean fullscreen ) {
		long monitor = glfwGetPrimaryMonitor();
		GLFWVidMode vidMode = glfwGetVideoMode( monitor );
		if( fullscreen ) {
			switchToFullscreen( monitor, vidMode );
		} else {
			switchToWindowed( vidMode );
		}
		this.fullscreen = fullscreen;
	}
	
	private void switchToFullscreen( long monitor, GLFWVidMode vidMode ) {
		this.desiredWidth = widthScreenCoords;
		this.desiredHeight = heightScreenCoords;
		glfwSetWindowMonitor( id, monitor, 0, 0, vidMode.width(), vidMode.height(), vidMode.refreshRate() );
	}
	
	private void switchToWindowed( GLFWVidMode vidMode ) {
		glfwSetWindowMonitor( id, NULL, 0, 0, desiredWidth, desiredHeight, vidMode.refreshRate() );
		glfwSetWindowPos( id, ( vidMode.width() - desiredWidth ) / 2, ( vidMode.height() - desiredHeight ) / 2 );
	}
	
	private void addScreenSizeListener() {
		glfwSetWindowSizeCallback( id, ( window, width, height ) -> {
			if ( validSizeChange( width, height, widthScreenCoords, heightScreenCoords ) ) {
				this.widthScreenCoords = width;
				this.heightScreenCoords = height;
				notifyListeners();
			}
		});
	}
	
	private void addPixelSizeListener() {
		GLFW.glfwSetFramebufferSizeCallback(id, (window, width, height) -> {
			if (validSizeChange(width, height, pxWidth, pxHeight)) {
				this.pxWidth = width;
				this.pxHeight = height;
				notifyListeners();
			}
		});
	}
	
	private void getInitialWindowSizes() {
		try ( MemoryStack stack = MemoryStack.stackPush() ) {
			IntBuffer widthBuff = stack.mallocInt( 1 );
			IntBuffer heightBuff = stack.mallocInt( 1 );
			getInitialScreenSize( widthBuff, heightBuff );
			getInitialPixelSize( widthBuff, heightBuff );
		}
	}
	
	private void getInitialScreenSize( IntBuffer widthBuff, IntBuffer heightBuff ) {
		glfwGetWindowSize( id, widthBuff, heightBuff );
		this.widthScreenCoords = widthBuff.get( 0 );
		this.heightScreenCoords = heightBuff.get( 0 );
		widthBuff.clear();
		heightBuff.clear();
	}
	
	private void getInitialPixelSize( IntBuffer widthBuff, IntBuffer heightBuff ) {
		glfwGetFramebufferSize( id, widthBuff, heightBuff );
		this.pxWidth = widthBuff.get( 0 );
		this.pxHeight = heightBuff.get( 0 );
	}
	
	private boolean validSizeChange( int newWidth, int newHeight, int oldWidth, int oldHeight ) {
		if ( newWidth == 0 || newHeight == 0 ) {
			return false;
		}
		return newWidth != oldWidth || newHeight != oldHeight;
	}
	
	private void notifyListeners() {
		for( WindowSizeListener listener : listeners ) {
			listener.sizeChanged( pxWidth, pxHeight );
		}
	}
	
}
