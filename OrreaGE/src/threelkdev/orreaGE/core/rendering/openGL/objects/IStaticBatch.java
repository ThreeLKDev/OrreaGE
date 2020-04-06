package threelkdev.orreaGE.core.rendering.openGL.objects;

public interface IStaticBatch {
	
	public boolean isVisible();
	public float getAlpha();
	public Vao getVao();
	public int getVertexCount();
	public float getProgression();
	
}
