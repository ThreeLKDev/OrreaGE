package threelkdev.orreaGE.core.engine;

public class EngineConfigs {
	
	public int languageId = 0;
	
	public int windowWidth = 1280;
	public int windowHeight = 720;
	
	public int windowMinWidth = 600;
	public int windowMinHeight = 350;
	public float UiSize = 1f;
	
	public boolean fullscreen = false;
	public String windowTitle = "Orrea";
	public int fps = 100;
	public boolean vsync = true;
	public boolean isMSAA = true;
	
	public static EngineConfigs getDefaultConfigs() {
		return new EngineConfigs();
	}
	
	private EngineConfigs() {}
	
}
