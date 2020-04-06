package threelkdev.orreaGE.core.rendering;

import threelkdev.orreaGE.core.rendering.openGL.objects.Vao;

public interface IModelBatch {
	
	public boolean isVisible();
	public float getAlpha();
	public int getVertexCount();
	public int getIndexCount();
	public Vao getVao();
	
}
