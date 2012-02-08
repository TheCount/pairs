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
 * Interface for representations of an image resource.
 */
public interface ImageResource {
	/**
	 * Resource type.
	 */
	public static enum Type {
		SVG
	}

	/**
	 * Returns the type of this image resource.
	 *
	 * @return The type of this image resource is returned.
	 */
	public Type getType();

	/**
	 * Returns the copyright text for this image resource.
	 *
	 * @return The copyright text for this image resource is returned.
	 */
	public String getCopyrightText();

	/**
	 * Returns the licence name for this image resource.
	 *
	 * @return The licence name for this image resource is returned.
	 */
	public String getLicenceName();

	/**
	 * Returns the licence text for this image resource.
	 *
	 * @return The licence text for this image resource is returned.
	 */
	public String getLicenceText();

	/**
	 * Creates a new image from this image resource.
	 *
	 * @param width Width of the new image.
	 * @param height Height of the new image.
	 * @param renderingHints Rendering hints.
	 *
	 * @return An image of the specified width and height will be returned.
	 * 	Vector image resources will be rasterised using the specified rendering hints.
	 * 	Other image resources will be scaled.
	 */
	public Image createImage( int width, int height, RenderingHints renderingHints );

	/**
	 * Creates a new image from this image resource using default rendering hints.
	 *
	 * @param width Width of the new image.
	 * @param height Height of the new image.
	 *
	 * @return An image of the specified width and height will be returned.
	 * 	The rendering hints used to create the image will depend on the image type.
	 */
	public Image createImage( int width, int height );
}
