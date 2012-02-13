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

package pairs.util;

/**
 * An unordered pair of non-null elements.
 *
 * @param E Type of the elements in the pair.
 */
public class UnorderedPair<E> {
	/**
	 * First element.
	 */
	private final E first;

	/**
	 * Second element.
	 */
	private final E second;

	/**
	 * Creates a new unordered pair.
	 *
	 * @param first First element.
	 * @param second Second element.
	 *
	 * @throws NullPointerException if one of the elements is null.
	 */
	public UnorderedPair( E first, E second ) {
		if ( ( first == null ) || ( second == null ) ) {
			throw new NullPointerException();
		}
		this.first = first;
		this.second = second;
	}

	/**
	 * Gets the first element of this pair.
	 *
	 * @return The first element of this pair is returned.
	 */
	public E getFirst() {
		return first;
	}

	/**
	 * Gets the second element of this pair.
	 *
	 * @return The second element of this pair is returned.
	 */
	public E getSecond() {
		return second;
	}

	/**
	 * Returns the hash code for this unordered pair.
	 * The hash code is independent of the order of the constituents of this pair.
	 *
	 * @return The hash code of this pair is returned.
	 */
	public @Override int hashCode() {
		return first.hashCode() ^ second.hashCode();
	}

	/**
	 * Compares this unordered pair for equality with another.
	 * Two unordered pairs {a,b} and {c,d} are equals if either (a,b)==(c,d) or (a,b)==(d,c).
	 *
	 * @param o Some object.
	 *
	 * @return If o is a non-null unordered pair equal to this pair, true is returned.
	 * 	Otherwise, false is returned.
	 */
	public @Override boolean equals( Object o ) {
		try {
			UnorderedPair<E> p = (UnorderedPair<E>) o;
			return ( ( first.equals( p.first ) && second.equals( p.second ) )
					|| ( first.equals( p.second ) && second.equals( p.first ) ) );
		} catch ( RuntimeException e ) {
			return false;
		}
	}

	/**
	 * Renders this unordered pair as a string.
	 *
	 * @return A string rendition of this unordered pair is returned.
	 */
	public @Override String toString() {
		return "{" + first.toString() + "," + second.toString() + "}";
	}
}
