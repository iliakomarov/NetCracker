package server.src.server;


import server.src.server.session.Session;
import server.src.server.session.protocols.TTP;
import server.src.tree.TaskTree;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Fadeev on 12.11.2015.
 */
public class HttpServer {

    private static Logger log = Logger.getLogger(HttpServer.class.getName());
    private static ArrayList<Socket> clients = new ArrayList<>();
    public static void main(String[] args) {

        int portNumber = 9999;
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            log.info("Server starting on port " + portNumber);
            while (true) {
                Socket socket = serverSocket.accept();
                clients.add(socket);
                new Thread(new Session(socket)).start();
                log.info("New client accepted!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendToAll(Object message) throws IOException {
        if (message.getClass() == TaskTree.class) {
            for (int i = 0; i < clients.size(); i++) {
                if (i % 2 != 0 && clients.get(i) != null) {
                    try{
                        TTP.sendObject(message, new DataOutputStream(clients.get(i).getOutputStream()), TaskTree.class);
                    }
                    catch (SocketException e){
                        clients.set(i, null);
                    }
                }
            }
        }
    }

    public void start(){main(new String[]{});
    }
}
