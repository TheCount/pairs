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

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static pairs.util.Message.__;

/**
 * Class for loading image resources.
 */
public class ImageResourceLoader {
	/**
	 * Image database resource name.
	 */
	private static final String DATABASE_NAME = "images.json";

	/**
	 * Image database.
	 */
	private static final JsonNode database;

	/**
	 * Initialises the image database.
	 */
	static {
		try {
			ObjectMapper om = new ObjectMapper();
			om.configure( JsonParser.Feature.ALLOW_COMMENTS, true );

			String jsonText = Resources.loadResourceAsString( DATABASE_NAME ); // workaround for bug #779, see http://jira.codehaus.org/browse/JACKSON-779
			database = om.readTree( jsonText );
		} catch ( IOException e ) {
			throw new ExceptionInInitializerError( e );
		}
	}

	/**
	 * Loads an image resource.
	 *
	 * @param imageName Image name.
	 *
	 * @return An image resource is returned.
	 *
	 * @throws IllegalArgumentException if there is no image with the specified imageName.
	 */
	public static ImageResource load( String imageName ) {
		ImageResource result;
		try {
			JsonNode imageNode = database.get( imageName );
			String typeName = imageNode.get( "type" ).textValue();
			ImageResource.Type type = Enum.valueOf( ImageResource.Type.class, typeName );

			String copyrightName = imageNode.get( "copyright" ).textValue();
			Copyright copyright = Copyright.get( copyrightName );

			switch ( type ) {
				case SVG:
					result = new SVGResource( imageNode.get( "resource" ).textValue(), copyright );
					break;
				default:
					throw new AssertionError( "This should not happen" );
			}
		} catch ( Exception e ) {
			throw new IllegalArgumentException( __( "error-loadingimage", imageName ), e );
		}
		return result;
	}
}
