package memory;

import org.apache.log4j.BasicConfigurator;

import memory.util.Message;

/**
 * Main class.
 */
public class Main {
	static {
		/* Init logger */
		BasicConfigurator.configure();
	}

	/**
	 * Entry point.
	 *
	 * @param args Program arguments.
	 */
	public static void main( String... args ) {
		System.out.println( Message.get( "label-test" ) );
	}
}
