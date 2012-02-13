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

package pairs.data;

import pairs.util.UnorderedPair;

/**
 * A card pair.
 */
public class CardPair extends UnorderedPair<Card> {
	/**
	 * Creates a new card pair.
	 *
	 * @param first First card.
	 * @param second Second card.
	 *
	 * @throws NullPointerException if one of the arguments is null.
	 */
	public CardPair( Card first, Card second ) {
		super( first, second );
	}
}
