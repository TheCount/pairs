package pairs;

import javax.swing.*;

import org.apache.log4j.BasicConfigurator;

import pairs.ui.MainWindow;

import pairs.util.Message;

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
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				new MainWindow();
			}
		} );
	}
}
