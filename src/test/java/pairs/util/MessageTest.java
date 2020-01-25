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

import org.apache.logging.log4j.Level;

import org.apache.logging.log4j.core.config.Configurator;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Message test.
 */
public class MessageTest {
	/**
	 * Global test setup.
	 */
	@BeforeClass public static void setup() {
		Configurator.setRootLevel( Level.OFF ); // silence the logger during the tests
	}

	/**
	 * Null message test.
	 */
	@Test( expected = NullPointerException.class ) public void nullTest() {
		Message.get( null );
	}

	/**
	 * Non-existent key test.
	 */
	@Test public void nonexistentKeyTest() {
		assertEquals( "", Message.__( "foo" ) );
	}

	/**
	 * No-param test.
	 */
	@Test public void noParamTest() {
		assertEquals( "Test", Message.__( "test0" ) );
		assertEquals( "Test", Message.__( "test0", "foo" ) );
	}

	/**
	 * One-param test.
	 */
	@Test public void oneParamTest() {
		assertEquals( "Test{0}", Message.__( "test1" ) );
		assertEquals( "Testfoo", Message.__( "test1", "foo" ) );
		assertEquals( "Testfoo", Message.__( "test1", "foo", "bar" ) );
	}

	/**
	 * Two-param test.
	 */
	@Test public void twoParamTest() {
		assertEquals( "Test{0}{1}", Message.__( "test2" ) );
		assertEquals( "Test{1}{0}", Message.__( "test3" ) );
		assertEquals( "Testfoobar", Message.__( "test2", "foo", "bar" ) );
		assertEquals( "Testbarfoo", Message.__( "test3", "foo", "bar" ) );
	}
}
