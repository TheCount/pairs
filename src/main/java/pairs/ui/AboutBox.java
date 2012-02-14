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

import java.util.Vector;

import javax.swing.*;

import org.apache.log4j.Logger;

import pairs.util.Copyright;
import pairs.util.Licence;

import static pairs.util.Message._;

/**
 * About box.
 */
class AboutBox extends JDialog {
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = Logger.getLogger( AboutBox.class );

	/**
	 * Default text width in columns.
	 */
	private static final int DEFAULT_COLS = 40;

	/**
	 * Creates a text area with some default properties.
	 *
	 * @param text Text to be displayed.
	 *
	 * @return A suitable text area is returned.
	 */
	private static JTextArea makeTextArea( String text ) {
		JTextArea textArea = new JTextArea( text, 0, DEFAULT_COLS );
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
	 * Pane with copyright information.
	 */
	private class CopyrightPane extends JComponent implements Scrollable, SwingConstants {
		/**
		 * Adds copyright info.
		 *
		 * @param copyright Copyright to provide info for.
		 * @param row Row in which the copyright info is to be placed.
		 */
		private void addCopyrightInfo( final Copyright copyright, final int row ) {
			final Licence licence = copyright.getLicence();
			GridBagConstraints c;
		       
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = row;
			c.insets = defaultInsets();
			add( makeTextArea( copyright.getText() ), c );

			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = row;
			c.insets = defaultInsets();
			add( new JButton( new ButtonAction( "button-showlicence" ) {
				public void actionPerformed( ActionEvent e ) {
					new TextDisplayBox( AboutBox.this, licence.getName(), licence.getText() );
				}
			} ), c );
		}

		/**
		 * Creates a new copyright pane.
		 */
		CopyrightPane() {
			setLayout( new GridBagLayout() );
			GridBagConstraints c;
			int row = 0;

			/* Main copyright notice */
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = row++;
			c.gridwidth = 2;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = defaultInsets();
			add( makeTextArea( _( "about-heading" ) ), c );

			/* GPL button */
			final Licence gpl = Licence.get( "GPL3" );
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = row++;
			c.gridwidth = 2;
			c.anchor = GridBagConstraints.EAST;
			c.insets = defaultInsets();
			add( new JButton( new AbstractAction( gpl.getName() ) {
				public void actionPerformed( ActionEvent e ) {
					new TextDisplayBox( AboutBox.this, gpl.getName(), gpl.getText() );
				}
			} ), c );

			/* Components label */
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = row++;
			c.gridwidth = 2;
			c.anchor = GridBagConstraints.CENTER;
			c.insets = new Insets( 10, 5, 5, 5 );
			add( new JLabel( _( "about-components" ) ), c );

			/* Components copyrights */
			Vector<Copyright> copyrights = Copyright.getAll();
			for ( Copyright copyright: copyrights ) {
				addCopyrightInfo( copyright, row++ );
			}
		}

		public Dimension getPreferredScrollableViewportSize() {
			return getPreferredSize();
		}

		public int getScrollableBlockIncrement( Rectangle visibleRect, int orientation, int direction ) {
			if ( orientation == VERTICAL ) {
				return getComponent( 0 ).getPreferredSize().height;
			} else {
				logger.warn( _( "warn-hscroll" ) );
				return 100;
			}
		}

		public boolean getScrollableTracksViewportHeight() {
			return false;
		}

		public boolean getScrollableTracksViewportWidth() {
			return false;
		}

		public int getScrollableUnitIncrement( Rectangle visibleRect, int orientation, int direction ) {
			if ( orientation == VERTICAL ) {
				return getComponent( 0 ).getGraphics().getFontMetrics().getHeight();
			} else {
				logger.warn( _( "warn-hscroll" ) );
				return 20;
			}
		}
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
		setLayout( new BorderLayout() );
		add( new JScrollPane( new CopyrightPane(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ), BorderLayout.CENTER );
		add( new JButton( new ButtonAction( "button-close" ) {
			public void actionPerformed( ActionEvent e ) {
				AboutBox.this.dispose();
			}
		} ), BorderLayout.SOUTH );

		/* Display */
		pack();
		Dimension size = getSize( null );
		size.height = Integer.MAX_VALUE;
		setMaximumSize( new Dimension( size ) );
		size.height = 10;
		setMinimumSize( new Dimension( size ) );
		setLocationRelativeTo( null );
		setVisible( true );
	}
}
