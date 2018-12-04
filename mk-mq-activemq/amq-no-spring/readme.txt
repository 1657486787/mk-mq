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

6.ActivieMQ高级特性-消息的可靠性
    消息发送成功后，接收端接收到了消息。然后进行处理，但是可能由于某种原因，高并发也好，IO阻塞也好，反正这条消息在接收端处理失败了。而点对点的特性是一条消息，只会被一个接收端给接收，只要接收端A接收成功了，接收端B，就不可能接收到这条消息，如果是一些普通的消息还好，但是如果是一些很重要的消息，比如说用户的支付订单，用户的退款，这些与金钱相关的，是必须保证成功的，那么这个时候要怎么处理呢？必须要保证消息的可靠性，除了消息的持久化，还包括两个方面，一是生产者发送的消息可以被ActiveMQ收到，二是消费者收到了ActiveMQ发送的消息。
    生产者
        send方法
        在生产者端，我们会使用send() 方法向ActiveMQ发送消息，默认情况下，持久化消息以同步方式发送，send() 方法会被阻塞，直到 broker 发送一个确认消息给生产者，这个确认消息表示broker已经成功接收到消息，并且持久化消息已经把消息保存到二级存储中。
        实验send()方法：在模块amp-no-spring包msgreliability下的JmsMsgReliablitySendProducer中send方法上打一个断点，可以看到send方法每执行一次，ActiveMQ管理控制台增加一条入队消息，数据库中增加一条消息。
    事务消息
        事务中消息（无论是否持久化），会进行异步发送，send() 方法不会被阻塞。但是commit 方法会被阻塞，直到收到确认消息，表示broker已经成功接收到消息，并且持久化消息已经把消息保存到二级存储中。
        实验事务消息：在模块amp-no-spring包msgreliability下的JmsMsgReliablityTranProducer中在session.commit()打一个断点，可以看到send方法每执行一次，ActiveMQ管理控制台和数据库中没有任何变化，只有执行完session.commit()后ActiveMQ管理控制台和数据库中才增加。
    总结:非持久化又不在事务中的消息，可能会有消息的丢失。为保证消息可以被ActiveMQ收到，我们应该采用事务消息或持久化消息。
    消费者
        对消息的确认有4种机制
        1、	AUTO_ACKNOWLEDGE = 1    自动确认
        2、	CLIENT_ACKNOWLEDGE = 2    客户端手动确认
        3、	DUPS_OK_ACKNOWLEDGE = 3    自动批量确认
        4、	SESSION_TRANSACTED = 0    事务提交并确认
        ACK_MODE描述了Consumer与broker确认消息的方式(时机),比如当消息被Consumer接收之后,Consumer将在何时确认消息。所以ack_mode描述的不是producer于broker之间的关系，而是customer于broker之间的关系。
        对于broker而言，只有接收到ACK指令,才会认为消息被正确的接收或者处理成功了,通过ACK，可以在consumer与Broker之间建立一种简单的“担保”机制.
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        第一个参数:是否支持事务，如果为true，则会忽略第二个参数，自动被jms服务器设置为SESSION_TRANSACTED
    AUTO_ACKNOWLEDGE
        自动确认
            “同步”(receive)方法返回message给消息时会立即确认。
             在"异步"(messageListener)方式中,将会首先调用listener.onMessage(message)，如果onMessage方法正常结束,消息将会正常确认。如果onMessage方法异常，将导致消费者要求ActiveMQ重发消息。此外需要注意，消息的重发次数是有限制的，每条消息中都会包含“redeliveryCounter”计数器，用来表示此消息已经被重发的次数，如果重发次数达到阀值，将导致broker端认为此消息无法消费,此消息将会被删除或者迁移到"dead letter"通道中。
             因此当我们使用messageListener方式消费消息时，可以在onMessage方法中使用try-catch,这样可以在处理消息出错时记录一些信息，而不是让consumer不断去重发消息；如果你没有使用try-catch,就有可能会因为异常而导致消息重复接收的问题,需要注意onMessage方法中逻辑是否能够兼容对重复消息的判断。
        实验方法：在模块amp-no-spring包msgreliability下的JmsMsgReliablityConsumerAsyn中onMessage方法中增加一条throw语句，出现消息重发的现象。
    CLIENT_ACKNOWLEDGE :
        客户端手动确认，这就意味着AcitveMQ将不会“自作主张”的为你ACK任何消息，开发者需要自己择机确认。可以用方法： message.acknowledge()，或session.acknowledge()；效果一样。
        如果忘记调用acknowledge方法，将会导致当consumer重启后，会接受到重复消息，因为对于broker而言，那些尚未真正ACK的消息被视为“未消费”。
        我们可以在当前消息处理成功之后，立即调用message.acknowledge()方法来"逐个"确认消息，这样可以尽可能的减少因网络故障而导致消息重发的个数；当然也可以处理多条消息之后，间歇性的调用acknowledge方法来一次确认多条消息，减少ack的次数来提升consumer的效率，不过需要自行权衡。
        实验方法：在模块amp-no-spring包msgreliability下的JmsMsgReliablityConsumerAsyn中将session模式改为Session.CLIENT_ACKNOWLEDGE，，启动两个消费者，发送消息后，可以看到JmsMsgReliablityConsumerAsyn接收了消息但不确认。当JmsMsgReliablityConsumerAsyn重新启动后，会再一次收到同样的消息。加入message.acknowledge()后该现象消失。
    DUPS_OK_ACKNOWLEDGE
        类似于AUTO_ACK确认机制，为自动批量确认而生，而且具有“延迟”确认的特点，ActiveMQ会根据内部算法，在收到一定数量的消息自动进行确认。在此模式下，可能会出现重复消息，什么时候？当consumer故障重启后，那些尚未ACK的消息会重新发送过来。
    SESSION_TRANSACTED
        当session使用事务时，就是使用此模式。当决定事务中的消息可以确认时，必须调用session.commit()方法，commit方法将会导致当前session的事务中所有消息立即被确认。在事务开始之后的任何时机调用rollback()，意味着当前事务的结束，事务中所有的消息都将被重发。当然在commit之前抛出异常，也会导致事务的rollback。

