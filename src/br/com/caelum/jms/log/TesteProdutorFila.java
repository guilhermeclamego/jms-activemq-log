package br.com.caelum.jms.log;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class TesteProdutorFila {
    public static void main(String[] args) throws NamingException, JMSException {

        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");

        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination fila = (Destination) context.lookup("financeiro");
        MessageProducer producer = session.createProducer(fila);

        Message message = session.createTextMessage("<pedido><id>15/id></pedido>");
        producer.send(message);
//        for(int i =100; i < 120; i++){
//            Message message = session.createTextMessage("<pedido><id>"+i+"</id></pedido>");
//            producer.send(message);
//        }


        session.close();
        connection.close();
        context.close();
    }
}
