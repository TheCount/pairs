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

package pairs.util;

import java.awt.*;

/**
 * Abstract implementation of the {@link ImageResource} interface.
 * This implementation provides the getter methods.
 */
abstract class AbstractImageResource implements ImageResource {
	/**
	 * Type.
	 */
	private final Type type;

	/**
	 * Copyright.
	 */
	private final Copyright copyright;

	/**
	 * Creates a new abstract image resource.
	 *
	 * @param type Image resource type.
	 * @param copyright Image resource copyright.
	 *
	 * @throws NullPointerException if one of the arguments is null.
	 */
	protected AbstractImageResource( Type type, Copyright copyright ) {
		if ( ( type == null ) || ( copyright == null ) ) {
			throw new NullPointerException();
		}
		this.type = type;
		this.copyright = copyright;
	}

	public Type getType() {
		return type;
	}

	public Copyright getCopyright() {
		return copyright;
	}

	public abstract Image createImage( int width, int height, RenderingHints renderingHints );

	public abstract Image createImage( int width, int height );
}
