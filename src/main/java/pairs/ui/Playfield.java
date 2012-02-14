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

package pairs.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import pairs.data.Card;
import pairs.data.CardPackage;

import pairs.model.PlayfieldChangeListener;
import pairs.model.PlayfieldModel;

import static pairs.model.PlayfieldChangeListener.*;

/**
 * Playing field.
 */
class Playfield extends JComponent implements PlayfieldChangeListener {
	/**
	 * Delay before turning cards over again in ms.
	 */
	private static final int TURN_DELAY = 1000;

	/**
	 * Unknown card text.
	 */
	private static final String UNKNOWN_CARD_TEXT = "?";

	/**
	 * Playfield model.
	 */
	private final PlayfieldModel playfieldModel;

	/**
	 * Owner.
	 */
	private final MainWindow owner;

	/**
	 * Card button.
	 */
	private class CardButton extends JButton {
		/**
		 * Button index.
		 */
		private final int index;

		/**
		 * Turn timer.
		 */
		private final Timer turnTimer;

		/**
		 * Creates a new card button.
		 *
		 * @param i Button index.
		 */
		CardButton( int i ) {
			super();
			this.index = i;
			this.turnTimer = new Timer( TURN_DELAY, new ActionListener() {
				public void actionPerformed( ActionEvent event ) {
					setText( playfieldModel.isWon( index ) ? null : UNKNOWN_CARD_TEXT );
					setIcon( null );
				}
			} );
			this.turnTimer.setRepeats( false );
			setAction( new AbstractAction( UNKNOWN_CARD_TEXT ) {
				public void actionPerformed( ActionEvent event ) {
					if ( playfieldModel.isWon( index ) ) {
						return;
					}
					showCard();
					cardClicked( index );
				}
			} );
 
		}

		/**
		 * Shows the card.
		 */
		public void showCard() {
			turnTimer.stop();
			Card card = playfieldModel.getCard( index );
			switch ( card.getType() ) {
				case IMAGE:
					setText( null );
					setIcon( new ImageIcon( card.getImageResource().createImage( getWidth() / 2, getHeight() / 2 ) ) );
					break;
				case TEXT:
					setText( card.getText() );
					setIcon( null );
					break;
			}
		}

		/**
		 * Hides the card.
		 */
		public void hideCard() {
			turnTimer.restart();
		}

		/**
		 * Removes the card.
		 */
		public void removeCard() {
			turnTimer.restart();
		}
	}

	/**
	 * Card button array.
	 */
	private final CardButton[] cardButtons;

	/**
	 * Creates a new playfield component.
	 *
	 * @param owner Owner of this playfield.
	 * @param cardPackage Card package to use.
	 * @param sizeHint Playfield size hint.
	 *
	 * @throws NullPointerException if cardPackage is null.
	 */
	Playfield( MainWindow owner, CardPackage cardPackage, int sizeHint ) {
		this.owner = owner;

		/* Create model */
		playfieldModel = new PlayfieldModel( cardPackage, sizeHint );

		/* Layout */
		int size = playfieldModel.getSize();
		setLayout( new GridLayout( playfieldModel.getHeight(), playfieldModel.getWidth() ) );
		cardButtons = new CardButton[ size ];
		for ( int i = 0; i != size; ++i ) {
			CardButton button = new CardButton( i );
			cardButtons[ i ] = button;
			add( button );
		}
		playfieldModel.addChangeListener( this );
	}

	/**
	 * Triggered when a card is clicked.
	 *
	 * @param i Index of card clicked.
	 */
	private void cardClicked( int i ) {
		playfieldModel.pickCard( i );
	}

	/**
	 * Triggered when the playfield changes.
	 *
	 * @param playfieldModel Playfield mode which changed.
	 * @param i Index of card that changed.
	 * @param type Change type.
	 */
	public void playfieldChanged( PlayfieldModel playfieldModel, int i, ChangeType type ) {
		switch ( type ) {
			case CARD_HIDDEN:
				cardButtons[ i ].hideCard();
				break;
			case CARD_REMOVED:
				cardButtons[ i ].removeCard();
				break;
			case GAME_WON:
				owner.gameWon();
				break;
		}
	}
}
