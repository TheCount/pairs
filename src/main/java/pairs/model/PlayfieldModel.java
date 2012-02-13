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

package pairs.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import pairs.data.Card;
import pairs.data.CardPackage;
import pairs.data.CardPair;

import pairs.util.Random;
import pairs.util.UnorderedPair;

import static pairs.util.Message._;

/**
 * Pairs playfield model.
 */
public class PlayfieldModel {
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = Logger.getLogger( PlayfieldModel.class );

	/**
	 * Allowed playfield sizes.
	 */
	private static final int[] ALLOWED_SIZES = new int[] {
		2, 4, 6, 8, 10,
		12, 16, 18, 20, 24,
		28, 30, 32, 36, 40,
		42, 48, 50, 54, 56,
		60, 64, 66, 70, 72,
		80, 84, 88, 90, 96,
		100, 108, 110, 120, 132,
		144
	};

	/**
	 * Playfield dimensions depending on size.
	 */
	private static final int[][] PLAYFIELD_DIMENSIONS = new int[][] {
		{ 2, 1 }, { 2, 2 }, { 3, 2 }, { 4, 2 }, { 5, 2 },
		{ 4, 3 }, { 4, 4 }, { 6, 3 }, { 5, 4 }, { 6, 4 },
		{ 7, 4 }, { 6, 5 }, { 8, 4 }, { 6, 6 }, { 8, 5 },
		{ 7, 6 }, { 8, 6 }, { 10, 5 }, { 9, 6 }, { 8, 7 },
		{ 10, 6 }, { 8, 8 }, { 11, 6 }, { 10, 7 }, { 9, 8 },
		{ 10, 8 }, { 12, 7 }, { 11, 8 }, { 10, 9 }, { 12, 8 },
		{ 10, 10 }, { 12, 9 }, { 11, 10 }, { 12, 10 }, { 12, 11 },
		{ 12, 12 }
	};

	/**
	 * Playfield size.
	 */
	private final int size;

	/**
	 * Playfield width.
	 */
	private final int width;

	/**
	 * Playfield height.
	 */
	private final int height;

	/**
	 * Card pairs this playfield is based on.
	 */
	private final Set<CardPair> cardPairs;

	/**
	 * Cards on the playfield.
	 */
	private final Card[] cards;

	/**
	 * Position of the card currently picked, or -1 if none.
	 */
	private int pickedCardX;
	private int pickedCardY;

	/**
	 * Array indicating whether the respective card has already been won.
	 */
	private final boolean[] cardsWon;

	/**
	 * Cards pairs left to win.
	 */
	private int cardPairsLeft;

	/**
	 * Number of failed picks.
	 */
	private int failedPicks;

	/**
	 * Creates a new random playfield model.
	 *
	 * @param cardPackage Card package this model is based on.
	 * @param sizeHint Suggested size of the playfield.
	 *
	 * @throws NullPointerException if the card package is null.
	 */
	public PlayfieldModel( CardPackage cardPackage, int sizeHint ) {
		if ( cardPackage == null ) {
			throw new NullPointerException();
		}

		/* Playfield dimensions */
		int sizeIndex = Arrays.binarySearch( ALLOWED_SIZES, sizeHint );
		if ( sizeIndex < 0 ) {
			sizeIndex = -sizeIndex - 1;
			if ( sizeIndex >= ALLOWED_SIZES.length ) {
				sizeIndex = ALLOWED_SIZES.length - 1;
			}
			logger.warn( _( "warn-playfieldsize", ALLOWED_SIZES[ sizeIndex ], sizeHint ) );
		}
		this.size = ALLOWED_SIZES[ sizeIndex ];
		this.width = PLAYFIELD_DIMENSIONS[ sizeIndex ][ 0 ];
		this.height = PLAYFIELD_DIMENSIONS[ sizeIndex ][ 1 ];

		/* Cards and card pairs */
		CardPair[] cardPairs = cardPackage.createRandomSample( this.size / 2 );
		this.cards = new Card[ this.size ];
		int index = 0;
		for( final CardPair cardPair: cardPairs ) {
			this.cards[ index++ ] = cardPair.getFirst();
			this.cards[ index++ ] = cardPair.getSecond();
		}
		Random.randomiseArray( this.cards );
		this.cardPairs = new HashSet( Arrays.asList( cardPairs ) );
		if ( 2 * this.cardPairs.size() != this.cards.length ) {
			throw new AssertionError( "This should not happen" );
		}
		this.pickedCardX = -1;
		this.pickedCardY = -1;

		/* Game statistics */
		this.cardsWon = new boolean[ this.size ];
		for( int i = 0; i != this.size; ++i ) {
			this.cardsWon[ i ] = false;
		}
		this.cardPairsLeft = cardPairs.length;
		this.failedPicks = 0;
	}

