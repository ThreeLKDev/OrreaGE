package threelkdev.orreaGE.testing;

import threelkdev.orreaGE.core.ui.UiComponent;

public abstract class UiElement extends UiComponent {
	
	UiElementStyle style;
	
	public UiElement( UiElementStyle style ) {
		this.style = style;
	}
	
	public static abstract class UiElementStyle {
		
	}
}
