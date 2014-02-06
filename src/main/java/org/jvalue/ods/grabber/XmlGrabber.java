/*
 * 
 */
package org.jvalue.ods.grabber;
import org.jvalue.ods.data.GenericValue;
import org.jvalue.ods.data.ListValue;
import org.jvalue.ods.data.MapValue;
import org.jvalue.ods.data.StringValue;
import org.jvalue.ods.logger.Logging;

import java.io.File;

import org.w3c.dom.*;

import javax.xml.parsers.*;

/**
 * The Class XmlGrabber.
 */
public class XmlGrabber {

	
	/**
	 * Grab.
	 *
	 * @param source the source
	 * @return the generic value
	 */
	public GenericValue grab(String source) {
		if (source == null)
			throw new IllegalArgumentException("source is null");	
		
		try {					 
			File xmlFile = new File(source);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			Node rootNode = doc.getFirstChild();
			
			GenericValue gv = new MapValue();
			((MapValue)gv).getMap().put(rootNode.getNodeName(), convertXml(rootNode, false));
						
			
			return gv;
		}
		catch(Exception ex)
		{
			Logging.info(this.getClass(), ex.getMessage());
			return null;
		}
		
	}

	/**
	 * Convert xml.
	 *
	 * @param node the node
	 * @param isMultiple the is multiple
	 * @return the generic value
	 */
	private GenericValue convertXml(Node node, boolean isMultiple) {
		GenericValue gv = null;		
		
		if (node.getNodeType() == Node.ELEMENT_NODE)
		{
			if (isMultiple || !isMultipleNode(node))
			{
				gv = new MapValue();				
				fillNodeRec(node, (MapValue) gv);				
			}
			else
			{
				gv = new ListValue();
				fillMultipleNodes(node, (ListValue) gv);
			}
		}
		else if ((node.getNodeType() == Node.ATTRIBUTE_NODE) || (node.getNodeType() == Node.TEXT_NODE))
		{			
			String s = node.getNodeValue();
			gv = new StringValue(s);			
		}
		
		return gv;
	}

	/**
	 * Fill multiple nodes.
	 *
	 * @param node the node
	 * @param lv the lv
	 */
	private void fillMultipleNodes(Node node, ListValue lv) {
		Node tmpNode = node;
		
		String currentNodeName = node.getNodeName();
		
		while(tmpNode != null)
		{
			String tempNodeName = tmpNode.getNodeName();
			if (tempNodeName.equals(currentNodeName))
			{
				lv.getList().add(convertXml(tmpNode, true));
			}
			tmpNode = tmpNode.getNextSibling();
		}
		
		tmpNode = node.getPreviousSibling();
		while(tmpNode != null)
		{
			String tempNodeName = tmpNode.getNodeName();
			if (tempNodeName.equals(currentNodeName))
			{
				lv.getList().add(convertXml(tmpNode, true));
			}
			tmpNode = tmpNode.getPreviousSibling();
		}
		
	}

	/**
	 * Fill node rec.
	 *
	 * @param node the node
	 * @param mv the mv
	 */
	private void fillNodeRec(Node node, MapValue mv) {
		if (node.hasAttributes()) {
			NamedNodeMap map = node.getAttributes();
			for (int i = 0; i < map.getLength(); i++)
			{
				Node n = map.item(i);
				GenericValue gv = convertXml(n, false);
				mv.getMap().put(n.getNodeName(), gv);
			}
		}
		
		if (node.hasChildNodes())
		{
			NodeList list = node.getChildNodes();		
			
			for (int i = 0; i < list.getLength(); i++)
			{
				Node n = list.item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					GenericValue gv = convertXml(n, false);
					mv.getMap().put(n.getNodeName(), gv);
				}
			}
		}
	}

	/**
	 * Checks if is multiple node.
	 *
	 * @param node the node
	 * @return true, if is multiple node
	 */
	private boolean isMultipleNode(Node node)
	{
		String currentNodeName = node.getNodeName();
		
		Node tmpNode = node;
		while (tmpNode.getNextSibling() != null)
		{
			String nextNodeName = tmpNode.getNextSibling().getNodeName();
			if (nextNodeName.equals(currentNodeName))
			{
				return true;
			}
			tmpNode = tmpNode.getNextSibling();
		}
		
		tmpNode = node;
		while (tmpNode.getPreviousSibling() != null)
		{
			String previousNodeName = tmpNode.getPreviousSibling().getNodeName();
			if (previousNodeName.equals(currentNodeName))
			{
				return true;
			}
			tmpNode = tmpNode.getPreviousSibling();
		}
		return false;
	}
}
