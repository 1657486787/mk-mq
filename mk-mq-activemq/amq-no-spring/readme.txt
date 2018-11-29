使用原生ActiveMQ

1.ActiveMq生产者:
  1.1 引入pom
         <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-all</artifactId>
            <version>5.9.0</version>
        </dependency>
  1.2 创建连接工厂，创建连接，启动连接，创建会话，创建消息目的地，创建生产者，组装好消息，然后发送
  1.3 消息有两种模式，点对点p2p和发布订阅(广播)pub/sub模式，在创建消息目的地时指定
  点对点p2p模式：一个消息只有一个消费者，默认会持久化，就是说重启activeMq，消息会自动保存
  发布订阅(广播)pub/sub模式：一个消息有多个消费者消费，默认不会持久化，就是说重启activeMq，消息会丢失，而且如果是生产者先发送消息之后，消费者才启动，那么消费者是获取不到消息的

        public class JSMProducer {

            public static String ACTIVEMQ_BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL;/*默认连接用户名*/
            public static String ACTIVEMQ_USERNAME = ActiveMQConnection.DEFAULT_USER;/*默认连接用户名*/
            public static String ACTIVEMQ_PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD ;/*默认连接密码*/
            public static String HELLO_ACTIVEMQ_QUEUE = AMQConstant.HELLO_ACTIVEMQ_QUEUE;//消息模式：点对点模式
            public static String HELLO_ACTIVEMQ_TOPIC = AMQConstant.HELLO_ACTIVEMQ_TOPIC;//消息模式：发布订阅(广播)pub/sub模式

            public static void main(String[] args) {
                Connection connection = null;
                try {
                    //连接工厂
                    ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_USERNAME,ACTIVEMQ_PASSWORD,ACTIVEMQ_BROKERURL);
                    //创建连接
                    connection = connectionFactory.createConnection();
                    //启动连接
                    connection.start();
                    //会话，第一个参数表示是否使用事务，第二次参数表示是否自动确认
                    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                    //消息的目的地
        //            Destination destination = session.createTopic(HELLO_ACTIVEMQ_TOPIC);//发布订阅(广播)模式
                    Destination destination  = session.createQueue(HELLO_ACTIVEMQ_QUEUE);//点对点模式
                    //消息的生产者
                    MessageProducer producer = session.createProducer(destination);
                    //发送
                    for(int i=0;i<2;i++){
                        //消息
                        String msg = "发送消息："+i;
                        TextMessage textMessage = session.createTextMessage(msg);
                        producer.send(textMessage);
                        System.out.println("send ["+textMessage.getText()+"] to ["+connection.getClientID()+"]   success...");
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                } finally {
                    if(null != connection){
                        try {
                            connection.close();
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }

2.ActiveMq消费者:同步阻塞获取消息  或者使用异步监听器的方式获取消息
  2.1引入pom
         <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-all</artifactId>
           <version>5.9.0</version>
        </dependency>
  2.2创建连接工厂，创建连接，启动连接，创建会话，创建消息目的地，创建消费者，循环同步阻塞获取消息
  2.3消息有两种模式，点对点p2p和发布订阅(广播)pub/sub模式，在创建消息目的地时指定
  点对点p2p模式：一个消息只有一个消费者，默认会持久化，就是说重启activeMq，消息会自动保存
  发布订阅(广播)pub/sub模式：一个消息有多个消费者消费，默认不会持久化，就是说重启activeMq，消息会丢失，而且如果是生产者先发送消息之后，消费者才启动，那么消费者是获取不到消息的
        public class JMSConsumer {

            public static String ACTIVEMQ_BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL;/*默认连接用户名*/
            public static String ACTIVEMQ_USERNAME = ActiveMQConnection.DEFAULT_USER;/*默认连接用户名*/
            public static String ACTIVEMQ_PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD ;/*默认连接密码*/
            public static String HELLO_ACTIVEMQ_QUEUE = AMQConstant.HELLO_ACTIVEMQ_QUEUE;//消息模式：点对点模式
            public static String HELLO_ACTIVEMQ_TOPIC = AMQConstant.HELLO_ACTIVEMQ_TOPIC;//消息模式：发布订阅(广播)pub/sub模式

            public static void main(String[] args) {
                Connection connection = null;
                try {
                    //连接工厂
                    ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_USERNAME,ACTIVEMQ_PASSWORD,ACTIVEMQ_BROKERURL);
                    //连接
                    connection = connectionFactory.createConnection();
                    //启动连接
                    connection.start();
                    //会话
                    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                    //目的地
        //            Destination destination = session.createTopic(HELLO_ACTIVEMQ_TOPIC);//发布订阅(广播)模式
                    Destination destination  = session.createQueue(HELLO_ACTIVEMQ_QUEUE);//点对点模式
                    //消费者
                    MessageConsumer consumer = session.createConsumer(destination);
                    //消息
                    Message message = null;
                    while( null != (message = consumer.receive())){
                        TextMessage textMessage = (TextMessage) message;
                        System.out.println("receive ["+textMessage.getText()+"] from ["+connection.getClientID()+"]   success...");
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                } finally {
                    if(null != connection){
                        try {
                            connection.close();
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

3.ActivieMQ高级特性-内嵌模式
  3.1 启动EmbedMQ
    public class EmbedMQ {

        public static void main(String[] args) throws Exception {
            BrokerService brokerService = new BrokerService();
            brokerService.setBrokerName("embedMq");
            brokerService.addConnector("tcp://localhost:61000");
            brokerService.setManagementContext(new ManagementContext());
            brokerService.start();
            System.out.println("EmbedMQ start......");
        }
    }
  3.2 生产者
    public class EmbedJmsProducer {

        public static String ACTIVEMQ_BROKERURL = "tcp://localhost:61000";/*自定义端口*/
        public static String ACTIVEMQ_USERNAME = ActiveMQConnection.DEFAULT_USER;/*默认连接用户名*/
        public static String ACTIVEMQ_PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD ;/*默认连接密码*/
        public static String HELLO_ACTIVEMQ_QUEUE = AMQConstant.HELLO_ACTIVEMQ_QUEUE;//消息模式：点对点模式

        public static void main(String[] args) throws JMSException {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_USERNAME,ACTIVEMQ_PASSWORD,ACTIVEMQ_BROKERURL);
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            TextMessage textMessage = session.createTextMessage("内嵌activemq EmbedJmsProducer");
            Destination queue = session.createQueue("embed.queue");
            MessageProducer producer = session.createProducer(queue);
            producer.send(queue,textMessage);
            System.out.println("send ["+textMessage.getText()+"] to ["+connection.getClientID()+"]   success...");
        }
    }
  3.3 消费者
    public class EmbedJmsConsumer {

        public static String ACTIVEMQ_BROKERURL = "tcp://localhost:61000";/*自定义端口*/
        public static String ACTIVEMQ_USERNAME = ActiveMQConnection.DEFAULT_USER;/*默认连接用户名*/
        public static String ACTIVEMQ_PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD ;/*默认连接密码*/
        public static String HELLO_ACTIVEMQ_QUEUE = AMQConstant.HELLO_ACTIVEMQ_QUEUE;//消息模式：点对点模式

        public static void main(String[] args) throws JMSException {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_USERNAME,ACTIVEMQ_PASSWORD,ACTIVEMQ_BROKERURL);
            final Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("embed.queue");
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        TextMessage textMessage = (TextMessage)message;
                        System.out.println("receive ["+textMessage.getText()+"] from ["+connection.getClientID()+"]   success...");
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
4.ActivieMQ高级特性-持久化，默认使用的是kahadb，可以切换成关系型数据库如mysql
    4.1新建库名，如hhn_mq
    4.2修改activemq.xml
        将
        <persistenceAdapter>
             <kahaDB directory="${activemq.data}/kahadb"/>
         </persistenceAdapter>
        修改为
        <persistenceAdapter>
            <jdbcPersistenceAdapter dataSource="#mysql-ds"/>
        </persistenceAdapter>
        并且增加id为mysql-ds的配置
            <bean id="mysql-ds" class="org.apache.commons.dbcp2.BasicDataSource"
                destroy-method="close">
                <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://10.50.10.201:3306/hhn_mq?relaxAutoCommit=true&amp;characterEncoding=UTF-8"/>
                <property name="username" value="develop"/>
                <property name="password" value=""/>
                <property name="poolPreparedStatements" value="true"/>
            </bean>
    4.3如果报错com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Specified key was too long; max key length is 1000 bytes，说明索引有问题，可以自己手工创建
        CREATE TABLE ACTIVEMQ_ACKS (
          CONTAINER VARCHAR (100) NOT NULL,
          SUB_DEST VARCHAR (250),
          CLIENT_ID VARCHAR (100) NOT NULL,
          SUB_NAME VARCHAR (100) NOT NULL,
          SELECTOR VARCHAR (250),
          LAST_ACKED_ID BIGINT,
          PRIMARY KEY (CONTAINER, CLIENT_ID, SUB_NAME)
        ) ;
    4.4 启动完成之后会创建三张表
    SELECT * FROM activemq_msgs;
    SELECT * FROM `activemq_acks`;
    SELECT * FROM `activemq_lock`;
5.ActivieMQ高级特性-消息的持久化订阅
    分别运行订阅模式和P2P模式，可以发现，P2P模式缺省把消息进行持久化，而topic模式是没有的
    5.1一般topic模式实验：
        1、	启动两个消费者，启动一个生产者，发送消息，两个消费者都可以收到。
        2、	关闭一个消费者，生产者发送消息，活跃的消费者可以收到消息，启动被关闭的消费者，无法收到消息。
        3、	关闭所有消费者，生产者发送消息，在ActiveMQ控制台可以看见消息已被接收，关闭再启动ActiveMQ，启动消费者收不到消息。
        如果topic模式下，需要消费者在离线又上线后，不管ActiveMQ是否重启过，都保证可以接受到消息，就需要进行持久化订阅。具体代码参见模块amq-no-spirng包durabletopic

    5.2持久Topic消费者端
        需要设置客户端id：connection.setClientID("DurableTopic____1");
        消息的destination变为 Topic
        消费者类型变为TopicSubscriber
        消费者创建时变为session.createDurableSubscriber(destination,"任意名字，代表订阅名 ");
        运行一次消费者，将消费者在ActiveMQ上进行一次注册。然后在ActiveMQ的管理控制台subscribers页面可以看见我们的消费者。
        效果：
        1、	运行生产者，发布消息，多个消费者可以正常收到。
        2、	关闭一个消费者，运行生产者，发布消息后再启动被关闭的消费者，可以收到离线后的消息；
        3、	关闭所有消费者，运行生产者，发布消息后，关闭ActiveMQ再启动，启动所有消费者，都可以收到消息。
        注意：生产者端无需另外单独配置JmsDurableTopicProducer

        消费者可以有多个，只是客户端id要设置成不一样的idconnection.setClientID("DurableTopic____1")：JmsDurableTopicConsumer，JmsDurableTopicConsumerOther
    5.3消息非持久化
        在生产者JmsDurableTopicProducer中，默认情况下messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT) 是持久化的。
        修改messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);来设置消息本身的持久化属性为非持久化。重复上述实验，可以发现，第1,2点保持不变，但是第三点，当关闭ActiveMQ再启动，消费者关闭后再启动，是收不到消息的。
        说明，即使进行了持久订阅，但是消息本身如果是不持久化的，ActiveMQ关闭再启动，这些非持久化的消息会丢失，进行持久订阅的消费者也是收不到自身离线期间的消息的。