7.ActivieMQ高级特性-通配符订阅
    Wildcards 用来支持联合的名字分层体系（federated name hierarchies）。它不是JMS 规范的一部分，而是ActiveMQ 的扩展。ActiveMQ 支持以下三种
    wildcards：
    "." 用于作为路径上名字间的分隔符。
    "*" 用于匹配路径上的任何名字。
    ">" 用于递归地匹配任何以这个名字开始的destination。
    订阅者可以明确地指定destination 的名字来订阅消息，或者它也可以使用wildcards 来定义一个分层的模式来匹配它希望订阅的 destination。
    具体情况参见代码，在模块amp-no-spirng包wildcards下。

8.ActivieMQ高级特性-死信队列
    用来保存处理失败或者过期的消息。
    出现下面情况时，消息会被重发：
    i.	事务会话被回滚。
    ii.	事务会话在提交之前关闭。
    iii.	会话使用CLIENT_ACKNOWLEDGE模式，并且Session.recover()被调用。
    iv.	自动应答失败
    当一个消息被重发超过最大重发次数（缺省为6次，消费者端可以修改）时，会给broker发送一个"有毒标记“，这个消息被认为是有问题，这时broker将这个消息发送到死信队列，以便后续处理。
    在配置文件(activemq.xml)来调整死信发送策略。
        <!-- 自定义死信队列的前缀，如queuePrefix="mk.DLQ."-->
        		<destinationPolicy>
        			<policyMap>
        				<policyEntries>
        					<policyEntry queue=">">
        						<deadLetterStrategy>
        							<individualDeadLetterStrategy queuePrefix="mk.DLQ." useQueueForQueueMessages="true" processExpired="false" processNonPersistent="false"/>
        						</deadLetterStrategy>
        					</policyEntry>
        				</policyEntries>
        			</policyMap>
        		</destinationPolicy>
    可以单独使用死信消费者处理这些死信。参见代码，在模块amp-no-spirng包dlq下。
    注意，该代码中展示了如何配置重发策略。同时，重试策略属于ActiveMQ的部分，所以有部分connectionFactory，connection的声明等等不能使用接口，必须使用ActiveMQ的实现。

    生产者DlqProducer：和普通的生产者的一样
    消费者DlqConsumer：在普通的消费者之上，可以设置重试策略（通过抛异常来模拟重试次数）
         //限制了重发次数策略
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(1);
        // 拿到消费者端重复策略map
        RedeliveryPolicyMap redeliveryPolicyMap = connection.getRedeliveryPolicyMap();
        // 将消费者端重发策略配置给消费者
        redeliveryPolicyMap.put(destination,redeliveryPolicy);
    处理死信队列的消费者ProcessDlqConsumer:和普通的消费者一样


8.ActivieMQ高级特性-虚拟Destination
    对于消息发布者来说，就是一个正常的Topic(createTopic)，名称以VirtualTopic.开头。例如VirtualTopic.vtgroup
    对于消息接收端来说，是个队列(createQueue,而不是createTopic)，不同应用里使用不同的前缀作为队列的名称，即可表明自己的身份即可实现消费端应用分组。
      例如Consumer.A.VirtualTopic. vtgroup，说明它是名称为A群组的消费端，同理Consumer.B.VirtualTopic. vtgroup说明是一个名称为B群组的客户端。
    默认虚拟主题的前缀是 ：VirtualTopic.
     消费虚拟地址默认格式：Consumer.*.VirtualTopic.
    参见代码，在模块amp-no-spirng包virtualtopic下

