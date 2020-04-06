package threelkdev.orreaGE.core.inputs;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.glfw.GLFW;

public class Keyboard {

	private Set< Integer > keysPressedThisFrame = new HashSet< Integer >();
	private Set< Integer > keysRepeatedThisFrame = new HashSet< Integer >();
	private Set< Integer > keysReleasedThisFrame = new HashSet< Integer >();
	private Set< Integer > keysDown = new HashSet< Integer >();
	private String charsThisFrame = "";
	
	public Keyboard( long windowId ) {
		addKeyListener( windowId );
		addTextListener( windowId );
	}
	
	public void update() {
		keysPressedThisFrame.clear();
		keysRepeatedThisFrame.clear();
		keysReleasedThisFrame.clear();
		charsThisFrame = "";
	}
	
	public boolean isKeyDown( int key ) {
		return keysDown.contains( key );
	}
	
	public String getChars() {
		return charsThisFrame;
	}
	
	public boolean keyPressEvent( int key ) {
		return keysPressedThisFrame.contains( key );
	}
	
	public boolean keyPressEvent( int key, boolean checkRepeats ) {
		return keysPressedThisFrame.contains( key ) || ( checkRepeats && keysRepeatedThisFrame.contains( key ) );
	}
	
	public boolean keyReleaseEvent( int key ) {
		return keysReleasedThisFrame.contains( key );
	}
	
	private void reportKeyPress( int key ) {
		keysDown.add( key );
		keysPressedThisFrame.add( key );
	}
	
	private void reportKeyRelease( int key ) {
		keysDown.remove( ( Integer ) key );
		keysReleasedThisFrame.add( key );
	}
	
	private void addTextListener( long windowId ) {
		GLFW.glfwSetCharCallback( windowId, ( window, unicode ) -> {
			charsThisFrame += ( char ) unicode;
		});
	}
	
	private void addKeyListener( long windowId ) {
		GLFW.glfwSetKeyCallback( windowId,  ( window, key, scancode, action, mods ) -> {
			if ( action == GLFW.GLFW_PRESS ) {
				reportKeyPress( key );
			} else if ( action == GLFW.GLFW_RELEASE ) {
				reportKeyRelease( key );
			} else if ( action == GLFW.GLFW_REPEAT ) {
				keysRepeatedThisFrame.add( key );
			}
		});
	}
	
}
