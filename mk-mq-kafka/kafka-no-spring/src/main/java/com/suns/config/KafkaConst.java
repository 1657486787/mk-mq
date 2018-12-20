/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.config <br>
 *
 * @author mk <br>
 * Date:2018-12-17 11:03 <br>
 */

package com.suns.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * ClassName: KafkaConst <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-17 11:03 <br>
 * @version
 */
public class KafkaConst {

    /*生产者和消费者共用配置常量*/
    public static final String BOOTSTRAP_SERVERS = "bootstrap.servers";
    public static final String LOCAL_BROKER = "127.0.0.1:9092";
    public static final String BROKER_LIST = "127.0.0.1:9092,127.0.0.1:9093";

    /*======================生产者配置常量============================*/
    public static final String KEY_SERIALIZER = "key.serializer";
    public static final String VALUE_SERIALIZER = "value.serializer";

    public static final String STRING_SERIALIZER
            = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String INTEGER_SERIALIZER
            = "org.apache.kafka.common.serialization.IntegerSerializer";
    public static final String DOUBLE_SERIALIZER
            = "org.apache.kafka.common.serialization.DoubleSerializer";
    public static final String LONG_SERIALIZER
            = "org.apache.kafka.common.serialization.LongSerializer";
    public static final String BYTE_ARRAY_SERIALIZER
            = "org.apache.kafka.common.serialization.ByteArraySerializer";
    public static final String BYTE_BUFFER_SERIALIZER
            = "org.apache.kafka.common.serialization.ByteBufferSerializer";
    public static final String BYTE_SERIALIZER
            = "org.apache.kafka.common.serialization.BytesSerializer";

    public static Properties producerConfig(Class<? extends Serializer> keySerializeClazz, Class<? extends Serializer> valueSerializeClazz){
        return producerConfig(keySerializeClazz.getName(), valueSerializeClazz.getName());
    }

    public static Properties producerConfig(String keySerializeName, String valueSerializeName){
        Properties properties = new Properties();
        properties.put(BOOTSTRAP_SERVERS,LOCAL_BROKER);
        properties.put(KEY_SERIALIZER,keySerializeName);
        properties.put(VALUE_SERIALIZER,valueSerializeName);
        return properties;
    }


    /*======================消费者配置常量=========================*/
    public static final String KEY_DESERIALIZER = "key.deserializer";
    public static final String VALUE_DESERIALIZER = "value.deserializer";
    public static final String KEY_GROUP_ID = "group.id";
    public static final String STRING_DESERIALIZER
            = "org.apache.kafka.common.serialization.StringDeserializer";
    public static final String INTEGER_DESERIALIZER
            = "org.apache.kafka.common.serialization.IntegerDeserializer";
    public static final String DOUBLE_DESERIALIZER
            = "org.apache.kafka.common.serialization.DoubleDeserializer";
    public static final String LONG_DESERIALIZER
            = "org.apache.kafka.common.serialization.LongDeserializer";
    public static final String BYTE_ARRAY_DESERIALIZER
            = "org.apache.kafka.common.serialization.ByteArrayDeserializer";
    public static final String BYTE_BUFFER_DESERIALIZER
            = "org.apache.kafka.common.serialization.ByteBufferDeserializer";
    public static final String BYTE_DESERIALIZER
            = "org.apache.kafka.common.serialization.BytesDeserializer";

    public static Properties consumerConfig(String groupId,
                                            Class<? extends Deserializer> keyDeserializeClazz,
                                            Class<? extends Deserializer> valueDeserializeClazz){
        return consumerConfig(groupId,keyDeserializeClazz.getName(), valueDeserializeClazz.getName());
    }

    public static Properties consumerConfig(String groupId,String keyDeserializeName,String valueDeserializeName){
        Properties properties = new Properties();
        properties.put(BOOTSTRAP_SERVERS,LOCAL_BROKER);
        properties.put(KEY_DESERIALIZER,keyDeserializeName);
        properties.put(VALUE_DESERIALIZER,valueDeserializeName);
        properties.put(KEY_GROUP_ID,groupId);
        return properties;
    }

    public static Map<String,Object> consumerConfigMap(String groupId,
                                                       Class<? extends Deserializer> keyDeserializeClazz,
                                                       Class<? extends Deserializer> valueDeserializeClazz){
        Map<String,Object> properties = new HashMap<String, Object>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,LOCAL_BROKER);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,keyDeserializeClazz);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,valueDeserializeClazz);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        return properties;
    }
}
