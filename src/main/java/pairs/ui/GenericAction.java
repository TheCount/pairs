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

import pairs.util.Message;

import static pairs.util.Message._;

/**
 * Generic action.
 */
abstract class GenericAction extends AbstractAction {
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = Logger.getLogger( GenericAction.class );

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
			if ( key.startsWith( "S-" ) ) {
				modifiers |= Event.SHIFT_MASK;
				key = key.substring( 2 );
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
	 * Creates a new generic action.
	 *
	 * @param titleKey Action title key.
	 * @param mnemoKey Action mnemonic key. Ignored if empty.
	 * @param accelKey Action accelerator key. Ignored if empty.
	 * @param shortdescKey Key for short description. Ignored if empty.
	 * @param longdescKey Key for long description. Ignored if empty.
	 */
	protected GenericAction( String titleKey, String mnemoKey, String accelKey, String shortdescKey, String longdescKey ) {
		super( _( titleKey ) );
		if ( !Message.isEmpty( mnemoKey ) ) {
			try {
				putValue( MNEMONIC_KEY, parseKey( _( mnemoKey ) ) );
			} catch ( IllegalArgumentException e ) {
				logger.error( _( "error-mnemonickey", mnemoKey, _( titleKey ) ), e );
			}
		}
		if ( !Message.isEmpty( accelKey ) ) {
			try {
				putValue( ACCELERATOR_KEY, parseKeystroke( _( accelKey ) ) );
			} catch ( IllegalArgumentException e ) {
				logger.error( _( "error-accelkey", accelKey, _( titleKey ) ), e );
			}
		}
		if ( !Message.isEmpty( shortdescKey ) ) {
			putValue( SHORT_DESCRIPTION, _( shortdescKey ) );
		}
		if ( !Message.isEmpty( longdescKey ) ) {
			putValue( LONG_DESCRIPTION, _( longdescKey ) );
		}
	}

	/**
	 * Creates a new generic action with default parameters derived from the title key.
	 *
	 * @param titleKey Action title key.
	 */
	protected GenericAction( String titleKey ) {
		this( titleKey, titleKey + "-mnemo", titleKey + "-accel", titleKey + "-shortdesc", titleKey + "-longdesc" );
	}
}
