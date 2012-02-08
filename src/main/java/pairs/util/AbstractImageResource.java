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
	 * Copyright text.
	 */
	private final String copyrightText;

	/**
	 * Licence name.
	 */
	private final String licenceName;

	/**
	 * Licence text.
	 */
	private final String licenceText;

	/**
	 * Creates a new abstract image resource.
	 *
	 * @param typeName Image resource type name.
	 * @param copyrightText Copyright text.
	 * @param licenceName Name of the licence this image resource is under.
	 * @param licenceText Licence text.
	 *
	 * @throws NullPointerException if one of the arguments is null.
	 */
	protected AbstractImageResource( Type type, String copyrightText, String licenceName, String licenceText ) {
		if ( type == null ) {
			throw new NullPointerException();
		}
		this.type = type;
		this.copyrightText = copyrightText.intern();
		this.licenceName = licenceName.intern();
		this.licenceText = licenceText.intern();
	}

	public Type getType() {
		return type;
	}

	public String getCopyrightText() {
		return copyrightText;
	}

	public String getLicenceName() {
		return licenceName;
	}

	public String getLicenceText() {
		return licenceText;
	}

	public abstract Image createImage( int width, int height, RenderingHints renderingHints );

	public abstract Image createImage( int width, int height );
}
