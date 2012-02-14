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
import java.awt.image.*;

import java.util.prefs.Preferences;

import javax.swing.*;

import org.apache.log4j.Logger;

import pairs.data.CardPackage;

import pairs.util.ImageResource;
import pairs.util.ImageResourceLoader;

import static pairs.util.Message._;

/**
 * Main window.
 */
public class MainWindow extends JFrame implements ComponentListener, WindowStateListener {
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = Logger.getLogger( MainWindow.class );

	/**
	 * Minimum window width.
	 */
	private static final int MIN_WIDTH = 600;

	/**
	 * Minimum window height.
	 */
	private static final int MIN_HEIGHT = 400;

	/**
	 * Preferences for this class.
	 */
	private static final Preferences preferences = Preferences.userNodeForPackage( MainWindow.class );

	/**
	 * Window width preference key.
	 */
	private static final String PREF_WIDTH = "mainWindowWidth";

	/**
	 * Window height preference key.
	 */
	private static final String PREF_HEIGHT = "mainWindowHeight";

	/**
	 * Window state preference key.
	 */
	private static final String PREF_STATE = "mainWindowState";

	/**
	 * Status bar.
	 */
	private final StatusBar statusBar;

	/**
	 * Current playfield.
	 */
	private Playfield playfield;

	/**
	 * Game timer.
	 */
	private class GameTimer extends Timer {
		/**
		 * Update interval (ms).
		 */
		private static final int UPDATE_INTERVAL = 1000;

		/**
		 * Start time.
		 */
		private long startTime;

		/**
		 * Creates a new game timer.
		 */
		GameTimer() {
			super( UPDATE_INTERVAL, null );
			addActionListener( new ActionListener() {
				public void actionPerformed( ActionEvent event ) {
					statusBar.setTime( (int) ( ( System.currentTimeMillis() - getStartTime() ) / 1000 ) );
				}
			} );

		}

		/**
		 * Gets the start time.
		 *
		 * @return The start time is returned.
		 */
		private long getStartTime() {
			return startTime;
		}

		public @Override void start() {
			startTime = System.currentTimeMillis();
			super.start();
		}

		public @Override void restart() {
			startTime = System.currentTimeMillis();
			super.restart();
		}
	}

	private final GameTimer gameTimer;

	/**
	 * Displays main window.
	 */
	public MainWindow() {
		super( _( "label-concentration" ) );

		/* Program icon */
		try {
			ImageResource resource = ImageResourceLoader.load( "program-icon" );
			setIconImage( resource.createImage( 64, 64 ) );
		} catch ( Exception e ) {
			logger.error( _( "error-icon" ), e );
		}

		/* Default close operation */
		setDefaultCloseOperation( EXIT_ON_CLOSE );

		/* Menu */
		JMenu fileMenu = new JMenu( new MenuAction( "menu-file" ) );
		fileMenu.add( new MenuItemAction( "menu-newgame" ) {
			public void actionPerformed( ActionEvent event ) {
				new NewGame( MainWindow.this );
			}
		} );
		fileMenu.addSeparator();
		fileMenu.add( new MenuItemAction( "menu-exit" ) {
			public void actionPerformed( ActionEvent e ) {
				MainWindow.this.dispose();
			}
		} );
		JMenu helpMenu = new JMenu( new MenuAction( "menu-help" ) );
		helpMenu.add( new MenuItemAction( "menu-about" ) {
			public void actionPerformed( ActionEvent e ) {
				new AboutBox( MainWindow.this );
			}
		} );
		JMenuBar menuBar = new JMenuBar();
		menuBar.add( fileMenu );
		menuBar.add( helpMenu );
		setJMenuBar( menuBar );

		/* Layout */
		setLayout( new BorderLayout() );
		playfield = null;
		statusBar = new StatusBar();
		add( statusBar, BorderLayout.SOUTH );

		/* Other stuff */
		gameTimer = new GameTimer();

		/* Window sizing */
		addComponentListener( this );
		addWindowStateListener( this );
		setSize( preferences.getInt( PREF_WIDTH, MIN_WIDTH ), preferences.getInt( PREF_HEIGHT, MIN_HEIGHT ) );
		setExtendedState( preferences.getInt( PREF_STATE, NORMAL ) );
		if ( getExtendedState() == NORMAL ) {
			setLocationRelativeTo( null );
		}
		validate();
		setVisible( true );
	}

	/**
	 * Resets the playfield with the specified arguments.
	 *
	 * @param cardPackage Card package to use in new playfield.
	 * @param sizeHint Suggested playfield size.
	 */
	public void resetPlayfield( CardPackage cardPackage, int sizeHint ) {
		statusBar.setStatusMessage( "" );
		if ( playfield != null ) {
			remove( playfield );
		}
		playfield = new Playfield( this, cardPackage, sizeHint );
		add( playfield, BorderLayout.CENTER );
		validate();
		gameTimer.restart();
	}

	/**
	 * Triggered if the game is won.
	 */
	public void gameWon() {
		gameTimer.stop();
		statusBar.setStatusMessage( _( "label-congrats" ) );
	}

	public void componentHidden( ComponentEvent e ) {
	}

	public void componentMoved( ComponentEvent e ) {
	}

	public void componentResized( ComponentEvent e ) {
		int width = getWidth();
		int height = getHeight();
		if ( ( width < MIN_WIDTH ) || ( height < MIN_HEIGHT ) ) {
			width = Math.max( width, MIN_WIDTH );
			height = Math.max( height, MIN_HEIGHT );
			setSize( width, height );
		}
		preferences.putInt( PREF_WIDTH, width );
		preferences.putInt( PREF_HEIGHT, height );
	}

	public void componentShown( ComponentEvent e ) {
	}

	public void windowStateChanged( WindowEvent e ) {
		preferences.putInt( PREF_STATE, e.getNewState() );
	}
}
