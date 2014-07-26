package org.jvalue.ods.translator;

import org.jvalue.ods.data.generic.BaseObject;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.filter.Filter;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


final class XmlTranslator implements Filter<Document, GenericEntity> {

	@Override
	public GenericEntity filter(Document xmlDocument) {
		Node rootNode = xmlDocument.getFirstChild();

		GenericEntity gv = new MapObject();
		((MapObject) gv).getMap().put(rootNode.getNodeName(),
				convertXml(rootNode, false));

		return gv;
	}


	private GenericEntity convertXml(Node node, boolean isMultiple) {
		GenericEntity gv = null;

		if (node.getNodeType() == Node.ELEMENT_NODE) {
			if (isMultiple || !isMultipleNode(node)) {
				gv = fillNodeRec(node);
			} else {
				gv = new ListObject();
				gv = fillMultipleNodes(node);
			}
		} else if ((node.getNodeType() == Node.ATTRIBUTE_NODE)
				|| (node.getNodeType() == Node.TEXT_NODE)) {
			String s = node.getNodeValue();
			gv = new BaseObject(s);
		}

		return gv;
	}


	private GenericEntity fillMultipleNodes(Node node) {
		Node tmpNode = node;

		ListObject lv = new ListObject();

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


	private GenericEntity fillNodeRec(Node node) {

		GenericEntity mv = new MapObject();

		if (node.hasAttributes()) {
			NamedNodeMap map = node.getAttributes();
			for (int i = 0; i < map.getLength(); i++) {
				Node n = map.item(i);
				GenericEntity gv = convertXml(n, false);
				((MapObject) mv).getMap().put(n.getNodeName(), gv);
			}
		}

		if (node.hasChildNodes()) {
			NodeList list = node.getChildNodes();

			if ((list.getLength() == 1)
					&& (list.item(0).getNodeType() == Node.TEXT_NODE)) {
				mv = new BaseObject(list.item(0).getNodeValue());
				return mv;
			}

			for (int i = 0; i < list.getLength(); i++) {
				Node n = list.item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					GenericEntity gv = convertXml(n, false);
					((MapObject) mv).getMap().put(n.getNodeName(), gv);
				}

			}
		}

		return mv;
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
