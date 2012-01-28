package memory.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.net.URL;
import java.net.URLConnection;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Resource bundle control for UTF-8 properties files.
 *
 * Someone at Sun obviously forgot to add the ability to load property
 * resources in UTF-8 format. Hence this class to workaround the problem.
 */
public class ResourceBundleControl extends ResourceBundle.Control {
	/**
	 * Resource bundle format.
	 */
	private static final String FORMAT = "properties";

	/**
	 * Resource bundle file encoding.
	 */
	private static final String ENCODING = "UTF-8";

	// default constructed
	
	public @Override List<String> getFormats( final String baseName ) {
		if ( baseName == null ) {
			throw new NullPointerException( "Supplied baseName is null" ); // per spec
		}
		return Collections.singletonList( FORMAT );
	}

	public @Override ResourceBundle newBundle( final String baseName, final Locale locale, final String format, final ClassLoader loader, final boolean reload ) throws IOException {
		/* throw some null pointer exceptions per spec */
		if ( baseName == null ) {
			throw new NullPointerException( "Supplied baseName is null" );
		}
		if ( locale == null ) {
			throw new NullPointerException( "Supplied locale is null" );
		}
		if (format == null) {
			throw new NullPointerException( "Supplied format is null" );
		}
		if ( loader == null ) {
			throw new NullPointerException( "Supplied class loader is null" );
		}
		/* We do only properties */
		if ( !FORMAT.equals(format) ) {
			return null;
		}
		/* open input stream for bundle data */
		final String bundleName = toBundleName( baseName, locale );
		final String resourceName = toResourceName( bundleName, format ); // possible NPE per spec
		InputStream stream;
		if ( reload ) { // as suggested by API spec example
			final URL url = loader.getResource( resourceName );
			if ( url == null ) {
				return null;
			}
			final URLConnection connection = url.openConnection();
			if ( connection == null ) {
				return null;
			}
			connection.setUseCaches( false );
			stream = connection.getInputStream();
		} else {
			stream = loader.getResourceAsStream( resourceName );
			if ( stream == null ) {
			       return null;
			}	       
		}
		/* create and return bundle */
		final InputStreamReader reader = new InputStreamReader( stream, ENCODING );
		final PropertyResourceBundle result = new PropertyResourceBundle( reader );
		reader.close();
		return result;
	}

}
