package memory.ui;

import javax.swing.*;

import memory.util.Message;

/**
 * Main window.
 */
public class MainWindow extends JFrame {
	/**
	 * Displays main window.
	 */
	public MainWindow() {
		super( Message.get( "label-test" ).toString() );
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		JLabel label = new JLabel( Message.get( "label-test" ).toString() );
		getContentPane().add( label );

		pack();
		setVisible( true );
	}
}
