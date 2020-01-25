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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import pairs.data.Card;
import pairs.data.CardPackage;
import pairs.data.CardPair;

import pairs.util.Random;
import pairs.util.UnorderedPair;

import static pairs.model.PlayfieldChangeListener.*;

import static pairs.util.Message.__;

/**
 * Pairs playfield model.
 */
public class PlayfieldModel {
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = LogManager.getLogger( PlayfieldModel.class );

	/**
	 * Allowed playfield sizes.
	 */
	private static final Integer[] ALLOWED_SIZES = new Integer[] {
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
	 * Returns the allowed playfield sizes.
	 *
	 * @return The allowed playfield sizes are returned.
	 */
	public static Integer[] getAllowedSizes() {
		return Arrays.copyOf( ALLOWED_SIZES, ALLOWED_SIZES.length );
	}

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
	 * Index of the card currently picked, or -1 if none.
	 */
	private int pickedCardIndex;

	/**
	 * Array indicating which cards are currently shown.
	 */
	private final boolean[] cardsShown;

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
	 * Listener list.
	 */
	private final List<PlayfieldChangeListener> listenerList;

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
			logger.warn( __( "warn-playfieldsize", ALLOWED_SIZES[ sizeIndex ], sizeHint ) );
		}
		this.size = ALLOWED_SIZES[ sizeIndex ];
		this.width = PLAYFIELD_DIMENSIONS[ sizeIndex ][ 0 ];
		this.height = PLAYFIELD_DIMENSIONS[ sizeIndex ][ 1 ];

		/* Cards and card pairs */
		CardPair[] cardPairs = cardPackage.createRandomSample( this.size / 2 );
		this.cards = new Card[ this.size ];
		int index = 0;
		for ( final CardPair cardPair: cardPairs ) {
			this.cards[ index++ ] = cardPair.getFirst();
			this.cards[ index++ ] = cardPair.getSecond();
		}
		Random.randomiseArray( this.cards );
		this.cardPairs = new HashSet( Arrays.asList( cardPairs ) );
		if ( 2 * this.cardPairs.size() != this.cards.length ) {
			throw new AssertionError( "This should not happen" );
		}
		this.pickedCardIndex = -1;
		this.cardsShown = new boolean[ this.size ];
		for ( int i = 0; i != this.size; ++i ) {
			this.cardsShown[ i ] = false;
		}

		/* Game statistics */
		this.cardsWon = new boolean[ this.size ];
		for ( int i = 0; i != this.size; ++i ) {
			this.cardsWon[ i ] = false;
		}
		this.cardPairsLeft = cardPairs.length;
		this.failedPicks = 0;
		this.listenerList = new LinkedList();
	}

	/**
	 * Adds a listener listening for playfield change events.
	 *
	 * @param listener Playfield change listener.
	 */
	public void addChangeListener( PlayfieldChangeListener listener ) {
		listenerList.add( listener );
	}

	/**
	 * Informs all listeners of a change.
	 *
	 * @param i Index of the card that has changed.
	 * @param type Change type.
	 */
	protected void firePlayfieldChanged( int i, ChangeType type ) {
		for ( PlayfieldChangeListener listener: listenerList ) {
			listener.playfieldChanged( this, i, type );
		}
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
	 * Gets the card at the specified index.
	 *
	 * @param i Index.
	 *
	 * @return The card at the specified index is returned.
	 *
	 * @throws IndexOutOfBoundsException if x or y is outside the playfield bounds.
	 */
	public Card getCard( int i ) {
		return cards[ i ];
	}

	/**
	 * Returns the currently picked card.
	 *
	 * @return The currently picked card is returned, or null if no card is currently picked.
	 */
	public Card getPickedCard() {
		if ( pickedCardIndex == -1 ) {
			return null;
		} else {
			return cards[ pickedCardIndex ];
		}
	}

	/**
	 * Returns the index of the currently picked card.
	 *
	 * @return The index of the currently picked card is returned, or -1 if no card is currently picked.
	 */
	public int getPickedCardIndex() {
		return pickedCardIndex;
	}

	/**
	 * Picks a card at the specified index.
	 * If the card is already won, no operation is performed.
	 * If the card is the currently picked card, no operation is performed.
	 * If no card is currently picked, the card at the specified position becomes the picked card.
	 * Otherwise, if the currently picked card and the specified card form a valid pair, they are marked as won.
	 * Otherwise, no card will be marked as picked, and the number of failed picks is increased by one.
	 *
	 * @param i Index.
	 *
	 * @throws IndexOutOfBoundsException if i is out of bounds.
	 */
	public void pickCard( int i ) {
		Card wannaPick = cards[ i ];
		if ( cardsWon[ i ] ) {
			return;
		}
		if ( pickedCardIndex == -1 ) {
			pickedCardIndex = i;
			cardsShown[ i ] = true;
			firePlayfieldChanged( i, ChangeType.CARD_SHOWN );
			return;
		}
		if ( pickedCardIndex == i ) {
			return;
		}
		Card pickedCard = cards[ pickedCardIndex ];
		CardPair candidate = new CardPair( pickedCard, wannaPick );
		if ( cardPairs.contains( candidate ) ) {
			cardsWon[ i ] = true;
			cardsWon[ pickedCardIndex ] = true;
			--cardPairsLeft;
			firePlayfieldChanged( i, ChangeType.CARD_REMOVED );
			firePlayfieldChanged( pickedCardIndex, ChangeType.CARD_REMOVED );
			if ( cardPairsLeft == 0 ) {
				firePlayfieldChanged( i, ChangeType.GAME_WON );
			}
		} else {
			++failedPicks;
			cardsShown[ i ] = false;
			cardsShown[ pickedCardIndex ] = false;
			firePlayfieldChanged( i, ChangeType.CARD_HIDDEN );
			firePlayfieldChanged( pickedCardIndex, ChangeType.CARD_HIDDEN );
		}
		pickedCardIndex = -1;
		return;
	}

	/**
	 * Returns whether the card at the specified index is won.
	 *
	 * @param i Index.
	 *
	 * @return If the card at the specified index is already won, true is returned.
	 * 	Otherwise, false is returned.
	 *
	 * @throws IndexOutOfBoundsException if i is out of bounds.
	 */
	public boolean isWon( int i ) {
		return cardsWon[ i ];
	}

	/**
	 * Returns whether the card at the specified index is shown.
	 *
	 * @param i Index.
	 *
	 * @return If the card at the specified index is currently shown, true is returned.
	 * 	Otherwise, false is returned.
	 *
	 * @throws IndexOutOfBoundsException if i is out of bounds.
	 */
	public boolean isShown( int i ) {
		return cardsShown[ i ];
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
