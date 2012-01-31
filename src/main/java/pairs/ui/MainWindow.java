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

package pairs.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import static pairs.util.Message._;

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
		fileMenu.add( new MenuItemAction( "menu-exit" ) {
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
