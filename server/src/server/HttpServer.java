package server.src.server;


import server.src.server.session.Session;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

/**
 * Created by Fadeev on 12.11.2015.
 */
public class HttpServer {

    private static Logger log = Logger.getLogger(HttpServer.class.getName());
    public static void main(String[] args) {

        int portNumber = 9999;
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            log.info("Server starting on port " + portNumber);
            while (true) {
                new Thread(new Session(serverSocket.accept())).start();
                log.info("New client accepted!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        main(new String[]{});
    }
}
