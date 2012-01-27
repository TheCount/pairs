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
		super( Message.get( "label-concentration" ).toString() );
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		JLabel label = new JLabel( Message.get( "label-concentration" ).toString() );
		getContentPane().add( label );

		pack();
		setVisible( true );
	}
}
