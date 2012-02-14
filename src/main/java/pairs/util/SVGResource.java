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

import java.awt.*;
import java.awt.image.*;

import java.io.IOException;

import java.net.URL;

import java.util.MissingResourceException;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.bridge.ViewBox;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;

import org.apache.batik.ext.awt.image.GraphicsUtil;

import org.apache.batik.gvt.GraphicsNode;

import org.apache.batik.util.XMLResourceDescriptor;

import org.w3c.dom.Element;

import org.w3c.dom.svg.SVGDocument;

import static pairs.util.Message._;

/**
 * SVG resource convenience class.
 */
class SVGResource extends AbstractImageResource {
	/**
	 * SVG DOM document.
	 */
	private final SVGDocument svgDocument;

	/**
	 * Root graphics node.
	 */
	private final GraphicsNode rootGraphicsNode;

	/**
	 * Bridge context.
	 */
	private final BridgeContext bridgeContext;

	/**
	 * Creates a new SVG resource.
	 *
	 * @param resourceName Resource name.
	 * @param copyright Copyright.
	 *
	 * @throws MissingResourceException if a resource named resourceName does not exist.
	 * @throws IOException if an error occurs while reading the resource.
	 * @throws NullPointerException if one of the arguments is null.
	 */
	SVGResource( String resourceName, Copyright copyright ) throws IOException {
		super( Type.SVG, copyright );
		URL resourceURL = ClassLoader.getSystemResource( resourceName );
		if ( resourceURL == null ) {
			throw new MissingResourceException( _( "error-loadingresource", resourceName ), ClassLoader.class.getName(), resourceName );
		}
		String parserClassName = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory documentFactory = new SAXSVGDocumentFactory( parserClassName );
		this.svgDocument = (SVGDocument) documentFactory.createDocument( resourceURL.toString() );
		this.bridgeContext = new BridgeContext( new UserAgentAdapter() );
		this.rootGraphicsNode = new GVTBuilder().build( bridgeContext, this.svgDocument );
	}

	/**
	 * Creates a new image.
	 *
	 * @param width Width of the new image.
	 * @param height Height of the new image.
	 * @param renderingHints Rendering hints for the new image.
	 *
	 * @return A new image rendered from SVG is returned.
	 */
	public Image createImage( int width, int height, RenderingHints renderingHints ) {
		BufferedImage result = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
		Graphics2D graphics = GraphicsUtil.createGraphics( result );
		graphics.addRenderingHints( renderingHints );

		/* Render SVG to graphics */
		Rectangle bounds = rootGraphicsNode.getSensitiveBounds().getBounds();
		float scaleW = (float) width / bounds.width;
		float scaleH = (float) height / bounds.height;
		float scale = scaleW < scaleH ? scaleW : scaleH;
		graphics.scale( scale, scale );
		rootGraphicsNode.paint( graphics );

		/* return newly painted image */
		graphics.dispose();
		return result;
	}

	/**
	 * Creates a new image with default rendering hints.
	 *
	 * @param width Width of the new image.
	 * @param height Height of the new image.
	 *
	 * @return A new image rendered from SVG is returned.
	 */
	public Image createImage( int width, int height ) {
		RenderingHints hints = new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		return createImage( width, height, hints );
	}
}
