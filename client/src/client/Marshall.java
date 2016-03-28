package client.src.client;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.Writer;
import java.rmi.UnmarshalException;

/**
 * Created by Fadeev on 12.11.2015.
 */
public class Marshall {

    public Object unmarshall(String xmlString, Class aClass) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(aClass);
        javax.xml.bind.Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        StringReader reader = new StringReader(xmlString);
        try {
            Object request = unmarshaller.unmarshal(reader);
            return request;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

    }

    public void marshall(Object object, OutputStream outputStream, Class aClass){
        JAXBContext jaxbContext = null;
        Marshaller marshaller = null;

        try {
            jaxbContext = JAXBContext.newInstance(aClass);
            marshaller = jaxbContext.createMarshaller();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        try {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        } catch (PropertyException e) {
            e.printStackTrace();
        }

        try {
            marshaller.marshal(object, outputStream);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


    public void marshall(Object object, Writer writer, Class aClass){
        JAXBContext jaxbContext = null;
        Marshaller marshaller = null;

        try {
            jaxbContext = JAXBContext.newInstance(aClass);
            marshaller = jaxbContext.createMarshaller();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        try {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        } catch (PropertyException e) {
            e.printStackTrace();
        }

        try {
            marshaller.marshal(object, writer);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