9.ActivieMQ高级特性-组合Destination
    组合队列允许用一个虚拟的destination代表多个destinations。这样就可以通过composite destinations在一个操作中同时向多个destination发送消息。
    多个destination之间采用“,”分割。例如：
      Queue queue = new ActiveMQQueue("FOO.A,FOO.B,FOO.C");
      或
      Destination destination = session.createQueue("my-queue,my-queue2");
    如果希望使用不同类型的destination，那么需要加上前缀如queue:// 或topic://，例如：
        Queue queue = new ActiveMQQueue("cd.queue,topic://cd.mark");
    参见代码，在模块amq-no-spirng包compositedest下

10.ActivieMQ高级特性-集群
    有四种方式：
    1.Pure Master Slave (5.8版本及以下)
    2.Shared File System Master Slave
    3.Shared Jdbc Master Slave
        谁先抢到锁，谁就是Master,对应activemq_locks表，谁先插入谁就是master
    4.Roplicated LevelDB Store(5.9版本+)
        需要配合zookeeper使用，利用了zk的master选举（谁能成功创建节点谁就是master）

    实战一：以下演示使用3.Shared Jdbc Master Slave
        参考代码：在模块amq-no-spirng包jq下
        1.在本地搭建三个activemq，需要在activemq.xml中修改如下配置
            1.不同的机器修改不同的端口号<transportConnector name="openwire" uri="tcp://0.0.0.0:61616?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
            2.可以注释其他的transportConnector
                 <!--
                    <transportConnector name="amqp" uri="amqp://0.0.0.0:5672?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
                    <transportConnector name="stomp" uri="stomp://0.0.0.0:61613?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
                    <transportConnector name="mqtt" uri="mqtt://0.0.0.0:1883?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
                    <transportConnector name="ws" uri="ws://0.0.0.0:61614?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
                -->
            3.修改管理后台端口，在jetty.xml中
            4.修改存储方式为jdbc
                 <!-- 默认持久化用kahadb -->
                   <!--
                    <persistenceAdapter>
                        <kahaDB directory="${activemq.data}/kahadb"/>
                    </persistenceAdapter>
                    -->

                    <!-- 修改持久化用mysql start -->
                    <persistenceAdapter>
                        <jdbcPersistenceAdapter dataSource="#mysql-ds"/>
                    </persistenceAdapter>
                 <!-- 修改持久化用mysql start -->
                <!-- 定义数据源 start-->
                    <bean id="mysql-ds" class="org.apache.commons.dbcp2.BasicDataSource"
                        destroy-method="close">
                        <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
                        <property name="url" value="jdbc:mysql://10.50.10.201:3306/hhn_mq?relaxAutoCommit=true&amp;useUnicode=true&amp;characterEncoding=utf-8&amp;serverTimezone=UTC"/>
                        <property name="username" value="develop"/>
                        <property name="password" value=""/>
                        <property name="poolPreparedStatements" value="true"/>
                    </bean>
                    <!-- 定义数据源 end-->
            5.代码需要修改的地方只有一个，修改brokerurl为："failover:(tcp://localhost:61616,tcp://localhost:61617,tcp://localhost:61618)?randomize=false";


    实战二：以下演示使用4.Roplicated LevelDB Store(5.9版本+)
        和3.Shared Jdbc Master Slave基本一样
        参考代码：在模块amq-no-spirng包jq下
        1.先启动zookeeper,如10.52.10.152:2181
        2.在本地搭建三个activemq，需要在activemq.xml中修改如下配置，其中bind="tcp://0.0.0.0:52623"中的端口号，每台要配置成不一样的
            1.不同的机器修改不同的端口号<transportConnector name="openwire" uri="tcp://0.0.0.0:61616?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>

            2.可以注释其他的transportConnector
             <!--
                <transportConnector name="amqp" uri="amqp://0.0.0.0:5672?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
                <transportConnector name="stomp" uri="stomp://0.0.0.0:61613?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
                <transportConnector name="mqtt" uri="mqtt://0.0.0.0:1883?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
                <transportConnector name="ws" uri="ws://0.0.0.0:61614?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
            -->
            3.修改管理后台端口，在jetty.xml中
            4.修改存储方式为levelDB（其中levelDB是内置的不需要安装）
                <!-- 修改持久化用levelDB start -->
                    <persistenceAdapter>
                        <replicatedLevelDB directory="${activemq.data}/leveldb" replicas="3" bind="tcp://0.0.0.0:52623" zkAddress="10.52.10.152:2181" hostname="localhost" zkPath="/activemq/leveldb-stores" />
                    </persistenceAdapter>
                <!-- 修改持久化用levelDB end -->
            5.代码需要修改的地方只有一个，修改brokerurl为："failover:(tcp://localhost:61616,tcp://localhost:61617,tcp://localhost:61618)?randomize=false";