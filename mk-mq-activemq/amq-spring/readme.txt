ActiveMQ和Spring结合

1.需要加入activeMq相关依赖
<!-- activemq -->
<dependency>
    <groupId>org.apache.activemq</groupId>
    <artifactId>activemq-all</artifactId>
    <version>5.9.0</version>
</dependency>

<!-- spring-jms -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jms</artifactId>
    <version>4.3.11.RELEASE</version>
</dependency>

2.在spring配置文件中增加如下配置
    注意：需要在命名空间中增加amp:xmlns:amp="http://activemq.apache.org/schema/core"
    2.1 生产者配置
        <!--activemq start -->
        <amp:connectionFactory id="amqConnectionFactory" brokerURL="tcp://127.0.0.1:61616" userName="" password="" />
        <!-- Spring用于管理真正的ConnectionFactory的ConnectionFactory -->
        <bean id="connectionFactory" class="org.springframework.jms.connection.CachingConnectionFactory">
            <!-- 配置连接 -->
            <property name="targetConnectionFactory" ref="amqConnectionFactory"/>
            <!-- 会话的最大连接数 -->
            <property name="sessionCacheSize" value="100"/>
        </bean>

        <!-- Spring JmsTemplate 的消息生产者 start-->
        <!-- 定义JmsTemplate的Queue类型 -->
        <bean id="queueJmsTemplate" class="org.springframework.jms.core.JmsTemplate">
            <constructor-arg ref="connectionFactory"/>
            <property name="pubSubDomain" value="false"/>
        </bean>
        <!-- 定义JmsTemplate的Topic类型 -->
        <bean id="topicJmsTemplate" class="org.springframework.jms.core.JmsTemplate">
            <constructor-arg ref="connectionFactory"/>
            <property name="pubSubDomain" value="true"/>
        </bean>
        <!-- Spring JmsTemplate 的消息生产者 end-->
    2.2 消费者配置
        <!--activemq start -->
        <amp:connectionFactory id="amqConnectionFactory" brokerURL="tcp://127.0.0.1:61616" userName="" password="" />
        <!-- Spring用于管理真正的ConnectionFactory的ConnectionFactory -->
        <bean id="connectionFactory" class="org.springframework.jms.connection.CachingConnectionFactory">
            <!-- 配置连接 -->
            <property name="targetConnectionFactory" ref="amqConnectionFactory"/>
            <!-- 会话的最大连接数 -->
            <property name="sessionCacheSize" value="100"/>
        </bean>

        <!-- activemq 消息消费者 start-->
        <!-- 定义Topic监听器 -->
        <jms:listener-container connection-factory="connectionFactory" acknowledge="auto"
                                destination-type="topic" container-type="default">
            <jms:listener destination="test.topic" ref="topicReceiver1"/>
            <jms:listener destination="test.topic" ref="topicReceiver2"/>
        </jms:listener-container>
        <!-- 定义queue监听器 -->
        <jms:listener-container connection-factory="connectionFactory" acknowledge="auto"
                                destination-type="queue" container-type="default">
            <jms:listener destination="test.queue" ref="queueReceiver1"/>
            <jms:listener destination="test.queue" ref="queueReceiver2"/>
        </jms:listener-container>
        <!-- activemq 消息消费者 end-->
3.启动activeMq客户端
4.启动amq-spring-consumer
5.启动amq-spring-provider，访问http://localhost:8081/ 即可测试

6.activeMq后台管理，访问http://localhost:8161/admin ,用户名密码admin/admin