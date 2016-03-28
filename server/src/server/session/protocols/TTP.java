package server.src.server.session.protocols;



import server.src.communications.Message;
import server.src.server.Marshall;

import javax.xml.bind.JAXBException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Fadeev on 21.11.2015.
 */
public class TTP {
    public static Message getMessage(DataInputStream inputStream) throws IOException {
        String xml = "";

        char startChar = (char)inputStream.read();
        char currChar = '.';

        Marshall marshall = new Marshall();
        Message message = null;

        if (startChar == 'r') {
            try {
                while ((currChar = (char) inputStream.read()) != '*') {
                    xml += currChar;
                }

            } catch (IOException e) {
                e.getMessage();
            }


            try {
                message = (Message)marshall.unmarshall(xml, Message.class);
            } catch (JAXBException | NullPointerException e) {
                e.printStackTrace();
            }
        }

        return message;
    }

    public static void sendResponse(Message message, DataOutputStream dataOutputStream) throws IOException{
        dataOutputStream.write("m".getBytes());
        Marshall marshall = new Marshall();
        marshall.marshall(message, dataOutputStream, Message.class);
        dataOutputStream.write("*".getBytes());
    }


    public static void sendObject(Object object, DataOutputStream dataOutputStream, Class aClass) throws IOException{
        dataOutputStream.write("m".getBytes());
        Marshall marshall = new Marshall();
        marshall.marshall(object, dataOutputStream, aClass);
        dataOutputStream.write("*".getBytes());
    }


    public static Object getObject(DataInputStream inputStream, Class aClass) throws IOException{
        String xml = "";

        char startChar = (char)inputStream.read();
        char currChar = '.';

        try {
            while ((currChar = (char)inputStream.read()) != '*')

                xml += currChar;

        } catch (IOException e) {
            e.getMessage();
        }

        Marshall marshall = new Marshall();
        Object object = null;
        try {
            object = marshall.unmarshall(xml, aClass);
        } catch (JAXBException e) {
            System.out.println(e.getMessage());
        }

        return object;
    }
}
