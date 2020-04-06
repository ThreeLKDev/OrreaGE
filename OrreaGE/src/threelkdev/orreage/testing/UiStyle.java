package threelkdev.orreage.testing;

import threelkdev.threelkdev.tools.colours.Colour;

public class UiStyle {
	
	public static final Colour DEFAULT_BACKGROUND			= new Colour( 0xff222222 );
	public static final Colour DEFAULT_FOREGROUND			= new Colour( 0xffaaaaaa );
	public static final Colour DEFAULT_ACCENT				= new Colour( 0xff2288ff );
	public static final Colour DEFAULT_ACCENT_ALT			= new Colour( 0xff44aaff );
	public static final Colour DEFAULT_ACCENT_ALT2			= new Colour( 0xff1666bb );
	public static final Colour DEFAULT_SLIDER_BACKGROUND	= new Colour( 0xff222222 );
	public static final Colour DEFAULT_SLIDER_FOREGROUND	= new Colour( 0xffaaaaaa );
	public static final Colour DEFAULT_SLIDER_HOVER			= new Colour( 0xffbbbbbb );
	public static final Colour DEFAULT_SLIDER_ACTIVE		= new Colour( 0xff888888 );
	public static final Colour DEFAULT_SLIDER_SPACER		= new Colour( 0xff222222 );

	
	private Colour colBackground;
	private Colour colForeground;
	private Colour colAccent;
	private Colour colAccentAlt;
	private Colour colAccentAlt2;
	private Colour colSliderBackground;
	private Colour colSliderForeground;
	private Colour colSliderHover;
	private Colour colSliderActive;
	private Colour colSliderSpacer;
	
	public Colour getBackgroundColour() {
		return colBackground == null ? DEFAULT_BACKGROUND : colBackground;
	}
	
	public Colour getForegroundColour() {
		return colForeground == null ? DEFAULT_FOREGROUND : colForeground;
	}
	
	public Colour getAccentColour() {
		return colAccent == null ? DEFAULT_ACCENT : colAccent;
	}
	
	public Colour getAccentAltColour() {
		return colAccentAlt == null ? DEFAULT_ACCENT_ALT : colAccentAlt;
	}
	
	public Colour getAccentAlt2Colour() {
		return colAccentAlt2 == null ? DEFAULT_ACCENT_ALT2 : colAccentAlt2;
	}
	
	public Colour getSliderBackgroundColour() {
		return colSliderBackground == null ? DEFAULT_SLIDER_BACKGROUND : colSliderBackground;
	}
	
	public Colour getSliderForegroundColour() {
		return colSliderForeground == null ? DEFAULT_SLIDER_FOREGROUND : colSliderForeground;
	}
	
	public Colour getSliderHoverColour() {
		return colSliderHover == null ? DEFAULT_SLIDER_HOVER : colSliderHover;
	}

	public Colour getSliderActiveColour() {
		return colSliderActive == null ? DEFAULT_SLIDER_ACTIVE : colSliderActive;
	}
	
	public Colour getSliderSpacerColour() {
		return colSliderSpacer == null ? DEFAULT_SLIDER_SPACER : colSliderSpacer;
	}	

	public UiStyle setColBackground(Colour colBackground) {
		this.colBackground = colBackground;
		return this;
	}

	public UiStyle setColForeground(Colour colForeground) {
		this.colForeground = colForeground;
		return this;
	}

	public UiStyle setColAccent(Colour colAccent) {
		this.colAccent = colAccent;
		return this;
	}

	public UiStyle setColAccentAlt(Colour colAccentAlt) {
		this.colAccentAlt = colAccentAlt;
		return this;
	}

	public UiStyle setColAccentAlt2(Colour colAccentAlt2) {
		this.colAccentAlt2 = colAccentAlt2;
		return this;
	}

	public UiStyle setColSliderBackground(Colour colSliderBackground) {
		this.colSliderBackground = colSliderBackground;
		return this;
	}

	public UiStyle setColSliderForeground(Colour colSliderForeground) {
		this.colSliderForeground = colSliderForeground;
		return this;
	}

	public UiStyle setColSliderHover( Colour colSliderHover ) {
		this.colSliderHover = colSliderHover;
		return this;
	}
	
	public UiStyle setColSliderActive( Colour colSliderActive ) {
		this.colSliderActive = colSliderActive;
		return this;
	}

	public UiStyle setColSliderSpacer(Colour colSliderSpacer) {
		this.colSliderSpacer = colSliderSpacer;
		return this;
	}
	
	
}
