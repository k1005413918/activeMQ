import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduce_Topic {

    public static final String ACTIVE_URL = "tcp://115.239.255.3:61616";
    public static final String TOPIC_NAME = "topic01";
    public static void main(String[] args) throws JMSException {
        //JMS：创建连接工厂，获取并启动连接，连接创建会话，
        // 会话创建目的地队列，消费生产者，消息，消费生产者操作消息，关闭资源
        
        //1创建工厂 Queue读Q
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory(ACTIVE_URL);
        //2获取连接并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        //3创建session，事务,签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4创建目的地，队列还是topic
        Topic topic = session.createTopic(TOPIC_NAME);
        //5创建消息生产者
        MessageProducer messageProducer = session.createProducer(topic);
        //6通过消息生产者生产3条消息发送到MQ的队列里
        for (int i = 0; i <3; i++) {
            //7创建消息
            TextMessage textMessage =session.createTextMessage("msg--"+i);//理解为字符串
            //8通过消息生产者发送给mq
            messageProducer.send(textMessage);

            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("k1","v1");
            mapMessage.setStringProperty("StringProperty", String.valueOf(i));
            messageProducer.send(mapMessage);
        }
        System.out.println("*****消息发送到MQ完成");

        //9关闭资源
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("*****释放资源完成");

    }
}

