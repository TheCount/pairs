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
import java.util.Iterator;
import java.util.Vector;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pairs.util.ImageResourceLoader;
import pairs.util.Random;
import pairs.util.Resources;

import static pairs.util.Message.__;

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
	 * Package description.
	 */
	private final String description;

	/**
	 * Card pairs.
	 */
	private final CardPair[] cardPairs;

	/**
	 * Creates a new card package.
	 *
	 * @param name Card package name.
	 * @param description Package description.
	 * @param cardPairs Array of card pairs.
	 *
	 * @throws NullPointerException if one of the arguments is null.
	 */
	protected CardPackage( String name, String description, CardPair[] cardPairs ) {
		if ( ( name == null ) || ( cardPairs == null ) ) {
			throw new NullPointerException();
		}
		this.name = name;
		this.description = description;
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
	 * Gets the description of this package.
	 *
	 * @return The description of this package is returned.
	 */
	public String getDescription() {
		return description;
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
			throw new IllegalArgumentException( __( "error-randomsample", name, cardPairs.length, n ) );
		}
		Random.randomiseArray( cardPairs ); // Yep, always randomise the entire array. The niftier methods might be too much for Java's 48-bit default RNG
		return Arrays.copyOf( cardPairs, n );
	}

	public @Override String toString() {
		return getName();
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
			String packageKey = packageNode.get( "key" ).textValue();
			String packageResourceName = packageNode.get( "resource" ).textValue();

			String jsonText = Resources.loadResourceAsString( packageResourceName );
			JsonNode packageResource = objectMapper.readTree( jsonText );
			String packageDescriptionKey = packageResource.get( "descriptionKey" ).textValue();
			JsonNode pairs = packageResource.get( "pairs" );

			int size = pairs.size();
			CardPair[] cardPairs = new CardPair[ size ];
			for( int i = 0; i != size; ++i ) {
				JsonNode pair = pairs.get( i );
				Card[] cardPair = new Card[ 2 ];
				for( int j = 0; j != 2; ++j ) {
					JsonNode card = pair.get( j );
					Card.Type type = Card.Type.valueOf( Card.Type.class, card.get( "type" ).textValue() );
					switch( type ) {
						case IMAGE:
							cardPair[ j ] = new Card( ImageResourceLoader.load( card.get( "image" ).textValue() ) );
							break;
						case TEXT:
							cardPair[ j ] = new Card( card.get( "value" ).textValue() );
							break;
					}
				}
				cardPairs[ i ] = new CardPair( cardPair[ 0 ], cardPair[ 1 ] );
			}
			return new CardPackage( __( packageKey ), __( packageDescriptionKey ), cardPairs );
		} catch ( Exception e ) {
			throw new IllegalArgumentException( __( "error-cardpackage", name ), e );
		}
	}

	/**
	 * Obtains all card packages.
	 *
	 * @return An array containing all card packages is returned.
	 */
	public static Vector<CardPackage> getAll() {
		Vector<CardPackage> result = new Vector();
		Iterator<String> fieldNameIterator = database.fieldNames();
		while ( fieldNameIterator.hasNext() ) {
			result.add( get( fieldNameIterator.next() ) );
		}
		return result;
	}
}
