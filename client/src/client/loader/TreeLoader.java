package client.src.client.loader;

import client.src.info.Task;
import client.src.tree.TaskTree;
import client.src.tree.TaskTreeNode;
import org.w3c.dom.*;


import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fadeev on 21.11.2015.
 */
public class TreeLoader {
    //TODO Adding trees
    //TODO Updating trees
    //TODO Loading tree
    //TODO Marshal tree

    public static void addTree(TaskTree treeNode,String name , Document document) {
        try {
            String xmlTree = marshalTree(treeNode, name);
            FileWriter fileWriter = new FileWriter("server\\src\\trees.xml", false);
            Element node =  DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(xmlTree.getBytes()))
                    .getDocumentElement();
            Node n = node.cloneNode(true);
            document.adoptNode(n);
            document.getDocumentElement().appendChild(n);
            document.normalize();
            toXML(document, fileWriter);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static String marshalTree(TaskTree treeNode, String name) throws ParserConfigurationException, IOException, org.xml.sax.SAXException, TransformerConfigurationException, TransformerException {

        server.src.server.Marshall marshall = new server.src.server.Marshall();
        StringWriter stringWriter = new StringWriter();
        marshall.marshall(treeNode, stringWriter, TaskTree.class);

        return stringWriter.toString();
    }

    private static void getAllNodes(Node root, ArrayList<String> nodes) {

        for (int i = 0; i < root.getChildNodes().getLength(); i++) {
            nodes.add(root.getChildNodes().item(i).toString());
            if (root.getChildNodes().item(i).getChildNodes().getLength() != 0) {
                getAllNodes(root.getChildNodes().item(i), nodes);
            }
        }
    }

    public static void toXML(Document doc, Writer writer) throws IOException, org.xml.sax.SAXException, TransformerConfigurationException, TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "publicId");
        DOMImplementation domImpl = doc.getImplementation();
        DocumentType doctype = domImpl.createDocumentType("doctype",
                "PublicIdentifier",
                "treesDTD.dtd");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
    }


    private static Document getDocument(String fileName) throws org.xml.sax.SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setIgnoringElementContentWhitespace(true);
        f.setNamespaceAware(true);
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();
        Document doc = builder.parse(fileName);

        return doc;
    }


    public static String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            System.out.println("nodeToString Transformer Exception");
        }
        return sw.toString();
    }

    public static TaskTree loadTree(String name) throws org.xml.sax.SAXException, IOException, ParserConfigurationException, TransformerException, JAXBException {
        Document document = getDocument("server\\src\\trees.xml");

        NodeList treeList = document.getElementsByTagName("taskTree");
        TaskTreeNode root = null;
        Node node = findTree(treeList, name);
        String xml = nodeToString(node);
        TaskTree tree = null;
        server.src.server.Marshall marshall = new server.src.server.Marshall();
        tree = (TaskTree) marshall.unmarshall(xml, TaskTree.class);

        return tree;
    }

    private static String s = "";

    public static Node findTree(NodeList treeList, String name) {
        for (int i = 0; i < treeList.getLength(); i++) {
            NodeList nodeList = treeList.item(i).getChildNodes();
            for (int j = 0; j < nodeList.getLength(); j++) {
                if (nodeList.item(j).getNodeName().equals("user") && nodeList.item(j).getChildNodes().item(1).getTextContent().equals(name)) {
                    return treeList.item(i);
                }
            }
        }


        return null;
    }

    public static TaskTreeNode treeNode = null;

    public static TaskTreeNode findNode(TaskTreeNode root, Task userObject) {
        if (root.getUserObject() == userObject) return root;
        for (int i = 0; i < root.getChildCount(); i++) {
            if (treeNode != null && treeNode.getUserObject() == userObject) break;
            treeNode = (TaskTreeNode) root.getChildAt(i);

            if (!treeNode.getUserObject().equals(userObject)) {
                treeNode = findNode(treeNode, userObject);
            }
        }
        return treeNode;
    }

    public static void updateTree(TaskTree treeNode, String treeName) throws org.xml.sax.SAXException, IOException, ParserConfigurationException {
        int treeIndex = 0;
        Document document = getDocument("server\\src\\trees.xml");
        NodeList treeList = document.getElementsByTagName("taskTree");
        Node node = findTree(treeList, treeName);

        for (int i = 0; i < treeList.getLength(); i++) {
            if (treeList.item(i) == node) {
                treeIndex = i;
            }
        }

        Element element = (Element) document.getElementsByTagName("taskTree").item(treeIndex);
        element.getParentNode().removeChild(element);
        document.normalize();
        FileWriter fileWriter = new FileWriter("server\\src\\trees.xml", false);
        try {
            toXML(document, fileWriter);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        addTree(treeNode, treeName, document);
    }

}
