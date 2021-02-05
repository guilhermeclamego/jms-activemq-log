package br.com.caelum.jms.log;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class TesteConsumidorFila {
    public static void main(String[] args) throws NamingException, JMSException {

        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");

        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(true, Session.SESSION_TRANSACTED);

        Destination fila = (Destination) context.lookup("financeiro");
        MessageConsumer consumer = session.createConsumer(fila);


        consumer.setMessageListener(message -> {

            //Pegar apenas a mensagem do envio
            TextMessage textMessage = (TextMessage) message;
            try {
                //message.acknowledge();
                System.out.println(textMessage.getText());
                session.commit();
                //Com rollback irá tentar enviar 6 vezes e se tornará uma DLQ, como está com rollback, logo não irá commitar
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });

        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        context.close();
    }
}
