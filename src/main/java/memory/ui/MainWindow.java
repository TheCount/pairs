package memory.ui;

import java.awt.*;
import java.awt.event.*;

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

		/* Menu */
		JMenu fileMenu = new JMenu( Message.get( "menu-file" ).toString() );
		fileMenu.add( new AbstractAction( Message.get( "menu-exit" ).toString() ) {
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
