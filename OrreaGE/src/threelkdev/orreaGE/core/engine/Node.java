/**************************************************************************
 * Copyright 2019 Shaun McQuillen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************************************************************/

package threelkdev.orreaGE.core.engine;

import threelkdev.orreaGE.tools.utils.Array;
import threelkdev.orreaGE.tools.utils.Disposable;

public abstract class Node implements Disposable {
	
	protected Array< Node > children;
	protected Node parent;
	
	/** Adds the given Node to this Node's children array ( creating the array if there isn't one ),<br />
	 * sets the given Node's parent to this Node, then calls this.onAttach( node ) and node.onAttach() */
	public void attach( Node node ) {
		if ( children != null ) {
			this.children.add( node );
			node.parent = this;
			this.onAttach( node );
			node.onAttach();
		} else {
			this.children = new Array< Node >( false, 1, Node.class );
			this.attach( node );
		}
	}
	
	public Array.ArrayIterator< Node > getArrayIterator() {
		return new Array.ArrayIterator< Node >( this.children );
	}
	
	/** Called after this node has been attached to another. */
	protected void onAttach() {}
	/** Called after attaching a node to this. */
	protected void onAttach( Node node ) {}
	
	/** Called after this node has been detached from another. */
	protected void onDetach() {}
	/** Called after detaching another node from this. */
	protected void onDetach( Node node ) {}
	
	public void attach( Node[] nodes ) {
		if ( children != null ) {
			children.ensureCapacity( nodes.length );
			for( Node node : nodes ) {
				this.children.add( node );
				node.parent = this;
				this.onAttach( node );
				node.onAttach();
			}
		} else { 
			this.children = new Array< Node >( false, nodes.length, Node.class );
			this.attach( nodes );
		}
	}
	
	public Node getParent() { return this.parent; }
	
	public void setParent( Node node ) {
		this.parent.detach( this );
		if ( node != null ) {
			node.attach(  this );
		} else {
			this.parent = null;
		}
	}
	
	public void detach( Node node ) {
		int index = this.children.indexOf( node, true );
		if ( index >= 0 ) {
			this.children.removeIndex( index );
			node.parent = null;
			this.onDetach( node );
			node.onDetach();
		}
	}
	
	public void detach( Node[] nodes ) {
		for( Node node : nodes ) {
			this.detach( node );
		}
	}
	
	public boolean isRoot() { return this.parent == null; }
	
	public Node getRoot() {
		return this.isRoot() ? this : this.parent.getRoot();
	}
	
	public Array< Node > getChildren() { return this.children; }
	
	/** Checks through this Node's direct children to see if the given Node exists amongst them. 
	 * @return True once/if found, false if none of the children match. */
	public boolean hasChild( Node other ) {
		if( children == null ) return false;
		for( Node node : children ) {
			if( node.equals( other ) )
				return true;
		}
		return false;
	}
	
	/** Checks through every child and sub-child of this Node to see if the given Node exists amongst them.
	 * @return True once/if found, false if none of the descendants match. */
	public boolean isAncestorOf( Node other ) {
		for( Node node : children ) {
			if( node.equals( other ) ) {
				return true;
			} else {
				if( node.isAncestorOf( other ) )
					return true;
			}
		}
		return false;
	}
	
	/** Recursively checks through every parent to see if the given Node is amongst them.
	 * @return True once/if found, false if none of the ancestors match. */
	public boolean isDescendantOf( Node other ) {
		if( parent != null  && other != null) {
			if( parent.equals( other ) )
				return true;
			else {
				if( parent.isDescendantOf( other ) ) {
					return true;
				}
			}
		}
		return false;
	}
	
	/** Checks if this object's parent is equal to another's.<br />
	 * Note that this will return true if both parents are null.
	 * @return True if they are the same, false otherwise. */
	public boolean isSiblingOf( Node other ) {
		return parent == other.parent;
	}
	
	/**
	 * Iterates through all children; calls this.detach( child ) then child.dispose()
	 */
	@Override
	public void dispose() {
		for( Node node : this.children ) {
			this.detach( node );
			node.dispose();
		}
	}
	
	
	
}
