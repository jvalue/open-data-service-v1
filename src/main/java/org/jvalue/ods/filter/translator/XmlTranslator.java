package org.jvalue.ods.filter.translator;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


final class XmlTranslator extends Translator<Document> {

	@Override
	public Object translate(Document xmlDocument) {
		Node rootNode = xmlDocument.getFirstChild();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(rootNode.getNodeName(), convertXml(rootNode, false));

		return map;
	}


	private Object convertXml(Node node, boolean isMultiple) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			if (isMultiple || !isMultipleNode(node)) {
				return fillNodeRec(node);
			} else {
				return fillMultipleNodes(node);
			}
		} else if ((node.getNodeType() == Node.ATTRIBUTE_NODE)
				|| (node.getNodeType() == Node.TEXT_NODE)) {
			return node.getNodeValue();
		}
		return null;
	}


	private Object fillMultipleNodes(Node node) {
		Node tmpNode = node;

		List<Object> resultList = new LinkedList<Object>();

		String currentNodeName = node.getNodeName();

		while (tmpNode != null) {
			String tempNodeName = tmpNode.getNodeName();
			if (tempNodeName.equals(currentNodeName)) {
				resultList.add(convertXml(tmpNode, true));
			}
			tmpNode = tmpNode.getNextSibling();
		}

		tmpNode = node.getPreviousSibling();
		while (tmpNode != null) {
			String tempNodeName = tmpNode.getNodeName();
			if (tempNodeName.equals(currentNodeName)) {
				resultList.add(convertXml(tmpNode, true));
			}
			tmpNode = tmpNode.getPreviousSibling();
		}

		return resultList;
	}


	private Object fillNodeRec(Node node) {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (node.hasAttributes()) {
			NamedNodeMap map = node.getAttributes();
			for (int i = 0; i < map.getLength(); i++) {
				Node n = map.item(i);
				Object value = convertXml(n, false);
				resultMap.put(n.getNodeName(), value);
			}
		}

		if (node.hasChildNodes()) {
			NodeList list = node.getChildNodes();

			if ((list.getLength() == 1)
					&& (list.item(0).getNodeType() == Node.TEXT_NODE)) {
				return list.item(0).getNodeValue();
			}

			for (int i = 0; i < list.getLength(); i++) {
				Node n = list.item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Object value = convertXml(n, false);
					resultMap.put(n.getNodeName(), value);
				}

			}
		}

		return resultMap;
	}


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
