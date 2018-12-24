/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.config <br>
 *
 * @author mk <br>
 * Date:2018-12-24 14:01 <br>
 */

package com.suns.config;

import com.suns.service.SendListener;
import com.suns.util.SpringBeanUtils;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: KafkaProducerConfig <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-24 14:01 <br>
 * @version
 */
@Configuration
@EnableKafka
public class KafkaProducerConfig {

    @Value("${kafka.producer.servers}")
    private String servers;
    @Value("${kafka.producer.retries}")
    private int retries;
    @Value("${kafka.producer.batch.size}")
    private int batchSize;
    @Value("${kafka.producer.linger}")
    private int linger;
    @Value("${kafka.producer.buffer.memory}")
    private int bufferMemory;

    @Bean
    public Map<String,Object> producerConfigs(){
        Map<String,Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory producerFactory(){
        return new DefaultKafkaProducerFactory(producerConfigs());
    }

    @Bean
    public SendListener producerListener(){
        SendListener sendListener = SpringBeanUtils.getBean("sendListener");
        return sendListener;
    }

    @Bean
    public KafkaTemplate kafkaTemplate(){
        KafkaTemplate kafkaTemplate = new KafkaTemplate(producerFactory());
        kafkaTemplate.setProducerListener(producerListener());
        return kafkaTemplate;
    }

}
