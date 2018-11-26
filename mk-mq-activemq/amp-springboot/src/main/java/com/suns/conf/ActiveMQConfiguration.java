/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.conf <br>
 *
 * @author mk <br>
 * Date:2018-11-26 11:47 <br>
 */

package com.suns.conf;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

/**
 * ClassName: ActiveMQConfiguration <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-26 11:47 <br>
 * @version
 */
@Configuration
@EnableJms
public class ActiveMQConfiguration {

    @Value("${spring.activemq.brokerurl}")
    private String ACTIVEMQ_BROKERURL ;/*默认连接用户名*/
    @Value("${spring.activemq.username}")
    private String ACTIVEMQ_USERNAME ;/*默认连接用户名*/
    @Value("${spring.activemq.password}")
    private String ACTIVEMQ_PASSWORD ;/*默认连接密码*/

    @Bean
    public ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_USERNAME,ACTIVEMQ_PASSWORD,ACTIVEMQ_BROKERURL);
        return activeMQConnectionFactory;
    }


    //topic模式需要增加jmsTopicListenerContainerFactory
    @Bean("jmsTopicListenerContainerFactory")
    public JmsListenerContainerFactory jmsTopicListenerContainerFactory(ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }
}
