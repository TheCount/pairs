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

import java.util.Iterator;
import java.util.Vector;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;

import static pairs.util.Message.__;

/**
 * Copyright representation.
 */
public class Copyright {
	/**
	 * Copyrights database name.
	 */
	private static final String DATABASE_NAME = "copyrights.json";

	/**
	 * Copyrights database.
	 */
	private static final JsonNode database;

	/**
	 * Initialises the database.
	 */
	static {
		try {
			ObjectMapper om = new ObjectMapper();
			om.configure( JsonParser.Feature.ALLOW_COMMENTS, true );

			String jsonText = Resources.loadResourceAsString( DATABASE_NAME ); // workaround for jackson bug #779, see http://jira.codehaus.org/browse/JACKSON-779
			database = om.readTree( jsonText );
		} catch ( Exception e ) {
			throw new ExceptionInInitializerError( e );
		}
	}

	/**
	 * Copyright text.
	 */
	private final String copyrightText;

	/**
	 * Licence.
	 */
	private final Licence licence;

	/**
	 * Creates a new copyright.
	 *
	 * @param copyrightText Copyright text.
	 * @param licence The licence.
	 *
	 * @throws NullPointerException if one of the arguments is null.
	 */
	protected Copyright( String copyrightText, Licence licence ) {
		if ( ( copyrightText == null ) || ( licence == null ) ) {
			throw new NullPointerException();
		}
		this.copyrightText = copyrightText;
		this.licence = licence;
	}

	/**
	 * Returns the copyright text.
	 *
	 * @return The copyright text is returned.
	 */
	public String getText() {
		return copyrightText;
	}

	/**
	 * Returns the licence.
	 *
	 * @return The licence is returned.
	 */
	public Licence getLicence() {
		return licence;
	}

	/**
	 * Obtains a copyright.
	 *
	 * @param key Copyright key.
	 *
	 * @return The copyright with the specified key is returned.
	 *
	 * @throws IllegalArgumentException if no copyright with the specified key exists.
	 */
	public static Copyright get( String key ) {
		JsonNode copyrightNode = database.get( key );
		if ( copyrightNode == null ) {
			throw new IllegalArgumentException( __( "error-copyright", key ) );
		}
		String copyrightText = copyrightNode.get( "copyright" ).textValue();
		return new Copyright( copyrightText, Licence.get( copyrightNode.get( "licence" ).textValue() ) );
	}

	/**
	 * Get all copyrights.
	 *
	 * @return All copyrights are returned.
	 */
	public static Vector<Copyright> getAll() {
		Vector<Copyright> result = new Vector();
		Iterator<String> copyrightNames = database.fieldNames();
		while ( copyrightNames.hasNext() ) {
			result.add( get( copyrightNames.next() ) );
		}
		return result;
	}
}
