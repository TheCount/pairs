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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.MissingResourceException;

import static pairs.util.Message.__;

/**
 * Resources utilities.
 */
public class Resources implements ResourceConstants {
	/**
	 * Loads a resource into a string.
	 *
	 * @param resourceName Name of the resource to be loaded.
	 *
	 * @return A string containing the resource data is returned.
	 *
	 * @throws IOException if an I/O error occurs.
	 * @throws MissingResourceException if the resource resourceName could not be found.
	 * @throws NullPointerException if resourceName is null.
	 */
	public static String loadResourceAsString( String resourceName ) throws IOException {
		if ( resourceName == null ) {
			throw new NullPointerException();
		}
		InputStream in = ClassLoader.getSystemResourceAsStream( resourceName );
		if ( in == null ) {
			throw new MissingResourceException( __( "error-loadingresource", resourceName ), ClassLoader.class.getName(), resourceName );
		}
		InputStreamReader inr = new InputStreamReader( in, ENCODING );
		char buf[] = new char[4096];
		StringBuilder sb = new StringBuilder();
		int read;
		while ( ( read = inr.read( buf, 0, buf.length ) ) != -1 ) {
			sb.append( buf, 0, read );
		}
		inr.close();
		return sb.toString();
	}
}
