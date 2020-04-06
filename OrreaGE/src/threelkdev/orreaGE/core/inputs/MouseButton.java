package threelkdev.orreaGE.core.inputs;

import org.lwjgl.glfw.GLFW;

public enum MouseButton {
	
	LEFT(GLFW.GLFW_MOUSE_BUTTON_LEFT),
	MIDDLE(GLFW.GLFW_MOUSE_BUTTON_MIDDLE),
	RIGHT(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
	
	private final int glfwId;
	
	private MouseButton( int id ) {
		this.glfwId = id;
	}
	
	protected int getId() {
		return glfwId;
	}
}
