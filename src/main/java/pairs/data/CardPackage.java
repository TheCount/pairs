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

import java.io.IOException;

import java.util.Arrays;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;

import org.codehaus.jackson.map.ObjectMapper;

import pairs.util.ImageResourceLoader;
import pairs.util.Random;
import pairs.util.Resources;

import static pairs.util.Message._;

/**
 * A card package.
 */
public class CardPackage {
	/**
	 * Card packages database resource name.
	 */
	private static final String DATABASE_NAME = "packages.json";

	/**
	 * Object mapper for loading packages.
	 */
	private static final ObjectMapper objectMapper;

	/**
	 * Packages database.
	 */
	private static final JsonNode database;

	/**
	 * Initialises the card packages database.
	 */
	static {
		try {
			objectMapper = new ObjectMapper();
			objectMapper.configure( JsonParser.Feature.ALLOW_COMMENTS, true );

			String jsonText = Resources.loadResourceAsString( DATABASE_NAME ); // workaround for bug #779, see http://jira.codehaus.org/browse/JACKSON-779
			database = objectMapper.readTree( jsonText );
		} catch ( IOException e ) {
			throw new ExceptionInInitializerError( e );
		}
	}

	/**
	 * Package name.
	 */
	private final String name;

	/**
	 * Card pairs.
	 */
	private final CardPair[] cardPairs;

	/**
	 * Creates a new card package.
	 *
	 * @param name Card package name.
	 * @param cardPairs Array of card pairs.
	 *
	 * @throws NullPointerException if one of the arguments is null.
	 */
	protected CardPackage( String name, CardPair[] cardPairs ) {
		if ( ( name == null ) || ( cardPairs == null ) ) {
			throw new NullPointerException();
		}
		this.name = name;
		this.cardPairs = cardPairs;
	}

	/**
	 * Gets the name of this package.
	 *
	 * @return The name of this package is returned.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the size of this package.
	 *
	 * @return The number of card pairs in this package is returned.
	 */
	public int size() {
		return cardPairs.length;
	}

	/**
	 * Returns a random sample of this card package.
	 *
	 * @param n Sample size.
	 *
	 * @return A random sample of n card pairs is returned.
	 *
	 * @throws IllegalArgumentException if n is negative or greather than the package size.
	 */
	public CardPair[] createRandomSample( int n ) {
		if ( ( n < 0 ) || ( n > cardPairs.length ) ) {
			throw new IllegalArgumentException( _( "error-randomsample", name, cardPairs.length, n ) );
		}
		Random.randomiseArray( cardPairs ); // Yep, always randomise the entire array. The niftier methods might be too much for Java's 48-bit default RNG
		return Arrays.copyOf( cardPairs, n );
	}

	/**
	 * Obtains the named card package.
	 *
	 * @param name Name of the requested card package.
	 *
	 * @return The named card package is returned.
	 *
	 * @throws IllegalArgumentException if there is no package with the specified name.
	 */
	public static CardPackage get( String name ) {
		try {
			JsonNode packageNode = database.get( name );
			String packageKey = packageNode.get( "key" ).getTextValue();
			String packageResourceName = packageNode.get( "resource" ).getTextValue();

			String jsonText = Resources.loadResourceAsString( packageResourceName );
			JsonNode pairs = objectMapper.readTree( jsonText ).get( "pairs" );

			int size = pairs.size();
			CardPair[] cardPairs = new CardPair[ size ];
			for( int i = 0; i != size; ++i ) {
				JsonNode pair = pairs.get( i );
				Card[] cardPair = new Card[ 2 ];
				for( int j = 0; j != 2; ++j ) {
					JsonNode card = pair.get( j );
					Card.Type type = Card.Type.valueOf( Card.Type.class, card.get( "type" ).getTextValue() );
					switch( type ) {
						case IMAGE:
							cardPair[ j ] = new Card( ImageResourceLoader.load( card.get( "image" ).getTextValue() ) );
							break;
						case TEXT:
							cardPair[ j ] = new Card( card.get( "value" ).getTextValue() );
							break;
					}
				}
				cardPairs[ i ] = new CardPair( cardPair[ 0 ], cardPair[ 1 ] );
			}
			return new CardPackage( _( packageKey ), cardPairs );
		} catch ( Exception e ) {
			throw new IllegalArgumentException( _( "error-cardpackage", name ), e );
		}
	}
}