ActiveMq与springboot整合
1.加入依赖
        <!--activemq start-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-activemq</artifactId>
        </dependency>
        <!--activemq end-->
        <!--activemq-pool start-->
        <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-pool</artifactId>
            <version>5.15.2</version>
        </dependency>
        <!--activemq-pool end-->

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
2.增加配置类ActiveMQConfiguration
    @Bean
    public ConnectionFactory connectionFactory(){
     ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_USERNAME,ACTIVEMQ_PASSWORD,ACTIVEMQ_BROKERURL);
     return activeMQConnectionFactory;
    }
3.点对点模式：
    3.1定义生产者ProductQueue
        @Component
        public class ProducerQueue {

            @Autowired
            private JmsTemplate jmsTemplate;

            public void sendMessage(Destination destination, final String message){
                //消息类型：文本，map，对象，字节，Stream流
                //1.文本
                jmsTemplate.send(destination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createTextMessage(message);
                    }
                });
            }
        }

    3.2定义消费者ConsumerQueue1,可以定义一个，也可以定义多个，如
        @Component
        public class ComsumerQueue1 {

            @JmsListener(destination = "springboot.queue")
            public void receive(String text) {
                System.out.println(this.getClass().getName() + " receive:"+text);
            }
        }
    3.3测试
        @SpringBootTest
        @RunWith(SpringRunner.class)
        public class ProducerQueueTest{

            @Autowired
            private ProducerQueue producerQueue;
            @Test
            public void test1(){
                ActiveMQQueue activeMQQueue = new ActiveMQQueue("springboot.queue");
                for(int i=0;i<10;i++){
        //        producerQueue.sendMessage("springboot.queue","test...");
                    producerQueue.sendMessage(activeMQQueue,"测试springboot.queue,i="+i);
                    System.out.println("send,i="+i);
                }
            }

        }

4.pub/sub模式：

    4.1如果是topic模式需要在配置文件中增加JmsListenerContainerFactory
        @Bean("jmsTopicListenerContainerFactory")
        public JmsListenerContainerFactory jmsTopicListenerContainerFactory(ConnectionFactory connectionFactory){
            DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
            factory.setConnectionFactory(connectionFactory);
            factory.setPubSubDomain(true);
            return factory;
        }

    4.2生产者和queue一样
        @Component
        public class ProducerTopic {

            @Autowired
            private JmsMessagingTemplate jmsMessagingTemplate;
            public void sendMessage(Destination destination, final String message){
                jmsMessagingTemplate.convertAndSend(destination,message);
            }
        }
    4.3消费者，需要在@JmsListener中加containerFactory = "jmsListenerContainerFactory"
    @Component
    public class ConsumerTopic1 {

        @JmsListener(destination = "springboot.topic",containerFactory = "jmsListenerContainerFactory")
        public void receivv(String text){
            System.out.println(this.getClass().getName() + " receive:"+text);
        }
    }

    4.4测试
    @Autowired
        private ProducerTopic producerTopic;
        @Test
        public void testNormalTopic() throws IOException {
            ActiveMQTopic activeMQTopic = new ActiveMQTopic("springboot.topic");
            for(int i=0;i<10;i++){
                producerTopic.sendMessage(activeMQTopic,"测试springboot.topic,i="+i);
                System.out.println("testNormalTopic send,i="+i);
            }
        }

5.request-response请求响应模式，一般线上用的比较多，就是生产者需要得到消费者的结果
    5.1生产者：在生产者发送消息时,设置replayto
    @Service
    public class ProducerR {

        @Autowired
        private JmsTemplate jmsTemplate;
        @Autowired
        private ProducerGetResponse producerGetResponse;

        public void sendMessage(Destination destination, final String message){
            jmsTemplate.send(destination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    TextMessage textMessage = session.createTextMessage(message);

                    //配置，告诉消费者如何应答
    //                Destination temporaryQueue = session.createTemporaryQueue();
                    Destination responseQueue = session.createQueue("springboot.produceR-responseQueue");
                    MessageConsumer responseConsumer = session.createConsumer(responseQueue);
    //                responseConsumer.setMessageListener(producerGetResponse);//生产者监听结果ProducerGetResponse
                    textMessage.setJMSReplyTo(responseQueue);

                    String uid = "UID:"+System.currentTimeMillis();
                    textMessage.setJMSCorrelationID(uid);
                    return textMessage;
                }
            });
        }
    }

    5.2 在生产者这段，监听响应的消息队列
    @Component
    public class ProducerGetResponse{

        @JmsListener(destination = "springboot.produceR-responseQueue")
        public void onMessage(Message message) {
            try {
                TextMessage textMessage = (TextMessage)message;
                System.out.println(this.getClass().getName() + " receive:"+textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    5.3消费者，收到消息后，需要再调用发送消息
    @Component
    public class ConsumerR {

        @Autowired
        private JmsTemplate jmsTemplate;

        @JmsListener(destination = "springboot.replayto.queue")
        public void receive(final Message message, final String text) throws JMSException {
            System.out.println(this.getClass().getName() + " receive:"+text);

            Destination jmsReplyTo = message.getJMSReplyTo();//响应目的地
            jmsTemplate.send(jmsReplyTo, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    TextMessage textMessage = session.createTextMessage("consumerR响应结果："+text);
                    return textMessage;
                }
            });
        }
    }
