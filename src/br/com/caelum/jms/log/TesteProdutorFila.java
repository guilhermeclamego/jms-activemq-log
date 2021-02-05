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

        Destination fila = (Destination) context.lookup("LOG");
        MessageProducer producer = session.createProducer(fila);

        Message message = session.createTextMessage("ERRO | TESTE LOG");  //colocar prioridade 9
        //Message message = session.createTextMessage("WARN | TESTE LOG");  //colocar prioridade 3
        //Message message = session.createTextMessage("DEBUG | TESTE LOG"); //colocar prioridade 0
        //Message message = session.createTextMessage("ALERT | TESTE LOG"); //colocar prioridade 7
        /*Parâmetors:
            1 - message
            2 - DeliveryMode -> no caso colocado para não persistir, se cair, não irá recuperar
            3 - Prioridade da mensagem, de 0 à 9
            4 - Colocado 5 segundos, após isso a mensagem será apagada, caso não for processada
            5 - Teste, deixar 80 segundos e enviar as 4 mensagens acima, uma de cada, após isso
                executar o TesteConsumidorFila, irá retornar na ordem de prioridade. Porém, é necessário
                efetuar a configuração no activemq.xml na conf do activemq no PolicyEntries
                <policyEntry queue=">" prioritizedMessages="true"/>
         */
        producer.send(message, DeliveryMode.NON_PERSISTENT, 7, 80000 );

//        for(int i =100; i < 120; i++){
//            Message message = session.createTextMessage("<pedido><id>"+i+"</id></pedido>");
//            producer.send(message);
//        }


        session.close();
        connection.close();
        context.close();
    }
}
