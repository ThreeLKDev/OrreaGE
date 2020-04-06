package threelkdev.orreaGE.core.inputs;

import static org.lwjgl.glfw.GLFW.*;

import java.util.HashSet;
import java.util.Set;

import threelkdev.orreaGE.core.ui.UiMaster;
import threelkdev.orreaGE.core.windows.Window;

public class Mouse {
	
	private final Window window;

	private Set<Integer> buttonsDown = new HashSet<Integer>();
	private Set<Integer> buttonsClickedThisFrame = new HashSet<Integer>();
	private Set<Integer> buttonsReleasedThisFrame = new HashSet<Integer>();
	
	private float x, y;
	private float dx, dy;
	private float scroll;
	private float lastX, lastY;
	
	public Mouse(Window window){
		this.window = window;
		addMoveListener(window.getId());
		addClickListener(window.getId());
		addScrollListener(window.getId());
	}
	
	public boolean isButtonDown(MouseButton button){
		return buttonsDown.contains(button.getId());
	}
	
	public boolean isClickEvent(MouseButton button){
		return buttonsClickedThisFrame.contains(button.getId());
	}
	
	public boolean isClickEvent() {
		return !buttonsClickedThisFrame.isEmpty();
	}
	
	public boolean isReleaseEvent(MouseButton button){
		return buttonsReleasedThisFrame.contains(button.getId());
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public float getDx(){
		return dx;
	}
	
	public float getDy(){
		return dy;
	}
	
	public float getScroll(){
		return scroll;
	}
	
	public void update(){
		buttonsClickedThisFrame.clear();
		buttonsReleasedThisFrame.clear();
		updateDeltas();
		this.scroll = 0;
	}
	
	private void reportButtonClick(int button){
		buttonsClickedThisFrame.add(button);
		buttonsDown.add(button);
	}
	
	private void reportButtonRelease(int button){
		buttonsReleasedThisFrame.add(button);
		buttonsDown.remove((Integer)button);
	}
	
	private void updateDeltas(){
		this.dx = x - lastX;
		this.dy = y - lastY;
		this.lastX = x;
		this.lastY = y;
	}

	private void addMoveListener(long windowId){
		glfwSetCursorPosCallback (windowId, (currentWindow, xPos, yPos) -> {
			this.x = (float) (xPos / window.getScreenCoordWidth());
			this.y = (float) (yPos / window.getScreenCoordHeight());
		});
	}

	private void addClickListener(long windowId){
		glfwSetMouseButtonCallback(windowId, (window, button, action, mods) -> {
			if(action == GLFW_PRESS){
				reportButtonClick(button);
			}else if(action == GLFW_RELEASE){
				reportButtonRelease(button);
			}
		});
	}
	
	private void addScrollListener(long windowId){
		glfwSetScrollCallback(windowId, (window, scrollX, scrollY) -> {
			this.scroll = (float) scrollY;
		});
	}
	
	/** Event passed through to handler functions; note that relativeDragX and relativeDragY <br />
	 * will be null in any function other than onMouseDrag() */
	public static class MouseEvent {
		
		public final MouseButton button;
		
		public MouseEvent( MouseButton button ) {
			this.button = button;
		}
	}
	
	public static class MouseClickEvent extends MouseEvent {
		
		public final float x, y;
		public MouseClickEvent( MouseButton button, float x, float y ) {
			super( button );
			this.x = x;
			this.y = y;
		}

		public int getXAsInt() { return ( int ) ( this.x * UiMaster.getDisplayWidth() ); }
		public int getYAsInt() { return ( int ) ( this.y * UiMaster.getDisplayHeight() ); }
		
	}
	
	public static class MouseDragEvent extends MouseClickEvent {
		
		public final float startX, startY, diffX, diffY, relativeDragX, relativeDragY;
		public MouseDragEvent( MouseButton button, float x, float y, float startX, float startY, float diffX, float diffY, float relativeDragX, float relativeDragY ) {
			super( button, x, y );
			this.startX = startX;
			this.startY = startY;
			this.diffX = diffX;
			this.diffY = diffY;
			this.relativeDragX = relativeDragX;
			this.relativeDragY = relativeDragY;
		}

		public int getXAsInt() { return ( int ) ( this.x * UiMaster.getDisplayWidth() ); }
		public int getYAsInt() { return ( int ) ( this.y * UiMaster.getDisplayHeight() ); }
		public int getStartXAsInt() { return ( int ) ( this.startX * UiMaster.getDisplayWidth() ); }
		public int getStartYAsInt() { return ( int ) ( this.startY * UiMaster.getDisplayHeight() ); }
		public int getDiffXAsInt() { return ( int ) ( this.diffX * UiMaster.getDisplayWidth() ); }
		public int getDiffYAsInt() { return ( int ) ( this.diffY * UiMaster.getDisplayHeight() ); }
		public int getRelativeDragXAsInt() { return ( int ) ( this.relativeDragX * UiMaster.getDisplayWidth() ); }
		public int getRelativeDragYAsInt() { return ( int ) ( this.relativeDragY * UiMaster.getDisplayHeight() ); }
	}
	
	public MouseEvent getEvent( MouseButton button ) {
		return new MouseEvent( button );
	}
	
	public MouseClickEvent getClickEvent( MouseButton button ) {
		return new MouseClickEvent( button, this.x, this.y );
	}
	
	public MouseDragEvent getDragEvent( MouseButton button, float startDragX, float startDragY ) {
		return new MouseDragEvent( button, this.x, this.y, startDragX, startDragY, this.dx, this.dy, this.x - startDragX, this.y - startDragY );
	}
	
	
	
}
