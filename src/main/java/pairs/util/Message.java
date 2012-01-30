package pairs.util;

import java.util.IllegalFormatException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * Message retrieval.
 */
public final class Message {
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = Logger.getLogger( Message.class );

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
	private Message( String key, String[] args ) {
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
	 */
	public String toString() {
		String format;
		try {
			format = messages.getString( key );
		} catch ( MissingResourceException e ) {
			logger.error( "Message key not found: " + key, e );
			format = key;
		}
		if ( args == null ) {
			return format;
		}
		try {
			return String.format( format, args );
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
	public static Message get( String key, String... args ) {
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
	public static String _( String key, String... args ) {
		return new Message( key, args ).toString();
	}

	/**
	 * Obtains a rendered message without parameters.
	 *
	 * @param key Message key.
	 *
	 * @return The rendered message string is returned.
	 */
	public static String _( String key ) {
		return new Message( key, null ).toString();
	}
}
