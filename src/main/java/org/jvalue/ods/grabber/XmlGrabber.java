/*
 * 
 */
package org.jvalue.ods.grabber;

import java.io.File;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jvalue.ods.data.generic.GenericValue;
import org.jvalue.ods.data.generic.ListValue;
import org.jvalue.ods.data.generic.MapValue;
import org.jvalue.ods.data.generic.StringValue;
import org.jvalue.ods.logger.Logging;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * The Class XmlGrabber.
 */
public class XmlGrabber {

	/**
	 * Grab.
	 * 
	 * @param source
	 *            the source
	 * @return the generic value
	 */
	public GenericValue grab(String source) {
		if (source == null)
			throw new IllegalArgumentException("source is null");

		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document doc = null;
			if (!source.startsWith("http")) {
				URL sourceUrl = getClass().getResource(source);
				File xmlFile = new File(sourceUrl.toURI());
				doc = dBuilder.parse(xmlFile);
			} else {
				HttpReader reader = new HttpReader(source);
				String data = reader.read("UTF-8");
				doc = dBuilder.parse(new InputSource(new StringReader(data)));
			}

			doc.getDocumentElement().normalize();
			Node rootNode = doc.getFirstChild();

			GenericValue gv = new MapValue();
			((MapValue) gv).getMap().put(rootNode.getNodeName(),
					convertXml(rootNode, false));

			return gv;
		} catch (Exception ex) {
			Logging.info(this.getClass(), ex.getMessage());
			return null;
		}

	}

	/**
	 * Convert xml.
	 * 
	 * @param node
	 *            the node
	 * @param isMultiple
	 *            the is multiple
	 * @return the generic value
	 */
	private GenericValue convertXml(Node node, boolean isMultiple) {
		GenericValue gv = null;

		if (node.getNodeType() == Node.ELEMENT_NODE) {
			if (isMultiple || !isMultipleNode(node)) {
				gv = fillNodeRec(node);
			} else {
				gv = new ListValue();
				gv = fillMultipleNodes(node);
			}
		} else if ((node.getNodeType() == Node.ATTRIBUTE_NODE)
				|| (node.getNodeType() == Node.TEXT_NODE)) {
			String s = node.getNodeValue();
			gv = new StringValue(s);
		}

		return gv;
	}

	/**
	 * Fill multiple nodes.
	 * 
	 * @param node
	 *            the node
	 * @return the generic value
	 */
	private GenericValue fillMultipleNodes(Node node) {
		Node tmpNode = node;

		ListValue lv = new ListValue();

		String currentNodeName = node.getNodeName();

		while (tmpNode != null) {
			String tempNodeName = tmpNode.getNodeName();
			if (tempNodeName.equals(currentNodeName)) {
				lv.getList().add(convertXml(tmpNode, true));
			}
			tmpNode = tmpNode.getNextSibling();
		}

		tmpNode = node.getPreviousSibling();
		while (tmpNode != null) {
			String tempNodeName = tmpNode.getNodeName();
			if (tempNodeName.equals(currentNodeName)) {
				lv.getList().add(convertXml(tmpNode, true));
			}
			tmpNode = tmpNode.getPreviousSibling();
		}

		return lv;
	}

	/**
	 * Fill node rec.
	 * 
	 * @param node
	 *            the node
	 * @return the generic value
	 */
	private GenericValue fillNodeRec(Node node) {

		GenericValue mv = new MapValue();

		if (node.hasAttributes()) {
			NamedNodeMap map = node.getAttributes();
			for (int i = 0; i < map.getLength(); i++) {
				Node n = map.item(i);
				GenericValue gv = convertXml(n, false);
				((MapValue) mv).getMap().put(n.getNodeName(), gv);
			}
		}

		if (node.hasChildNodes()) {
			NodeList list = node.getChildNodes();

			if ((list.getLength() == 1)
					&& (list.item(0).getNodeType() == Node.TEXT_NODE)) {
				mv = new StringValue(list.item(0).getNodeValue());
				return mv;
			}

			for (int i = 0; i < list.getLength(); i++) {
				Node n = list.item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					GenericValue gv = convertXml(n, false);
					((MapValue) mv).getMap().put(n.getNodeName(), gv);
				}

			}
		}

		return mv;
	}

	/**
	 * Checks if is multiple node.
	 * 
	 * @param node
	 *            the node
	 * @return true, if is multiple node
	 */
	private boolean isMultipleNode(Node node) {
		String currentNodeName = node.getNodeName();

		Node tmpNode = node;
		while (tmpNode.getNextSibling() != null) {
			String nextNodeName = tmpNode.getNextSibling().getNodeName();
			if (nextNodeName.equals(currentNodeName)) {
				return true;
			}
			tmpNode = tmpNode.getNextSibling();
		}

		tmpNode = node;
		while (tmpNode.getPreviousSibling() != null) {
			String previousNodeName = tmpNode.getPreviousSibling()
					.getNodeName();
			if (previousNodeName.equals(currentNodeName)) {
				return true;
			}
			tmpNode = tmpNode.getPreviousSibling();
		}
		return false;
	}
}
