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

import org.apache.log4j.Logger;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;

import org.codehaus.jackson.map.ObjectMapper;

import static pairs.util.Message._;

/**
 * Class for loading image resources.
 */
public class ImageResourceLoader {
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = Logger.getLogger( ImageResourceLoader.class );

	/**
	 * Image database resource name.
	 */
	private static final String DATABASE_NAME = "images.json";

	/**
	 * Image database.
	 */
	private static final JsonNode imageDatabase;

	/**
	 * Initialises the image database.
	 */
	static {
		try {
			ObjectMapper om = new ObjectMapper();
			om.configure( JsonParser.Feature.ALLOW_COMMENTS, true );
			String jsonText = Resources.loadResourceAsString( DATABASE_NAME ); // workaround for bug #779, see http://jira.codehaus.org/browse/JACKSON-779
			imageDatabase = om.readTree( jsonText );
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
			JsonNode imagesNode = imageDatabase.get( "images" );
			JsonNode copyrightsNode = imageDatabase.get( "copyrights" );
			JsonNode licencesNode = imageDatabase.get( "licences" );

			JsonNode imageNode = imagesNode.get( imageName );
			String typeName = imageNode.get( "type" ).getTextValue();
			ImageResource.Type type = Enum.valueOf( ImageResource.Type.class, typeName );

			String copyrightNodeName = imageNode.get( "copyright" ).getTextValue();
			JsonNode copyrightNode = copyrightsNode.get( copyrightNodeName );
			String copyrightText = copyrightNode.get( "copyright" ).getTextValue();

			String licenceNodeName = copyrightNode.get( "licence" ).getTextValue();
			JsonNode licenceNode = licencesNode.get( licenceNodeName );
			String licenceName = licenceNode.get( "name" ).getTextValue();

			String licenceText = Resources.loadResourceAsString( licenceNode.get( "resource" ).getTextValue() );

			switch ( type ) {
				case SVG:
					result = new SVGResource( imageNode.get( "resource" ).getTextValue(), copyrightText, licenceName, licenceText );
					break;
				default:
					throw new AssertionError( "This should not happen" );
			}
		} catch ( Exception e ) {
			throw new IllegalArgumentException( _( "error-loadingimage", imageName ), e );
		}
		return result;
	}
}
