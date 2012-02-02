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

/**
 * Button action class.
 */
abstract class ButtonAction extends GenericAction {
	/**
	 * Creates a new button action.
	 *
	 * @param titleKey Action title key.
	 * @param mnemoKey Action menmonic key.
	 */
	protected ButtonAction( String titleKey, String mnemoKey ) {
		super( titleKey, mnemoKey, null, null, null );
	}

	/**
	 * Creates a new button action with default parameters derived from the title key.
	 *
	 * @param titleKey Action title key.
	 */
	protected ButtonAction( String titleKey ) {
		this( titleKey, titleKey + "-mnemo" );
	}
}
