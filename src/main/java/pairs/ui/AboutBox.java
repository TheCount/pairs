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
	 * Creates a new about box.
	 *
	 * @param owner Owner of the about box.
	 */
	AboutBox( Frame owner ) {
		super( owner, _( "label-about" ) );

		/* Dispose on close */
		setDefaultCloseOperation( DISPOSE_ON_CLOSE );

		/* Layout */
		setLayout( new BorderLayout() );
		add( new JTextArea( _( "about-heading" ) ), BorderLayout.NORTH );
		add( new JButton( new ButtonAction( "button-close" ) {
			public void actionPerformed( ActionEvent e ) {
				AboutBox.this.dispose();
			}
		} ), BorderLayout.SOUTH );

		/* Display */
		pack();
		setVisible( true );
	}
}
