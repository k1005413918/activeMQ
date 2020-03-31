import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumer {

    public static final String ACTIVE_URL = "tcp://115.239.255.3:61616";
    public static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException, IOException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVE_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session =connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);
        MessageConsumer messageConsumer = session.createConsumer(queue);

//        while (true){
////            //receive,同步阻塞方式，里面是毫秒数，没有的话一直阻塞，消费者一直存在，前台能看到消费者数量
////            TextMessage textMessage= (TextMessage) messageConsumer.receive(4000);
////            if (textMessage != null) {
////                System.out.println("****消费者接受到的消息："+textMessage.getText());
////            }else {
////                break;
////            }
////        }

        //通过监听的方式来获取消息
        messageConsumer.setMessageListener(new MessageListener() {

           @Override
           public void onMessage(Message message) {
               if (message != null && message instanceof TextMessage) { //对象instanceof类,对象是后面类或者其子类的实例 返回true
                    TextMessage textMessage = (TextMessage) message;
                   try {
                       System.out.println("****消费者接受到的消息："+textMessage.getText());
                   } catch (JMSException e) {
                       e.printStackTrace();
                   }
               }
           }
       });
        //监听需要时间，下面这个输入任何按键后继续往下执行
        System.in.read();

        messageConsumer.close();
        session.close();
        connection.close();
        System.out.println("*****释放资源完成");

    /*
    1.先生产，启动1号消费者，再启动2号消费者，2号不能消费吗
    2.启动12号消费者后，生产6条消息，024 135，各消费了3条
    注：在run-edit configurations-allow parrallel run勾选，可以run多个main方法
     */
    }
}
