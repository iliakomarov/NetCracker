package server.src.com.company;


import server.src.server.HttpServer;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {

        boolean isFile = false;

        while (!isFile) {
            System.out.println("Enter path to config file:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String config = reader.readLine();
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(config);
            } catch (FileNotFoundException e) {
                System.out.println("File not found!");
            }
            try {
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                int port = 0;
                try {
                    port = Integer.parseInt(bufferedReader.readLine());
                    isFile = true;
                } catch (NumberFormatException e) {
                    System.out.println("Port number set on default(9999)");
                    port = 9999;// default
                    isFile = true;
                }


                HttpServer server = new HttpServer(port);
                server.start();
            } catch (NullPointerException e) {
                System.out.println("Enter another file!");
            }
        }

    }
}
