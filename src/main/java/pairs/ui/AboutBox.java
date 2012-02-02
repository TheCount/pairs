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

import static pairs.util.Message._;

/**
 * About box.
 */
class AboutBox extends JDialog {
	/**
	 * Default text width in columns.
	 */
	private static final int DEFAULT_COLS = 50;

	/**
	 * Creates a text area with some default properties.
	 *
	 * @param textKey Text key of displayed text.
	 *
	 * @return A suitable text area is returned.
	 */
	private static JTextArea makeTextArea( String textKey ) {
		JTextArea textArea = new JTextArea( _( textKey ), 0, DEFAULT_COLS );
		textArea.setEditable( false );
		textArea.setLineWrap( true );
		textArea.setWrapStyleWord( true );
		return textArea;
	}

	/**
	 * Creates default insets.
	 *
	 * @return Default insets are returned.
	 */
	private static Insets defaultInsets() {
		return new Insets( 5, 5, 5, 5 );
	}

	/**
	 * Creates a new about box.
	 *
	 * @param owner Owner of the about box.
	 */
	AboutBox( Frame owner ) {
		super( owner, _( "label-about" ) );

		/* Dispose on close */
		setDefaultCloseOperation( DISPOSE_ON_CLOSE );

		/* Layout */
		setLayout( new GridBagLayout() );
		GridBagConstraints c;

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = defaultInsets();
		add( makeTextArea( "about-heading" ), c );

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.insets = defaultInsets();
		add( makeTextArea( "about-source" ), c );

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.insets = defaultInsets();
		add( new JButton( new ButtonAction( "button-showlicence" ) {
			public void actionPerformed( ActionEvent e ) {
				// FIXME
			}
		} ), c );

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.insets = defaultInsets();
		add( makeTextArea( "about-log4j" ), c );

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.insets = defaultInsets();
		add( new JButton( new ButtonAction( "button-showlicence" ) {
			public void actionPerformed( ActionEvent e ) {
				// FIXME
			}
		} ), c );

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.insets = defaultInsets();
		add( new JButton( new ButtonAction( "button-close" ) {
			public void actionPerformed( ActionEvent e ) {
				AboutBox.this.dispose();
			}
		} ), c );

		/* Display */
		pack();
		setVisible( true );
	}
}