	/**
	 * Gets the playfield size.
	 *
	 * @return The playfield size is returned.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Gets the playfield width.
	 *
	 * @return The playfield width is returned.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the playfield height.
	 *
	 * @return The playfield height is returned.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the card at the specified position.
	 *
	 * @param x X position.
	 * @param y Y position.
	 *
	 * @return The card at the specified position is returned.
	 *
	 * @throws IndexOutOfBoundsException if x or y is outside the playfield bounds.
	 */
	public Card getCard( int x, int y ) {
		if ( ( x < 0 ) || ( x >= width ) || ( y < 0 ) || ( y >= height ) ) {
			throw new IndexOutOfBoundsException();
		}
		return cards[ y * height + x ];
	}

	/**
	 * Returns the currently picked card.
	 *
	 * @return The currently picked card is returned, or null if no card is currently picked.
	 */
	public Card getPickedCard() {
		return getCard( pickedCardX, pickedCardY );
	}

	/**
	 * Picks a card at the specified position.
	 * If the card is already won, no operation is performed.
	 * If the card is the currently picked card, no operation is performed.
	 * If no card is currently picked, the card at the specified position becomes the picked card.
	 * Otherwise, if the currently picked card and the specified card form a valid pair, they are marked as won.
	 * Otherwise, no card will be marked as picked, and the number of failed picks is increased by one.
	 *
	 * @param x X position.
	 * @param y Y position.
	 *
	 * @throws IndexOutOfBoundsException if x or y is outside the playfield bounds.
	 */
	public void pickCard( int x, int y ) {
		Card wannaPick = getCard( x, y );
		if ( cardsWon[ y * height + x ] ) {
			return;
		}
		if ( pickedCardX == -1 ) {
			pickedCardX = x;
			pickedCardY = y;
			return;
		}
		Card pickedCard = getCard( pickedCardX, pickedCardY );
		if ( pickedCard == wannaPick ) {
			return;
		}
		CardPair candidate = new CardPair( pickedCard, wannaPick );
		if ( cardPairs.contains( candidate ) ) {
			cardsWon[ y * height + x ] = true;
			cardsWon[ pickedCardY * height + pickedCardX ] = true;
			--cardPairsLeft;
		} else {
			++failedPicks;
		}
		pickedCardX = -1;
		pickedCardY = -1;
	}

	/**
	 * Returns whether the card at the specified position is won.
	 *
	 * @param x X position.
	 * @param y Y position.
	 *
	 * @return If the card at the specified position is already won, true is returned.
	 * 	Otherwise, false is returned.
	 *
	 * @throws IndexOutOfBoundsException if x or y is outside the playfield bounds.
	 */
	public boolean isWon( int x, int y ) {
		if ( ( x < 0 ) || ( x >= width ) || ( y < 0 ) || ( y >= height ) ) {
			throw new IndexOutOfBoundsException();
		}
		return cardsWon[ y * height + x ];
	}

	/**
	 * Returns how many card pairs are left to find.
	 *
	 * @return The number of remaining card pairs is returned.
	 */
	public int cardPairsLeft() {
		return cardPairsLeft;
	}

	/**
	 * Returns whether the game is won.
	 *
	 * @return If the game is won, true is returned.
	 * 	Otherwise, false is returned.
	 */
	public boolean isAllWon() {
		return ( cardPairsLeft() == 0 );
	}

	/**
	 * Gets the number of failed picks.
	 *
	 * @return The number of failed picks is returned.
	 */
	public int failedPicks() {
		return failedPicks;
	}
}
