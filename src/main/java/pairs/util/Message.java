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

package pairs.util;

import java.text.MessageFormat;

import java.util.IllegalFormatException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Message retrieval.
 */
public final class Message {
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = LogManager.getLogger( Message.class );

	/**
	 * Messages bundle.
	 */
	private static final ResourceBundle messages = ResourceBundle.getBundle( "Messages", new ResourceBundleControl() );

	/**
	 * Message key.
	 */
	private final String key;

	/**
	 * Message arguments.
	 */
	private final Object[] args;

	/**
	 * Creates a new message.
	 *
	 * @param key Message key.
	 * @param args Message arguments.
	 *
	 * @throws NullPointerException if key is null.
	 */
	private Message( String key, Object[] args ) {
		if ( key == null ) {
			throw new NullPointerException();
		}
		this.key = key;
		this.args = args;
	}

	/**
	 * Renders a message.
	 *
	 * @return The rendered message is returned.
	 * 	If the key the message was constructed with does not exist, the empty string is returned.
	 * 	If formatting the message fails, the raw format string is returned.
	 */
	public String toString() {
		String format;
		try {
			format = messages.getString( key );
		} catch ( MissingResourceException e ) {
			logger.error( "Message key not found: " + key, e );
			return "";
		}
		if ( args == null ) {
			return format;
		}
		try {
			return MessageFormat.format( format, args );
		} catch ( IllegalFormatException e ) {
			logger.error( "Invalid message format: " + format, e );
			return format;
		}
	}

	/**
	 * Obtains a message.
	 *
	 * @param key Message key.
	 * @param args Message args.
	 *
	 * @return The appropriate message is returned.
	 */
	public static Message get( String key, Object... args ) {
		return new Message( key, args );
	}

	/**
	 * Obtains a message without parameters.
	 *
	 * @param key Message key.
	 *
	 * @return The appropriate message is returned.
	 */
	public static Message get( String key ) {
		return new Message( key, null );
	}

	/**
	 * Obtains a rendered message.
	 *
	 * @param key Message key.
	 * @param args Message args.
	 *
	 * @return The rendered message string is returned.
	 */
	public static String __( String key, Object... args ) {
		return new Message( key, args ).toString();
	}

	/**
	 * Obtains a rendered message without parameters.
	 *
	 * @param key Message key.
	 *
	 * @return The rendered message string is returned.
	 */
	public static String __( String key ) {
		return new Message( key, null ).toString();
	}

	/**
	 * Checks whether a message is empty.
	 *
	 * @return Whether the message is empty is returned.
	 */
	public static boolean isEmpty( String key ) {
		if ( key == null ) {
			return true;
		}
		return "".equals( new Message( key, null ).toString().trim() );
	}
}
