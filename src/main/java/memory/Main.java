package memory;

import javax.swing.*;

import org.apache.log4j.BasicConfigurator;

import memory.ui.MainWindow;

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
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				new MainWindow();
			}
		} );
	}
}
