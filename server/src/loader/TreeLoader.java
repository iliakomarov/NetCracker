package server.src.loader;

import server.src.server.Marshall;
import server.src.tree.TreeNode;
import org.w3c.dom.*;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
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

    public static void addTree(TreeNode treeNode, String name){
        try {
           Document document =  marshalTree(treeNode, name);
            FileWriter fileWriter = new FileWriter("server\\src\\trees.xml", false);

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

    public static Document marshalTree(TreeNode treeNode, String name) throws ParserConfigurationException, IOException, org.xml.sax.SAXException, TransformerConfigurationException, TransformerException{



        List<StringBuffer> xmlNodes = new ArrayList<>();
        Marshall marshall = new Marshall();
        StringWriter stringWriter = new StringWriter();
        String currentXmlNode = "";
        TreeNode currParent = null;

        Document doc = getDocument("server\\src\\trees.xml");

        Node trees = doc.getElementsByTagName("trees").item(0);

        ArrayList<TreeNode> treeNodes = new ArrayList<>();
        treeNodes.add(treeNode);
        getAllNodes(treeNode, treeNodes);

        for (int i = 0; i < treeNodes.size(); i++) {
            if (treeNodes.get(i).getParent() != null) {
                currParent = (TreeNode) treeNodes.get(i).getParent();
                treeNodes.get(i).setCurrParent((String) currParent.getUserObject());
            }
            else treeNodes.get(i).setCurrParent("null");
            marshall.marshall(treeNodes.get(i), stringWriter, TreeNode.class);
            StringBuffer stringBuffer = stringWriter.getBuffer();
            xmlNodes.add(stringBuffer);
            stringWriter = new StringWriter();
        }

        Element tree = doc.createElement("tree");
        tree.setAttribute("name", name);


        for (int i = 0; i < xmlNodes.size(); i++) {
            int start = 0;
            int end = 0;
            Element treeNodeElement = doc.createElement("treeNode");
            Element userObjectElement = doc.createElement("userObject");
            Element allowsChildElement = doc.createElement("allowsChild");
            Element parentElement = doc.createElement("parent");

            start = xmlNodes.get(i).indexOf("userObject");
            end = xmlNodes.get(i).indexOf("</userObject");

            userObjectElement.setTextContent(xmlNodes.get(i).substring(start + 130, end));


            start = xmlNodes.get(i).indexOf("allowsChild");
            end = xmlNodes.get(i).indexOf("</allowsChild");


            allowsChildElement.setTextContent(xmlNodes.get(i).substring(start + 15, end));

            start = xmlNodes.get(i).indexOf("currParent");
            end = xmlNodes.get(i).indexOf("</currParent");


            parentElement.setTextContent(xmlNodes.get(i).substring(start + 11, end));

            treeNodeElement.appendChild(userObjectElement);
            treeNodeElement.appendChild(allowsChildElement);
            treeNodeElement.appendChild(parentElement);

            tree.appendChild(treeNodeElement);
        }

        trees.appendChild(tree);

        return doc;

    }

    private static void getAllNodes(TreeNode root, ArrayList<TreeNode> nodes){

        for (int i = 0; i < root.getChildCount(); i++) {
           nodes.add((TreeNode)root.getChildAt(i));
            if (root.getChildAt(i).getChildCount() != 0){
                getAllNodes((TreeNode)root.getChildAt(i), nodes);
            }
        }
    }

    public static void toXML(Document doc, Writer writer) throws IOException, org.xml.sax.SAXException, TransformerConfigurationException, TransformerException{
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

    private static Document getDocument(String fileName) throws org.xml.sax.SAXException, IOException, ParserConfigurationException{
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setIgnoringElementContentWhitespace(true);
        f.setNamespaceAware(true);
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();
        Document doc = builder.parse(fileName);

        return doc;
    }

    public static TreeNode loadTree(String name) throws org.xml.sax.SAXException, IOException, ParserConfigurationException{
        Document document = getDocument("server\\src\\trees.xml");

        NodeList treeList = document.getElementsByTagName("tree");
        String s = "";
        Node currTree = null;
        TreeNode root = null;
        currTree = findTree(document, name);

        ArrayList<TreeNode> tree = new ArrayList<>();

        NodeList treeNodesXML = currTree.getChildNodes();
        NodeList treeNodeData = treeNodesXML.item(0).getChildNodes();

        root = new TreeNode(treeNodeData.item(0).getTextContent(), Boolean.valueOf(treeNodeData.item(1).getTextContent()));
        root.setParent(null);
        tree.add(root);

        for (int i = 1; i < treeNodesXML.getLength(); i++) {
            treeNodeData = treeNodesXML.item(i).getChildNodes();
            TreeNode treeNode = new TreeNode(treeNodeData.item(0).getTextContent(), Boolean.valueOf(treeNodeData.item(1).getTextContent()));
            for (int j = 0; j < tree.size(); j++) {
                if (tree.get(j).getUserObject().equals(treeNodeData.item(2).getTextContent())){
                    tree.get(j).add(treeNode);
                    tree.add(treeNode);
                    break;
                }
            }
        }


        return tree.get(0);
    }


    public static Node findTree(Document document, String name){
        NodeList treeList = document.getElementsByTagName("tree");
        String s = "";
        Node currTree = null;
        TreeNode root = null;
        for (int i = 0; i < treeList.getLength(); i++) {
            NamedNodeMap attributes = treeList.item(i).getAttributes();

            if (attributes.getNamedItem("name").getNodeValue().equals(name))
            {
                currTree = treeList.item(i);
            }
        }

        return currTree;
    }

    public static TreeNode treeNode = null;
    public static TreeNode findNode(TreeNode root, String userObject){
        if (root.getUserObject().equals(userObject)) return root;
        for (int i = 0; i < root.getChildCount(); i++) {
            if (treeNode != null && treeNode.getUserObject().equals(userObject)) break;
            treeNode = (TreeNode) root.getChildAt(i);

            if (!treeNode.getUserObject().equals(userObject)) {
                treeNode = findNode(treeNode, userObject);
            }
        }
        return treeNode;
    }

    public static void updateTree(TreeNode treeNode, String treeName)throws org.xml.sax.SAXException, IOException, ParserConfigurationException{
        Document document = getDocument("server\\src\\trees.xml");
        Element root = document.getDocumentElement();
        root.removeChild(findTree(document, treeName));
        FileWriter fileWriter = new FileWriter("server\\src\\trees.xml", false);
        try {
            toXML(document, fileWriter);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        addTree(treeNode, treeName);
    }

}
