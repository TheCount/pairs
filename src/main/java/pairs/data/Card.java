/*
    Pairs, a concentration game with modular card packages.
    Copyright © 2012  Alexander Klauer

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package pairs.data;

import pairs.util.ImageResource;

/**
 * A card.
 */
public class Card {
	/**
	 * Possible card types.
	 */
	public static enum Type {
		IMAGE,
		TEXT
	}

	/**
	 * Card type.
	 */
	private final Type type;

	/**
	 * Card content.
	 */
	private final Object content;

	/**
	 * Creates a new image card.
	 *
	 * @param imageResource The image resource for the card.
	 *
	 * @throws NullPointerException if imageResource is null.
	 */
	Card( ImageResource imageResource ) {
		if ( imageResource == null ) {
			throw new NullPointerException();
		}
		this.type = Type.IMAGE;
		this.content = imageResource;
	}

	/**
	 * Creates a new text card.
	 *
	 * @param text Card text.
	 *
	 * @throws NullPointerException if text is null.
	 */
	Card( String text ) {
		if ( text == null ) {
			throw new NullPointerException();
		}
		this.type = Type.TEXT;
		this.content = text;
	}

	/**
	 * Returns the type of this card.
	 *
	 * @return The type of this card is returned.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Returns the image resource for this card.
	 *
	 * @return The image resource for this card is returned.
	 *
	 * @throws ClassCastException if this is a text card.
	 */
	public ImageResource getImageResource() {
		return (ImageResource) content;
	}

	/**
	 * Returns the text for this card.
	 *
	 * @return The text for this card is returned.
	 *
	 * @throws ClassCastException if this is an image card.
	 */
	public String getText() {
		return (String) content;
	}
}
