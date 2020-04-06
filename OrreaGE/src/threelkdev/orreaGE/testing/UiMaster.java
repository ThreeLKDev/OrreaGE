package threelkdev.orreaGE.testing;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import threelkdev.orreaGE.core.ui.UiComponent;

public class UiMaster {

	static Map< String,  Class< ? extends UiComponent > > classmap = new HashMap< String, Class< ? extends UiComponent > >();
	
	public static void registerUiComponentClass( Class< ? extends UiComponent > clazz, String alias ) {
		alias = alias.toLowerCase();
		if( !classmap.containsKey( alias ) ) {
			classmap.put( alias, clazz );
		}
	}
	
	public static String getAliasFromClass( Class< ? extends UiComponent > clazz ) {
		for( Entry< String, Class< ? extends UiComponent > > entry : classmap.entrySet() ) {
			if( entry.getValue() == clazz ) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	public static Class< ? extends UiComponent > getClassFromAlias( String alias ) {
		return classmap.get( alias );
	}
	
}

/*
UiElement = Utilized UiComponents. Components are the pieces, Elements are the whole; the Inventory menu would be an Element
	while the scroll box, item slots, names, etc. would be the components.
Style should work similar to CSS, e.g.
>	inventory > scrollbox { border-color: white; border-width: 1px; }
	^would affect only the top-level scrollbox within the inventory UiElement, giving it a 1px white border
>	dialogueyesno block { background-color: red; }
	^would affect every UiBlock contained within the yes/no dialogue UiElement, making their background color red.
	
Would have to parse all UiStyles on load, translate them, apply them to all registered UiElements, and save the resulting
	elements as (elementName).ui or something
	Treat this as a cache; timestamp them on creation and only re-create them when prompted to *or* after x days (config).
*/