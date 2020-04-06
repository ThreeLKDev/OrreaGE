package threelkdev.orreaGE.core.ui.text.loading;

import java.util.List;

import threelkdev.orreaGE.core.ui.text.Text;
import threelkdev.orreaGE.core.ui.text.TextMesh;

public class TextGenerator {
	
	private final TextStructureGenerator structureGenerator;
	private final TextMeshGenerator meshGenerator;
	
	protected TextGenerator( TextStructureGenerator structureGenerator, TextMeshGenerator meshGenerator ) {
		this.structureGenerator = structureGenerator;
		this.meshGenerator = meshGenerator;
	}
	
	public synchronized TextMesh initializeText( Text text, float maxLineLengthPix ) {
		List< Line > textStructure = structureGenerator.createStructure( text.getString(), text.getFontSize(), maxLineLengthPix );
		TextMesh mesh = meshGenerator.generateTextMesh( text, textStructure );
		return mesh;
	}
	
}
