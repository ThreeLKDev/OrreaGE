package threelkdev.orreaGE.core.windows;

import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_REFRESH_RATE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIcon;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeLimits;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage.Buffer;
import org.lwjgl.glfw.GLFWVidMode;

public class WindowBuilder {
	
	private final int width;
	private final int height;
	private final String title;
	
	private boolean fullscreen = false;
	private boolean vsync = true;
	private Buffer icon = null;
	private int minWidth = 120;
	private int minHeight = 120;
	private int fps = 100;
	private int maxWidth = GLFW_DONT_CARE;
	private int maxHeight = GLFW_DONT_CARE;
	private int samples = 0;
	
	protected WindowBuilder( int width, int height, String title ) {
		this.width = width;
		this.height = height;
		this.title = title;
	}
	
	public Window create() {
		GLFWErrorCallback.createPrint( System.err ).set();
		glfwInit();
		GLFWVidMode vidMode = glfwGetVideoMode( glfwGetPrimaryMonitor() );
		setWindowHints( vidMode );
		long windowId = createWindow( vidMode );
		applyWindowSettings( windowId );
		return new Window( windowId, width, height, fps, fullscreen, vsync );
	}
	
	public WindowBuilder fullscreen( boolean fullscreen ) {
		this.fullscreen = fullscreen;
		return this;
	}
	
	public WindowBuilder setFps( int fps ) {
		this.fps = fps;
		return this;
	}
	
	public WindowBuilder setVsync( boolean vsync ) {
		this.vsync = vsync;
		return this;
	}
	
	public WindowBuilder setMSAA( boolean enable ) {
		this.samples = enable ? 4 : 0;
		return this;
	}
	
	public WindowBuilder withIcon( Buffer icon ) {
		this.icon = icon;
		return this;
	}
	
	public WindowBuilder setMinSize( int width, int height ) {
		this.minWidth = width;
		this.minHeight = height;
		return this;
	}
	
	public WindowBuilder setMaxSize( int width, int height ) {
		this.maxWidth = width;
		this.maxHeight = height;
		return this;
	}
	
	public WindowBuilder withoutSizeLimits() {
		this.minWidth = GLFW_DONT_CARE;
		this.maxWidth = GLFW_DONT_CARE;
		this.minHeight = GLFW_DONT_CARE;
		this.maxHeight = GLFW_DONT_CARE;
		return this;
	}
	
	private long createWindow( GLFWVidMode vidMode ) {
		if ( fullscreen ) {
			return glfwCreateWindow( vidMode.width(), vidMode.height(), title, glfwGetPrimaryMonitor(), NULL );
		} else {
			long windowId = glfwCreateWindow( width, height, title, NULL, NULL );
			glfwSetWindowPos( windowId, ( vidMode.width() - width ) / 2, ( vidMode.height() - height ) / 2 );
			return windowId;
		}
	}
	
	private void applyWindowSettings( long windowId ) {
		glfwMakeContextCurrent( windowId );
		glfwSwapInterval( vsync ? 1 : 0 );
		glfwSetWindowSizeLimits( windowId, minWidth, minHeight, maxWidth, maxHeight );
		glfwShowWindow( windowId );
		if ( icon != null ) {
			glfwSetWindowIcon( windowId, icon );
			icon.close();
		}
	}
	
	private void setWindowHints( GLFWVidMode vidMode ) {
		glfwWindowHint( GLFW_VISIBLE, GLFW_FALSE );
		glfwWindowHint( GLFW_RESIZABLE, GLFW_TRUE );
		glfwWindowHint( GLFW_SAMPLES, samples );
		glfwWindowHint( GLFW_REFRESH_RATE, vidMode.refreshRate() );
	}
	
}
