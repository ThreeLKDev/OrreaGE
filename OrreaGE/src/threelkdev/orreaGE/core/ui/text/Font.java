package threelkdev.orreaGE.core.ui.text;

import java.io.File;

import threelkdev.orreaGE.core.rendering.openGL.textures.Texture;
import threelkdev.orreaGE.core.ui.text.loading.MetaDataLoader;
import threelkdev.orreaGE.core.ui.text.loading.TextGenerator;

public class Font {
	
	private final Texture fontAtlas;
	private final TextGenerator generator;
	
	public Font( Texture fontAtlas, TextGenerator generator ) {
		this.fontAtlas = fontAtlas;
		this.generator = generator;
	}
	
	public Texture getFontAtlas() { return fontAtlas; }
	
	public TextMesh initText( Text text, float maxLineLengthPix ) {
		return generator.initializeText( text, maxLineLengthPix );
	}
	
	public static Font loadFont( File textureFile, File metaFile ) {
		TextGenerator generator = new MetaDataLoader( metaFile ).loadMetaData();
		Texture texture = Texture.newTexture( textureFile ).create();
		return new Font( texture, generator );
	}
	
}
