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

import java.io.IOException;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.*;

import static pairs.util.Message._;

/**
 * Class representing a licence.
 */
public class Licence {
	/**
	 * Licences database.
	 */
	private static final JsonNode database;

	/**
	 * Database resource name.
	 */
	private static final String DATABASE_NAME = "licences.json";

	/**
	 * Initialises the database.
	 */
	static {
		try {
			ObjectMapper om = new ObjectMapper();
			om.configure( JsonParser.Feature.ALLOW_COMMENTS, true );

			String jsonText = Resources.loadResourceAsString( DATABASE_NAME ); // workaround for bug #779, see http://jira.codehaus.org/browse/JACKSON-779
			database = om.readTree( jsonText );
		} catch ( Exception e ) {
			throw new ExceptionInInitializerError( e );
		}
	}

	/**
	 * Licence name.
	 */
	private final String licenceName;

	/**
	 * Licence text resource name.
	 */
	private final String licenceTextName;

	/**
	 * Licence text.
	 */
	private String licenceText;

	/**
	 * Creates a new licence.
	 *
	 * @param licenceName Licence name.
	 * @param licenceTextName Name of the licence text resource.
	 *
	 * @throws NullPointerException if one of the arguments is null.
	 */
	protected Licence( String licenceName, String licenceTextName ) {
		if ( ( licenceName == null ) || ( licenceTextName == null ) ) {
			throw new NullPointerException();
		}
		this.licenceName = licenceName;
		this.licenceTextName = licenceTextName;
		licenceText = null; // loaded on demand
	}

	/**
	 * Returns the licence name.
	 *
	 * @return The name of the licence is returned.
	 */
	public String getLicenceName() {
		return licenceName;
	}

	/**
	 * Returns the licence text.
	 *
	 * @return The licence text is returned.
	 *
	 * @throws IOException if the licence text cannot be loaded.
	 */
	public String getLicenceText() throws IOException {
		if ( licenceText == null ) {
			licenceText = Resources.loadResourceAsString( licenceTextName ).intern();
		}
		return licenceText;
	}

	/**
	 * Returns the licence with the specified licence key.
	 *
	 * @param key Licence key.
	 *
	 * @return The licence with the specified key is returned.
	 *
	 * @throws IllegalArgumentException if there is no licence with the specified key.
	 */
	public static Licence get( String key ) {
		JsonNode licence = database.get( key );
		if ( licence == null ) {
			throw new IllegalArgumentException( _( "error-licence", key ) );
		}
		return new Licence( licence.get( "name" ).getTextValue(), licence.get( "resource" ).getTextValue() );
	}
}
