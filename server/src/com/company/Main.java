package server.src.com.company;


import server.src.server.HttpServer;

public class Main {

    public static void main(String[] args) {

       /* try {

            TaskTreeNode treeNode = new TaskTreeNode();
            treeNode.add(new TaskTreeNode());
            treeNode.add(new TaskTreeNode());

            TreeLoader.updateTree(treeNode, "Ilya");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }*/

        HttpServer server = new HttpServer();
        server.start();



    }
}
