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

import pairs.data.CardPackage;

import pairs.model.PlayfieldModel;

import static pairs.util.Message._;

/**
 * New game dialog.
 */
class NewGame extends JDialog {
	/**
	 * Default insets.
	 */
	private static final Insets DEFAULT_INSETS = new Insets( 5, 5, 5, 5 );

	/**
	 * Owner of this dialog.
	 */
	private final MainWindow owner;

	/**
	 * Playfield size selector.
	 */
	private static class SizeSelector extends JSpinner implements MouseWheelListener {
		/**
		 * Creates the size selector.
		 */
		SizeSelector() {
			super( new SpinnerListModel( PlayfieldModel.getAllowedSizes() ) );
			setValue( 24 );
			addMouseWheelListener( this );
		}

		public void mouseWheelMoved( MouseWheelEvent event ) {
			try {
				if ( event.getWheelRotation() > 0 ) {
					setValue( getModel().getPreviousValue() );
				} else if ( event.getWheelRotation() < 0 ) {
					setValue( getModel().getNextValue() );
				}
			} catch ( IllegalArgumentException e ) {
				// ignored
			}
		}
	}

	/**
	 * Creates a new New Game dialog.
	 *
	 * @param owner Owner of the NewGame dialog.
	 */
	NewGame( MainWindow owner ) {
		super( owner, _( "label-newgame" ) );
		this.owner = owner;

		setDefaultCloseOperation( DISPOSE_ON_CLOSE );
		setLayout( new GridBagLayout() );
		GridBagConstraints c;

		/* Package label */
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		c.insets = DEFAULT_INSETS;
		JLabel packageLabel = new JLabel( _( "label-package" ) );
		add( packageLabel, c );

		/* Package selector */
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		c.insets = DEFAULT_INSETS;
		final JComboBox packageSelector = new JComboBox( CardPackage.getAll() );
		packageLabel.setLabelFor( packageSelector );
		add( packageSelector, c );

		/* Size selection label */
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		c.insets = DEFAULT_INSETS;
		JLabel sizeLabel = new JLabel( _( "label-playfieldsize" ) );
		add( sizeLabel, c );

		/* Playfield size selector */
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = DEFAULT_INSETS;
		final SizeSelector sizeSelector = new SizeSelector(); 
		sizeLabel.setLabelFor( sizeSelector );
		add( sizeSelector, c );

		/* OK button */
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = DEFAULT_INSETS;
		add( new JButton( new ButtonAction( "button-ok" ) {
			public void actionPerformed( ActionEvent event ) {
				NewGame.this.owner.resetPlayfield( (CardPackage) packageSelector.getSelectedItem(), (Integer) sizeSelector.getValue() );
				NewGame.this.dispose();
			}
		} ), c );

		/* Cancel button */
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = DEFAULT_INSETS;
		add( new JButton( new ButtonAction( "button-cancel" ) {
			public void actionPerformed( ActionEvent event ) {
				NewGame.this.dispose();
			}
		} ), c );

		/* Display dialogue */
		pack();
		setLocationRelativeTo( null );
		setVisible( true );
	}
}
