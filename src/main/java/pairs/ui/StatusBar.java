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

import javax.swing.*;
import javax.swing.border.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import static pairs.util.Message.__;

/**
 * The status bar.
 */
class StatusBar extends JPanel {
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = LogManager.getLogger( StatusBar.class );

	/**
	 * Status message.
	 */
	private String message;

	/**
	 * Status message label.
	 */
	private final JLabel messageLabel;

	/**
	 * Time display in seconds.
	 */
	private int seconds;

	/**
	 * Time display label.
	 */
	private final JLabel timeLabel;

	/**
	 * Creates a new status bar.
	 */
	StatusBar() {
		super( new BorderLayout(), true );
		message = "";
		messageLabel = new JLabel( message );
		seconds = 0;
		timeLabel = new JLabel( __( "label-time", 0, 0 ) );

		/* Layout */
		setBorder( new BevelBorder( BevelBorder.RAISED ) );

		messageLabel.setBorder( new BevelBorder( BevelBorder.LOWERED ) );
		add( messageLabel, BorderLayout.CENTER );

		timeLabel.setBorder( new BevelBorder( BevelBorder.LOWERED ) );
		add( timeLabel, BorderLayout.EAST );
	}

	/**
	 * Sets the status message.
	 *
	 * @param msg The new status message.
	 */
	void setStatusMessage( String msg ) {
		messageLabel.setText( msg );
		revalidate();
	}

	/**
	 * Sets the current time.
	 *
	 * @param seconds Time in seconds.
	 *
	 * @throws IllegalArgumentException if seconds is negative.
	 */
	void setTime( int seconds ) {
		if ( seconds < 0 ) {
			String errmsg = __( "error-seconds", seconds );
			logger.error( errmsg );
			throw new IllegalArgumentException( errmsg );
		}
		int minutes = seconds / 60;
		if ( minutes > 99 ) {
			minutes = 99;
			seconds = 59;
		}
		timeLabel.setText( __( "label-time", minutes, seconds % 60 ) );
		revalidate();
	}
}
