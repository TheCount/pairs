package memory.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import static memory.util.Message._;

/**
 * Main window.
 */
public class MainWindow extends JFrame {
	/**
	 * Displays main window.
	 */
	public MainWindow() {
		super( _( "label-concentration" ) );
		setDefaultCloseOperation( EXIT_ON_CLOSE );

		/* Menu */
		JMenu fileMenu = new JMenu( _( "menu-file" ) );
		fileMenu.add( new AbstractAction( _( "menu-exit" ) ) {
			public void actionPerformed( ActionEvent e ) {
				System.exit( 0 );
			}
		} );
		JMenuBar menuBar = new JMenuBar();
		menuBar.add( fileMenu );
		setJMenuBar( menuBar );

		pack();
		setVisible( true );
	}
}
