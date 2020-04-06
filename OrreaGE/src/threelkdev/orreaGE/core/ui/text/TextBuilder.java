package threelkdev.orreaGE.core.ui.text;

import threelkdev.orreaGE.tools.colours.Colour;
import threelkdev.orreaGE.tools.files.FileUtils;

public class TextBuilder {
	
	private final String text;
	
	private float textSize = 1;
	private Colour colour = new Colour( 1, 1, 1 );
	private boolean scalable = false;
	private Alignment alignment = Alignment.LEFT;
	//TODO font repos
	private Font font = Font.loadFont( FileUtils.getResource( "fonts/mons.png" ), FileUtils.getResource( "fonts/mons.fnt" ) );
	
	protected TextBuilder( String text ) {
		this.text = text;
	}
	
	public TextBuilder align( Alignment alignment ) {
		this.alignment = alignment;
		return this;
	}
	
	public TextBuilder colour( Colour colour ) {
		this.colour = colour;
		return this;
	}
	
	public TextBuilder setFont( Font font ) {
		this.font = font;
		return this;
	}
	
	public TextBuilder setScalable() {
		this.scalable = true;
		return this;
	}
	
	public TextBuilder setFontSize( float size ) {
		this.textSize = size;
		return this;
	}
	
	public TextBuilder setFontSizePixels( int pixels ) {
		this.textSize = ( float ) pixels / ( float ) Text.LINE_HEIGHT_PIXELS;
		return this;
	}
	
	public Text create() {
		return new Text( text, font, textSize, alignment, colour, scalable );
	}
	
}
