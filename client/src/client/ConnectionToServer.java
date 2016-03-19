package client.src.client;

import client.src.communications.Message;
import client.src.tree.TaskTree;

import javax.xml.bind.JAXBException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Fadeev on 17.03.2016.
 */
public class ConnectionToServer {
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private Socket socket;
    private boolean isEdit;

    public ConnectionToServer(String hostName, int port) throws IOException {
        this.socket = new Socket(hostName, port);
        outputStream = new DataOutputStream(socket.getOutputStream());
        inputStream = new DataInputStream(socket.getInputStream());
        Thread readFromServer = new Thread(){
            public void run(){
                while(true){
                    try {
                        TaskTree tree = (TaskTree)getObject(TaskTree.class);
                        setIsEdit(true);
                        System.out.println(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        readFromServer.setDaemon(true);
        readFromServer.start();
    }



    public Object getObject(Class aClass) throws IOException{
        String xml = "";

        char startChar = (char)getInputStream().read();
        char currChar = '.';

        try {
            while ((currChar = (char)getInputStream().read()) != '*')

                xml += currChar;

        } catch (IOException e) {
            e.getMessage();
        }


        Marshall marshall = new Marshall();
        Object object = null;
        try {
            object = marshall.unmarshall(xml, aClass);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return object;
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(DataOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public DataInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }
}

