package threelkdev.orreaGE.testing.styles;

import threelkdev.orreaGE.core.ui.UiComponent;
import threelkdev.orreaGE.core.ui.UiComponent.UiRoot;
import threelkdev.orreaGE.testing.UiMaster;
import threelkdev.orreaGE.tools.utils.Array;

public class UiStyleSelector {
	
	public static final String COMBINATORS = ">~+";
	
	static enum SelectorMode {
		None, DirectChild, GeneralSibling, AdjacentSibling
	}
	
	final String[] selector;
	final String[] rules;
	
	public UiStyleSelector( String[] selector, String[] rules ) {
		Array< String > sel = new Array< String >( String.class );
		for( int i = 0; i < selector.length; i++ ) {
			if( selector[ i ].length() > 0 ) {
				sel.add( selector[ i ].toLowerCase() );
			}
		}
		this.selector = sel.toArray();
		this.rules = rules;
	}
	
	public void apply( UiRoot root ) {
		SelectorMode mode = SelectorMode.None;
		Array< UiComponent > currentMatches = new Array< UiComponent >( UiComponent.class );
		Array< UiComponent > nextMatches = new Array< UiComponent >( UiComponent.class );
		currentMatches.add( root );
		for( int i = 0; i < selector.length; i++ ) {
			mode = SelectorMode.None;
			if( selector[ i ].length() == 1 && COMBINATORS.contains( selector[ i ] ) ) {
				switch( selector[ i ] ) {
					case ">":
						mode = SelectorMode.DirectChild;
						i++;
						break;
					case "~":
						mode = SelectorMode.GeneralSibling;
						i++;
						break;
					case "+":
						mode = SelectorMode.AdjacentSibling;
						i++;
						break;
					default:
						break;
				}
			}
			if( selector[ i ].contains(":") ) {
				//Selector with psuedo-selector
				String[] split = selector[ i ].split(":");
				boolean ran = false;
				switch( split[ 1 ] ) {
				case "first-child":
					
					ran = true;
					break;
				case "first-of-type":
					
					ran = true;
					break;
				case "last-child":
					
					ran = true;
					break;
				case "last-of-type":
					
					ran = true;
					break;
				default: break;
				}
			} else {
				//Normal selector
				Class< ? extends UiComponent > clazz = UiMaster.getClassFromAlias( selector[ i ] );
				if( clazz == null )
					break; //!TODO! Throw error
				for( UiComponent component : currentMatches ) {
					component.applyToUiChildren( child -> {
						if( child.getClass() == clazz ) {
							if( !nextMatches.contains( child, true ) )
								nextMatches.add( child );
						}
					}, mode != SelectorMode.DirectChild);
				}
			}
			currentMatches.clear();
			currentMatches.addAll( nextMatches );
			nextMatches.clear();
		}
	}
	
}

/*

"Combinators" a la CSS:
	> direct child
	~ general sibling
	+ adjacent sibling (sibling directly after)
	
Rule proxies
	"follow #" relative pixel distance from preceding element
	"lead #" relative pixel distance from next element

e.g. selector: root > drag_window
rules:
	background: red
	left: 0px
	top: 50%
	
root > drag_window {
	background: red;
	left: 0px;
	top: 50%;
}

drag_window > block + block {
	top: follow 10px;
}


========================================== More realistically:
Constraint *type* remains unchanged, but the amount can be tweaked. E.g. an absolute pos can be altered, but relative pos
	can only have the distance/gap changed.

root > drag_window {
	background: ff0000;
	left: 0;
	top: 50;
}

Move all of this into a UiBuilder;
As you build each ui element, keep track of the current "path" to that element -- the list of ancestors -- and the 
	element's predecessor siblings.
 */ 
