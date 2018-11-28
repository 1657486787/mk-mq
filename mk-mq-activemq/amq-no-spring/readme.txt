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
    4.1修改activemq.xml
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
