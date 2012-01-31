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

import java.awt.Event;

import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import static pairs.util.Message._;

/**
 * Abstract menu item action.
 */
abstract class MenuItemAction extends AbstractAction {
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = Logger.getLogger( MenuItemAction.class );

	/**
	 * Parses a single VK_* key.
	 *
	 * @param key Key string.
	 *
	 * @return The appropriate key code is returned.
	 *
	 * @throws NullPointerException if key is null.
	 * @throws IllegalArgumentException if key does not yield a valid key code.
	 */
	private static int parseKey( String key ) {
		if ( key == null ) {
			throw new NullPointerException();
		}
		try {
			return KeyEvent.class.getField( "VK_" + key ).getInt( null );
		} catch ( Exception e ) {
			throw new IllegalArgumentException( _( "error-parsekey", key ), e );
		}
	}

	/**
	 * Parses a keystroke.
	 * The keystroke string may start with "C-" for a control modifier, or "A-" for an Alt modifier, or both.
	 * The remainder is interpreted as a VK_* key.
	 *
	 * @param keyString Keystroke string.
	 *
	 * @return The appropriate keystroke is returned.
	 *
	 * @throws NullPointerException if keyString is null.
	 * @throws IllegalArgumentException if keyString does not yield a valid keystroke.
	 */
	private static KeyStroke parseKeystroke( final String keyString ) {
		if ( keyString == null ) {
			throw new NullPointerException();
		}
		int modifiers = 0;
		String key = keyString;
		for ( ;; ) {
			if ( key.startsWith( "C-" ) ) {
				modifiers |= Event.CTRL_MASK;
				key = key.substring( 2 );
				continue;
			}
			if ( key.startsWith( "A-" ) ) {
				modifiers |= Event.ALT_MASK;
				key = key.substring( 2 );
				continue;
			}
			break;
		}
		try {
			return KeyStroke.getKeyStroke( parseKey( key ), modifiers );
		} catch ( IllegalArgumentException e ) {
			throw new IllegalArgumentException( _( "error-parsekeystroke", keyString ), e );
		}
	}

	/**
	 * Creates a new menu item action.
	 *
	 * @param titleKey Action title key.
	 * @param mnemoKey Action mnemonic key. Ignored if null.
	 * @param accelKey Action accelerator key. Ignored if null.
	 * @param shortdescKey Key for short description. Ignored if null.
	 * @param longdescKey Key for long description. Ignored if null.
	 */
	protected MenuItemAction( String titleKey, String mnemoKey, String accelKey, String shortdescKey, String longdescKey ) {
		super( _( titleKey ) );
		if ( mnemoKey != null ) {
			try {
				putValue( MNEMONIC_KEY, parseKey( _( mnemoKey ) ) );
			} catch ( IllegalArgumentException e ) {
				logger.error( _( "error-mnemonickey", mnemoKey, _( titleKey ) ), e );
			}
		}
		if ( accelKey != null ) {
			try {
				putValue( ACCELERATOR_KEY, parseKeystroke( _( accelKey ) ) );
			} catch ( IllegalArgumentException e ) {
				logger.error( _( "error-accelkey", accelKey, _( titleKey ) ), e );
			}
		}
		if ( shortdescKey != null ) {
			putValue( SHORT_DESCRIPTION, _( shortdescKey ) );
		}
		if ( longdescKey != null ) {
			putValue( LONG_DESCRIPTION, _( longdescKey ) );
		}
	}

	/**
	 * Creates a new menu item action with default parameters derived from the title key.
	 *
	 * @param titleKey Action title key.
	 */
	protected MenuItemAction( String titleKey ) {
		this( titleKey, titleKey + "-mnemo", titleKey + "-accel", titleKey + "-shortdesc", titleKey + "-longdesc" );
	}
}
